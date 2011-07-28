package mw;

import httpService.impl.HttpServiceApache;

/**
 * Se encarga de navegar por las pàginas
 * @author martin
 *
 */
public class PageBrowser {

	static final String JPG = "jpg";
	static final String GIF = "gif";
	
	protected HttpServiceApache serviceHttp = new HttpServiceApache(); 
	
	int niveles = 1; /* Cantidad de links a recorrer recursivamente
	 					1. Indica los links de la pagina indicada
	 				 */
	String fileType = "JPG"; /* Tipos de archivos a recoger*/
	
	public int getNiveles() {
		return niveles;
	}

	public void setNiveles(int niveles) {
		this.niveles = niveles;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
}
