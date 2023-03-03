package com.brocode.models;

import android.app.Notification;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.brocode.startups.ConManager;
import com.brocode.startups.Startup;
import com.brocode.utils.NotificationPojo;
import com.brocode.utils.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.socket.client.Socket;

public class NotificationListener extends NotificationListenerService {

	public static NotificationListener instance;

	public static Socket socket = ConManager.getSocket();
	public static ObjectMapper mapper = new ObjectMapper();

	public static final String[] REASONS = {"REASON_CLICK", "REASON_CANCEL", "REASON_CANCEL_ALL", "REASON_ERROR", "REASON_PACKAGE_CHANGED", "REASON_USER_STOPPED", "REASON_PACKAGE_BANNED", "REASON_APP_CANCEL", "REASON_APP_CANCEL_ALL", "REASON_LISTENER_CANCEL", "REASON_LISTENER_CANCEL_ALL", "REASON_GROUP_SUMMARY_CANCELED", "REASON_GROUP_OPTIMIZATION", "REASON_PACKAGE_SUSPENDED", "REASON_PROFILE_TURNED_OFF", "REASON_UNAUTOBUNDLED", "REASON_CHANNEL_BANNED", "REASON_SNOOZED", "REASON_TIMEOUT", "REASON_CHANNEL_REMOVED", "REASON_CLEAR_DATA", "REASON_ASSISTANT_CANCEL"};

	public NotificationListener() {
		instance = this;
		Log.d("NotificationListener", "NotificationListener is running...");
	}

	@Override
	public void onNotificationPosted(StatusBarNotification sbn) {
		NotificationPojo pojo = getNotificationPojo(sbn);
		try {
			String json = mapper.writeValueAsString(pojo);
			socket.emit("e#notification-posted", json);
			Log.i("notification-posted", "sent: " + json);
		} catch (JsonProcessingException e) {
			Log.e("notification-posted", "couldn't not able to parse the pojo " + e);
		}
	}

	@Override
	public void onNotificationRemoved(StatusBarNotification sbn, RankingMap rankingMap, int reason) {
		JSONObject json = new JSONObject();
		try {
			json.put("key", sbn.getKey());
			json.put("reason", REASONS[reason - 1]);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		socket.emit("e#notification-removed", json);
		Log.i("notification-removed", "send: " + json);
	}

	@Override
	public void onListenerConnected() {
		StatusBarNotification[] statusBarNotifications = getActiveNotifications();
		Stream.of(statusBarNotifications).forEach(this::onNotificationPosted);
	}

	private NotificationPojo getNotificationPojo(StatusBarNotification sbn) {
		Notification nf = sbn.getNotification();
		Bundle extras = nf.extras;

		Icon smallIcon = nf.getSmallIcon();
		Icon largeIcon = nf.getLargeIcon();
		byte[] smallIconBytes = getBytes(smallIcon);
		byte[] largeIconBytes = getBytes(largeIcon);
		List<String> actions = getActionTitles(nf.actions);

		var key = sbn.getKey();
		var channelId = nf.getChannelId();
		var packageName = sbn.getPackageName();
		var categoryName = nf.category;
		var timestamp = DateFormat.getTimeInstance().format(new Date(sbn.getPostTime()));
		var title = extras.get(Notification.EXTRA_TITLE);
		var text = extras.get(Notification.EXTRA_TEXT);
		var subText = extras.get(Notification.EXTRA_SUB_TEXT);
		var bigText = extras.get(Notification.EXTRA_BIG_TEXT);
		var summeryText = extras.get(Notification.EXTRA_SUMMARY_TEXT);

		NotificationPojo pojo = NotificationPojo.Builder.newBuilder()
				.setKey(key)
				.setChannelId(channelId)
				.setPackageName(packageName)
				.setCategoryName(categoryName)
				.setTimestamp(timestamp)
				.setTitle(title)
				.setText(text)
				.setSubText(subText)
				.setBigText(bigText)
				.setSummeryText(summeryText)
				.setSmallIconBytes(smallIconBytes)
				.setLargeIconBytes(largeIconBytes)
				.setActions(actions)
				.build();

		Log.d("pojo", pojo.toString());
		return pojo;
	}

	private List<String> getActionTitles(Notification.Action[] actions) {
		if (actions == null)
			return new ArrayList<>();
		return Stream.of(actions)
				.map(action -> action.title)
				.map(String::valueOf)
				.collect(Collectors.toList());
	}

	byte[] getBytes(Icon icon) {
		Bitmap bitmap;
		if (icon == null || (bitmap = getBitmap(icon)) == null)
			return new byte[0];
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		return stream.toByteArray();
	}

	private Bitmap getBitmap(Icon icon) {
		try {
			Drawable drawable = icon.loadDrawable(Startup.app.getBaseContext());
			Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
			drawable.draw(canvas);
			return bitmap;
		} catch (Exception e) {
			Log.e("getBitmap", e.toString());
		}
		return null;
	}

	public static void cancelNotification(Object... args) {
		try {
			checkSingletonIsAlive();
			String key = (String) args[0]; // key
			NotificationListener.getSingleton().cancelNotification(key);
			Log.i("notification-cancel", "key : " + key);
		} catch (Exception e) {
			Log.e("notification-cancel", e.toString());
		}
	}

	public static void cancelAllNotifications(Object... ignoredArgs) {
		try {
			checkSingletonIsAlive();
			NotificationListener.getSingleton().cancelAllNotifications();
			Log.i("notification-cancel-all", "canceled all notifications");
		} catch (Exception e) {
			Log.e("notification-cancel-all", e.toString());
		}
	}

	public static void snoozeNotification(Object... args) {
		try {
			checkSingletonIsAlive();
			String key = args[0].toString(); // key
			long seconds = Long.parseLong(args[1].toString()); // snooze time in seconds
			NotificationListener.getSingleton().snoozeNotification(key, seconds * 1000);
			Log.i("notification-snooze", "snoozed notification, key: " + key + " seconds: " + seconds);
		} catch (Exception e) {
			Log.e("notification-snooze", e.toString());
		}
	}

	public static void checkSingletonIsAlive() throws RuntimeException {
		if (NotificationListener.getSingleton() == null)
			throw new RuntimeException("NotificationListener singleton not initialized yet");
	}

	public static NotificationListener getSingleton() {
		if (instance != null)
			return instance;
		Util.checkNotificationListenerService();
		return instance;
	}
}
