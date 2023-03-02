package com.brocode.startups;

import android.app.Application;
import android.util.Log;

public class Startup extends Application {

	public static Startup singleton;

	public Startup() {
		if (singleton != null)
			Log.e("Startup singleton", "re-initializing the singleton");
		singleton = this;
		init();
		Log.d("Startup singleton", "initialized");
	}

	public void init() {
		ConManager.initEvents();
	}
}
