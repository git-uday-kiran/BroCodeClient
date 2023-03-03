package com.brocode.utils;

import static com.brocode.startups.Startup.activity;
import static com.brocode.startups.Startup.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.brocode.models.NotificationListener;

import java.util.stream.Stream;

public class Util {

	public static void checkNotificationListenerService() {
		if (!isNotificationListenerRunning())
			requestNotificationListenerPermission(activity);
	}

	public static boolean isNotificationListenerRunning() {
		ComponentName cn = new ComponentName(app.getBaseContext(), NotificationListener.class);
		String flat = Settings.Secure.getString(app.getBaseContext().getContentResolver(), "enabled_notification_listeners");
		return flat != null && flat.contains(cn.flattenToString());
	}

	public static void requestNotificationListenerPermission(Activity activity) {
		new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(app.getApplicationContext(), "allow notification listener permission", Toast.LENGTH_LONG).show());
		Intent i = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		activity.startActivity(i);
	}

	public static void requestUserPermissions() {
		String[] permissions = getManifestPermissions();
		if (!allGranted(permissions)) {
			new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(app.getBaseContext(), "asking user permissions", Toast.LENGTH_LONG).show());
			ActivityCompat.requestPermissions(activity, permissions, 1);
		}
	}

	public static boolean allGranted(String[] permissions) {
		return Stream.of(permissions).allMatch(Util::isGranted);
	}

	public static boolean isGranted(String permission) {
		boolean granted = ActivityCompat.checkSelfPermission(app.getBaseContext(), permission) == PackageManager.PERMISSION_GRANTED;
		if (!granted)
			Log.e("permission", permission + " is not granted");
		return granted;
	}

	public static String[] getManifestPermissions() {
		try {
			PackageInfo info = app.getPackageManager().getPackageInfo(app.getPackageName(), PackageManager.GET_PERMISSIONS);
			return info.requestedPermissions;
		} catch (PackageManager.NameNotFoundException e) {
			Log.e("getManifestPermissions", e.toString());
			return new String[]{};
		}
	}
}
