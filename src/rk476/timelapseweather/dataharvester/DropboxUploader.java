package rk476.timelapseweather.dataharvester;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;
import com.dropbox.core.DbxWriteMode;

public class DropboxUploader {
	private final static String APP_KEY = "gjyi0u0xtnr5596";
	private final static String APP_SECRET = "9n9el5palxd8hji";
	private static String AccessToken = "ncyyaU63pacAAAAAAAAWnYqQZnuElg95KhMu4aHUU6E";
	
	private DbxClient _client = null;
	private DbxRequestConfig _config;
	private DbxWebAuthNoRedirect _webAuth;
	
	public boolean doesFileExist(String path) {
		DbxEntry metadata = null;
		try {
			metadata = _client.getMetadata(path);
		} catch (DbxException e) {
			e.printStackTrace();
			return false;
		}
		return metadata.isFile();
	}

	private void getAccessToken() throws IOException {		
		String authorizeUrl = _webAuth.start();
		System.out.println("1. Go to: " + authorizeUrl);
		System.out.println("2. Click \"Allow\" (you might have to log in first)");
		System.out.println("3. Copy the authorization code.");
		System.out.println();
		AccessToken = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
		
		tryLinkAccount();
	}
	
	private void tryLinkAccount() throws IOException {
		DbxAuthFinish authFinish;
		try {
			authFinish = _webAuth.finish(AccessToken);
			String accessToken = authFinish.accessToken;
			_client = new DbxClient(_config, accessToken);
			System.out.println("Linked account: " + _client.getAccountInfo().displayName);	
		} catch (DbxException e) {
			System.out.println("Failed to link account to uploader: Code invalid");
			getAccessToken();
		}
	}
	
	public DropboxUploader() throws IOException {
		_config = new DbxRequestConfig("TimelapseWeather/1.0", Locale.getDefault().toString());
		_webAuth = new DbxWebAuthNoRedirect(_config, new DbxAppInfo(APP_KEY, APP_SECRET));
		
		tryLinkAccount();
	}
	
	public void uploadFile(String file, String destination) throws Exception {
		File inputFile = new File(file);
		FileInputStream inputStream = new FileInputStream(inputFile);
		try {
		    DbxEntry.File uploadedFile = _client.uploadFile(destination,
		        DbxWriteMode.force(), inputFile.length(), inputStream);
		    System.out.println("Uploaded: " + uploadedFile.toString());
		} finally {
		    inputStream.close();
		}
	}	
}
