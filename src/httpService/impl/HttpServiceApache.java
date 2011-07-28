package httpService.impl;

import httpService.HttpService;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.CookiePolicy;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import page.Page;
import page.impl.PageHtml;
/**
 * Test para lectura de paginas
 * @author mcammi
 *
 */
public class HttpServiceApache implements HttpService{

	private HttpClient client;
	private String page = "";
	private PostMethod method; 
	private GetMethod getMethod;

	/** Constructor por defecto */
	public HttpServiceApache(){
		client = new HttpClient();
	}
	
	/**
	 * Constructor con usr y pass para conexion autenticando al Apache con credenciales
	 * @param urlBase
	 * @param credentials
	 */
	public HttpServiceApache(String user, String pass){
		client = new HttpClient();
		client.getParams().setAuthenticationPreemptive(true);
		Credentials defaultcreds = new UsernamePasswordCredentials(user,pass);
		client.getState().setCredentials(AuthScope.ANY, defaultcreds);
		method.getParams().setCookiePolicy("RFC_2109");

	}

	 public static void main(String[] args) throws Exception{
		 HttpServiceApache service = new HttpServiceApache();
		 //Page page = service.getPage("http://vozme.com/text2voice.php?lang=es");
		 Page page = service.getPage("http://www.google.com/");		 
		 System.out.println(page.getResponse());
	 }
	
	/**
	 * Obtención de la página
	 */
	public Page getPage(String url) {
		
	    Page page = new PageHtml(url);
    	PostMethod method = new PostMethod(page.getUrlBase());
	    method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
	    new DefaultHttpMethodRetryHandler(3, false));
	    try {
	    	System.out.print("Obteniendo pagina...");
	    	int statusCode = client.executeMethod(method);
	    	if (statusCode != HttpStatus.SC_OK) {
	    		System.out.println("Error\t" + method.getStatusCode() + "\t" + method.getStatusText() + "\t" + method.getStatusLine());  
	    	}else{
	    	    //String response = method.getResponseBodyAsString();
	    		InputStream stream = method.getResponseBodyAsStream();
	    	    System.out.println("pagina obtenida.");
	    	    page.setResponse(stream);
	    	    stream.close();
	    	    return page;
	    	}
	       
        }catch (UnsupportedEncodingException e) {
        	System.out.println("Unsupported EncodingException");
	    }catch (HttpException e) {
	    	System.out.println("HTTP Exception");
		}catch (IOException e) {
			System.out.println("IO Exception");
		}catch (Exception e) {
			System.out.println("Exception e: " + e);
		}finally {
	        method.releaseConnection();
        }
		return null;
	}
	
	/**
	 * Obtenciï¿½n de la pï¿½gina
	 */
	public InputStream getStream(String url) {
		
    	method = new PostMethod(url);
	    method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
	    new DefaultHttpMethodRetryHandler(3, false));
	    try {
	    	//System.out.print("Obteniendo stream...");
	    	int statusCode = client.executeMethod(method);
	    	if (statusCode != HttpStatus.SC_OK) {
	    		System.out.println("Error\t" + method.getStatusCode() + "\t" + method.getStatusText() + "\t" + method.getStatusLine());  
	    	}else{
	    	    InputStream stream = method.getResponseBodyAsStream();
	    	    //System.out.println("pagina obtenida.");
	    	    return stream;
	    	}
	       
        }catch (UnsupportedEncodingException e) {
        	System.out.println("Unsupported EncodingException");
	    }catch (HttpException e) {
	    	System.out.println("HTTP Exception");
		}catch (IOException e) {
			System.out.println("IO Exception");
		}catch (Exception e) {
			System.out.println("Exception e: " + e);
		}finally {
	        //method.releaseConnection();
        }
		return null;
	}
	
	public InputStream getStreamGet(String url) {
		
    	getMethod = new GetMethod(url);
    	getMethod .getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
	    new DefaultHttpMethodRetryHandler(3, false));
	    try {
	    	//System.out.print("Obteniendo stream...");
	    	int statusCode = client.executeMethod(getMethod );
	    	if (statusCode != HttpStatus.SC_OK) {
	    		System.out.println("Error\t" + getMethod .getStatusCode() + "\t" + getMethod .getStatusText() + "\t" + getMethod .getStatusLine());  
	    	}else{
	    	    InputStream stream = getMethod .getResponseBodyAsStream();
	    	    //System.out.println("pagina obtenida.");
	    	    return stream;
	    	}
	       
        }catch (UnsupportedEncodingException e) {
        	System.out.println("Unsupported EncodingException");
	    }catch (HttpException e) {
	    	System.out.println("HTTP Exception");
		}catch (IOException e) {
			System.out.println("IO Exception");
		}catch (Exception e) {
			System.out.println("Exception e: " + e);
		}finally {
	        //method.releaseConnection();
        }
		return null;
	}
	
	public void releaseConnection(){
		
		if(method != null){
			method.releaseConnection();
		}

		if(getMethod != null){
			getMethod.releaseConnection();
		}
	}
}
 

