package mw;

import httpService.impl.HttpServiceApache;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mw.htmlParserMC.HtmlParserMC;
import mw.htmlParserMC.ParserException;
import page.Page;
import util.ExceptionMsg;
import util.FileUtils;
import util.MutableBoolean;
import util.Util;

/**
 * Search for an specific list of cards and download the prices 
 * @author Martin Cammi
  
   Ej:
   <a class="thumb" href="http://magiccards.info/zen/en/33.html" target="_blank">
   			<img src=http://magiccards.info/scans/en/zen/33.jpg alt="Quest for the Holy Relic" width="125" height="178">
   <//a>
 *
 */

public class MagicSearcherCost {

	private static String BASE_URL = "http://www.mtgmintcard.com/mtg_search_result.php?keywords=";
	private static String DEFAULT_DIRECTORY = "D:/Magic/magicCosts/";
	private static String DEFAULT_FILE_NAME_IN = "listadoCartas.txt";
	private static String DEFAULT_FILE_NAME_OUT = "listadoCartas.out.txt";
	private static String DEFAULT_FILE_HTML = "HtmlPage.txt";
	
	private static int IMPOSSIBLE_COST = 100000;
	public static double DEFAULT_DOLAR_COST = 4.2;
	public static double DEFAULT_GAIN = 0.45;
	
	public static String DOLAR_SIGN = "USD ";
	public static String PESO_SIGN = "$";
	
	public static final String JPG = "jpg";
	public static final String GIF = "gif";
	
	private double dolarCost = DEFAULT_DOLAR_COST;
	private double gain = DEFAULT_GAIN;
	private boolean returnPriceInDolar = false;
	private boolean returnPriceWithGain = true;
	private List<Integer> cardAmount = new ArrayList<Integer>();
	
	protected HttpServiceApache serviceHttp = new HttpServiceApache();
	
	private HashMap<String,Integer> cardsCosts;
	
	public MagicSearcherCost(){}
	
	public MagicSearcherCost(double dolarCost, double gain, boolean returnDolar, boolean returnWithGain){
		this.dolarCost = dolarCost;
		this.gain = gain;
		this.returnPriceInDolar = returnDolar;
		this.returnPriceWithGain = returnWithGain;
	}
	
	/**
	 * Get the card names from a file, this is the client request list for cards to get the prices.
	 * @return a List of card names.
	 * @throws Exception
	 */
	private List<String> cardNames(){
		 
		List<String> cardList = new ArrayList<String>();
		
		FileReader fr;
		try {
			fr = new FileReader(DEFAULT_DIRECTORY + DEFAULT_FILE_NAME_IN);
			BufferedReader br = new BufferedReader(fr);

			String cardName = br.readLine();
			for (int i = 0; cardName != null; i++) {
				if(Character.isDigit(cardName.charAt(0))){
					Integer amount = Integer.parseInt(cardName.charAt(0) + "");
					cardName = cardName.substring(2); //ej: 1 Avatar of Woe
					cardAmount.add(amount);
				}
				cardList.add(cardName);
				cardName = br.readLine();
			}
			
		} catch (Exception e) {
			System.out.print(ExceptionMsg.EXCEPTION_READING_CARD_FILE);
		}
		
		return cardList;
	}
	
	/**
	 * Write the string to a file
	 * @param write
	 * @throws Exception
	 */
	private void writeCardsCosts(String write){
		try {
			FileUtils.writeToFile(DEFAULT_DIRECTORY + DEFAULT_FILE_NAME_OUT, new StringBufferInputStream(write) );
		} catch (IOException e) {
			System.out.print(ExceptionMsg.EXCEPTION_WRITING_CARD_FILE + e.getMessage() + e.getCause());
		}
	}
	
	/**
	 * Gets the prices of every card based on the url passed as parameter
	 * @param url
	 * @throws Exception
	 */
	public void getPrices(){
		String url = BASE_URL;
		
		List<String> cardNames = cardNames();
		StringBuffer sb = new StringBuffer();
		double sum = 0;
		List<String> cardNameFiltered = Util.filterNullOrEmpty(cardNames);
		String sign = PESO_SIGN;
		int index = 0;
		for (String cardName : cardNameFiltered) {
			
			String cardFormatted = cardName.replaceAll(" ", "+");
			double finalCost = 0;
			MutableBoolean hayStock = new MutableBoolean(false);
			try{
				Page page = serviceHttp.getPage(url + cardFormatted);
				String response = page.getResponse(); 
				//String response = getResponseFromFile(); //Just for testing purposes
				
				double cardCost = getBestCardCost(response, hayStock);
				
				double sellWin;
				double sellCost; 
				
				
				if(returnPriceInDolar){
					dolarCost = 1;
					sign = DOLAR_SIGN;
				}
				
				if(returnPriceWithGain){
					sellWin = (cardCost * gain * dolarCost);
					sellCost = (cardCost * dolarCost) + sellWin;
					//finalCost = Math.ceil(sellCost + 0.5d);
					finalCost = Math.ceil(sellCost *100)/100;
				}else{
					finalCost = Math.ceil(cardCost * dolarCost * 100)/100;
				}
				if(cardAmount.size() > 0){
					sum = sum + (finalCost * cardAmount.get(index));
				}else{
					sum = sum + finalCost;
				}
				
				
				
				if(hayStock.value){
					sb.append(cardAmount.get(index) + " " + cardName + " (" + sign + finalCost + " c/u)");
					System.out.println(cardAmount.get(index) + " " + cardName + " (" + sign + finalCost + " c/u)");
				}else if(finalCost == -1){
					sb.append(cardAmount.get(index) + " " + cardName + " No existe");
					System.out.println(cardName + " No existe");
				}else{
					sb.append(cardAmount.get(index) + " " + cardName + " (" + sign + finalCost + " c/u) (Out of Stock)");
					System.out.println(cardAmount.get(index) + " " + cardName + " (" + sign + finalCost + " c/u) (Out of Stock)");
				}
				sb.append(Util.newLine);
				
			}catch (Exception e) {
				sb.append(cardName + " " + e.getMessage());
				System.out.println(cardName + ": " + e.getMessage());
				sb.append(Util.newLine);
			}finally{
				serviceHttp.releaseConnection();
				
			}
			index++;
		}
		
		sum = Math.ceil(sum * 100)/100;
		sb.append(Util.newLine);
		sb.append("Total: " + sign + sum);
		writeCardsCosts(sb.toString());
		cardAmount.clear();
	}

	private double getBestCardCost(String response, MutableBoolean stock) throws ParserException{
		HtmlParserMC htmlParser = new HtmlParserMC(response);
		
		//htmlParser.parseTable();
		//htmlParser.nextTable(4);
		//htmlParser.parseTableWithAttribute("class", "resultList");
		
		double precioInicial = IMPOSSIBLE_COST; 
		double mejorPrecio = precioInicial; //Valor arbitrario
		double mejorPrecioOutStock = precioInicial; //Valor arbitrario
		boolean finPrecios = false;
		htmlParser.parseMagicTable();
		htmlParser.parseRow();
		stock.value = false;
		
		while(!finPrecios){
			
			String nextRow = htmlParser.nextRow();
			htmlParser.parseColumn();
			htmlParser.nextColumn(8);
			String strCost = htmlParser.extractPrice();
			double precioCarta = Util.parseCurrency(strCost);
			htmlParser.nextColumn(0);
			String cantidad = htmlParser.extractQuantity();
			
			if(!HtmlParserMC.NO_QUANTITY.equals(cantidad)){
				if(precioCarta < mejorPrecio){
					mejorPrecio = precioCarta;	
				}
				stock.value = true;
			}else{
				if(precioCarta < mejorPrecioOutStock){
					mejorPrecioOutStock = precioCarta;	
				}
			}
			
			if(nextRow == null){
				finPrecios = true;
			}
			
			
		}
		
		if(!stock.value){
			mejorPrecio = mejorPrecioOutStock;
		}
		
		if(mejorPrecio == precioInicial){
			mejorPrecio = -1;
		}
		
		return mejorPrecio;
	}

	/**
	 * Just for testing purposes
	 * @return
	 * @throws IOException
	 */
	private String getResponseFromFile() throws IOException {
		
		FileReader fr = new FileReader(DEFAULT_DIRECTORY + DEFAULT_FILE_HTML);
		BufferedReader br = new BufferedReader(fr);
		
		String response = "";
		String s;
		while((s = br.readLine()) != null) { 
			response = response + s;
		}
		return response;
	}
	

	

	
}
