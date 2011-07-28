package mw;

import httpService.impl.HttpServiceApache;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import util.FileUtils;

/**
 * Busca imagnes con links y accede a ellos hasta que el link sea a una imagen entonces la baja. 
 * @author martin
  
   Ej:
   <a class="thumb" href="http://magiccards.info/dvd/en/1.html" target="_blank">
   			<img src="http://magiccards.info/scans/en/dvd/1.jpg" alt="Akroma, Angel of Wrath" width="125" height="160">
   <//a>
 *
 */

public class MagicSearcherImage {

	private static String urlIndex = "http://www.mtgmintcard.com/mtg_search_result.php?keywords=";
	static final String JPG = "jpg";
	static final String GIF = "gif";

	protected HttpServiceApache serviceHttp = new HttpServiceApache();
	
	private String directory = "/media/Back Up/Magic/magicImages/";
	private int index = 0;
	private boolean createDir = false;
	String fileType;
	
	public MagicSearcherImage(){
		this.fileType = PageBrowser.JPG; 
	}
	
	public MagicSearcherImage(boolean createDir){
		this();
		this.createDir = createDir;
	}
	
	public void getImages(){
		index = 0;
		
		String url = urlIndex;

		Map sets = getExpansions();
		Set<String> abbreviatures = getMeanings(sets);
		
		for (String abbr : abbreviatures) {
			
			String aDirectory = directory + abbr + "/";
		
			Map<String,Integer> manyCards = getCardsPerExpansion();
			
			createDir = true;
			int max = manyCards.get(abbr);
			for (int i = 1; i <= max; i++) {
				
				String fileName = String.valueOf(i);
				fileName = fileName + ".jpg";
			
				String aUrl = url + "/" + abbr + "/" + fileName;
				
				System.out.print("bajando imagen: " + fileName + "...");
				try {
					FileUtils.writeToFile(aDirectory + fileName, serviceHttp.getStreamGet(aUrl),createDir);
					createDir = false; //para que no siga creando
				} catch (IOException e) {
					System.out.print(e.getMessage() + "...");
				}
				System.out.println("OK");
				
				serviceHttp.releaseConnection();
			}
		}
	}
	
	private Map getExpansions(){
		
		Map<String,String> sets = new HashMap<String,String>();
		
		//sets.put("Shards of Alara", "ala");
		//sets.put("Conflux", "cfx");
		//sets.put("Alara Reborn", "arb");
		//sets.put("Zendikar", "zen");
		//sets.put("Worldwake", "wwk");
		//sets.put("Rise of Eldrazi", "roe");
		//sets.put("M10", "m10");
		//sets.put("M11", "m11");
		sets.put("Shards of Mirrodin", "som");
		
		return sets;
	}
	
	private Map getCardsPerExpansion(){
		
		Map<String,Integer> sets = new HashMap<String,Integer>();
		
		//sets.put("ala", 249);
		//sets.put("cfx", 145);
		//sets.put("arb", 145);
		//sets.put("zen", 249);
		//sets.put("wwk", 145);
		//sets.put("roe", 248);
		//sets.put("m10", 249);
		//sets.put("m11", 249);
		sets.put("som", 249);
		
		return sets;
	}
	
	private Set getMeanings(Map map){
		Set set = new HashSet();
		
		for (Object obj : map.keySet()) {
			set.add(map.get(obj));
		}
		
		return set;
	}
	
	private boolean isJpg(String str){
		return ((str.toLowerCase()).indexOf(fileType) >= 0);
	}
	
	public static String parseFileName(String url) {
		int lastBS = url.lastIndexOf("/");
		return url.substring(lastBS + 1);
	}
	
	public static String parseDirName(String url) {
		int lastBS = url.lastIndexOf("/");
		String strAux = url.substring(0, lastBS); 
		int lastBS2 = strAux.lastIndexOf("/");
		return strAux.substring(lastBS2 + 1);
	}
	
}
