package com.brocode.startup;

import android.content.Context;

import com.brocode.MainActivity;

public class Startup {

	public static MainActivity activity;

	public static void init(MainActivity activity) {
		Startup.activity = activity;

		ConManager.initEvents();
	}

	public static Context getApplicationContext() {
		return activity.getApplicationContext();
	}
}
