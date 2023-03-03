package com.brocode.startups;

import android.app.Application;
import android.util.Log;

import com.brocode.MainActivity;

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
		init();
	}

	public void init() {
		ConManager.initEvents();
	}
}
