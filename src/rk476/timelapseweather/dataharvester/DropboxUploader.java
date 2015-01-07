package rk476.timelapseweather.dataharvester;

import com.dropbox.core.*;
import java.util.Locale;

public class DropboxUploader {
	private final static String APP_KEY = "gjyi0u0xtnr5596";
	private final static String APP_SECRET = "9n9el5palxd8hji";

	@SuppressWarnings("unused")
	public DropboxUploader() {
		DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
		DbxRequestConfig config = new DbxRequestConfig("JavaTutorial/1.0", Locale.getDefault().toString());
		DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config, appInfo);
		
	}
}
