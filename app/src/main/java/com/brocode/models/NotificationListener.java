package com.brocode.models;

import android.app.Notification;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.Parcel;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.brocode.startups.ConManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.socket.client.Socket;

public class NotificationListener extends NotificationListenerService {

	public static Socket socket = ConManager.getSocket();
	public static ObjectMapper mapper = new ObjectMapper();

	public static NotificationListener singleton;

	public NotificationListener() {
		singleton = this;
	}

	@Override
	public void onNotificationPosted(StatusBarNotification sbn) {
		Notification nf = sbn.getNotification();
		Bundle extras = nf.extras;

		String key = sbn.getKey();
		Icon smallIcon = nf.getSmallIcon();
		Icon largeIcon = nf.getLargeIcon();

		NotificationPojo pojo = NotificationPojo.Builder.newBuilder()
				.setKey(sbn.getKey())
				.setTimeStamp(DateFormat.getTimeInstance().format(new Date(sbn.getPostTime())))
				.setTitle(extras.getString(Notification.EXTRA_TITLE))
				.setText(extras.getString(Notification.EXTRA_TEXT))
				.setSmallIconBytes(getBytes(smallIcon))
				.setLargIconBytes(getBytes(largeIcon))
				.setPackageName(sbn.getPackageName())
				.setActions(getActionTitles(nf.actions))
				.build();

		try {
			String json = mapper.writeValueAsString(pojo);
			socket.emit("e#notification-posted", json);
			Log.i("notification-posted", "sent : " + json);
		} catch (JsonProcessingException e) {
			Log.e("notification-posted", "couldn't not able to parse the pojo " + e);
		}
	}

	@Override
	public void onNotificationRemoved(StatusBarNotification sbn) {
		socket.emit("e#notification-removed", sbn.getKey());
		Log.i("notification-removed", sbn.getKey());
	}

	private List<String> getActionTitles(Notification.Action[] actions) {
		if (actions == null)
			return new ArrayList<>();

		return Stream.of(actions)
				.map(action -> action.title)
				.map(String::valueOf)
				.collect(Collectors.toList());
	}

	private byte[] getBytes(Icon icon) {
		if (icon == null)
			return new byte[0];
		Parcel parcel = Parcel.obtain();
		icon.writeToParcel(parcel, 0);
		return parcel.createByteArray();
	}
}
