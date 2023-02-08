package com.brocode.utils;

import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Constants {

	private static final Properties keys;

	static {
		keys = new Properties();
		try {
			InputStream stream = Constants.class.getClassLoader().getResourceAsStream("config.properties");
			keys.load(stream);
			Log.i("properties", keys.size() + " loaded successfully ");
		} catch (NullPointerException e) {
			Log.d("properties", e.toString());
		} catch (IOException e) {
			Log.e("properties", e.toString());
		}
	}

	public static final String SOCKET_URL = keys.getProperty("socket_url", "null");

}
