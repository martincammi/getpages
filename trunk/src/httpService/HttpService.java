package httpService;

import java.io.InputStream;

import page.Page;

public interface HttpService {

	public static String LOTO = "LOTO";
	public static String STATICS = "STATICS";

	public Page getPage(String url);
	public InputStream getStream(String url);
}
