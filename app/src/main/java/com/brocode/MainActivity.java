package com.brocode;

import static com.brocode.utils.Util.checkNotificationListenerService;
import static com.brocode.utils.Util.requestUserPermissions;

import android.app.Activity;
import android.os.Bundle;

import com.brocode.startups.Startup;

public class MainActivity extends Activity {

	public MainActivity() {
		Startup.activity = this;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestUserPermissions();
		checkNotificationListenerService();
		finish();
	}
}
