package com.brocode.startups;

import android.app.Application;
import android.util.Log;

import com.brocode.MainActivity;

import java.io.IOException;

public class Startup extends Application {

	public static Application app;
	public static MainActivity activity;

	public Startup() {
		Log.d("Startup", (app != null ? "re-" : "") + "initializing startup application");
		app = this;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		try {
			init();
		} catch (IOException e) {
			Log.d("exception", e.toString());
		}
	}

	public void init() throws IOException {
		ConManager.initEvents();
	}
}
