package util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Util {
	
	public static String newLine = System.getProperty("line.separator"); 
	public static double DOLAR = 4.15; 
	public static double GANANCIA = 0.45;
	
	public static Date stringToFecha(String fecha,String formato){
		//SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sdf= new SimpleDateFormat(formato);
		java.util.Date date = new Date();
		try {
			date = sdf.parse(fecha);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static String fechaHoy(){
		SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
		//SimpleDateFormat sdf= new SimpleDateFormat(formato);
		java.util.Date hoy = new Date();
		return sdf.format(hoy);
	}
	
	public static String fechaToString(Date fecha){
		SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(fecha);
	}
	
	public static double doubleRound(double numero, int digits){
		BigDecimal bd = new BigDecimal(numero);
		BigDecimal bd_round = bd.setScale( digits, BigDecimal.ROUND_HALF_UP );
		return bd_round.doubleValue();
	}
	
	public static double parseCurrency(String currency){
		String number = currency.substring(1);
		return Double.parseDouble(number);
	}
	
	
	public static List<String> filterNullOrEmpty(List<String> lista){
		
		List<String> listaNew = new ArrayList<String>(); 
		for (String string : lista) {
			if(string != null && !"".equals(string)){
				listaNew.add(string); 
			}
		}
		return listaNew;
	}
}
