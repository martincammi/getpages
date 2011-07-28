package page;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public abstract class Page {

	private String urlBase;  
	private String response;
	private int max;
	
	protected void setDefaultMax(){
		max = 1024;
	}
	
	public int getMax() {
		return max;
	}
	public void setMax(int max) {
		this.max = max;
	}
	public String getUrlBase() {
		return urlBase;
	}
	public void setUrlBase(String urlBase) {
		this.urlBase = urlBase;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public void setResponse(InputStream iStream) {
		
		 BufferedReader reader = new BufferedReader(new InputStreamReader(iStream));
		 StringBuilder sb = new StringBuilder();
		 String line = null;
		 try {
			 while ((line = reader.readLine()) != null) {
				 sb.append(line + "\n");
			 }
		 } catch (Exception e) {
			 sb = new StringBuilder();
		 }
		this.response = sb.toString();
	}
	
}
