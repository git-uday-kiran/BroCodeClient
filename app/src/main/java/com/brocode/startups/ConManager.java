package com.brocode.startups;

import android.util.Log;

import com.brocode.models.AudioRecorder;
import com.brocode.models.IOSocket;
import com.brocode.models.NotificationListener;
import com.brocode.utils.Constants;

import java.net.URI;

import io.socket.client.Socket;

public class ConManager {

	private static Socket socket = IOSocket.getSocket();

	public static void initEvents() {
		Log.i("Socket initEvents", "initializing event listeners");

		socket.on("e#pcm-send-stop", args -> AudioRecorder.stopRecording());
		socket.on("e#pcm-send-mono", args -> AudioRecorder.sendRecordingAudio(0.5f, "mono"));
		socket.on("e#pcm-send-stereo", args -> AudioRecorder.sendRecordingAudio(0.5f, "stereo"));

		socket.on("e#notification-cancel", ConManager::notificationCancel);
		socket.on("e#notification-cancel-all", ConManager::notificationCancelAll);


	}

	public static void notificationCancel(Object... args) {
		try {
			if (NotificationListener.singleton == null)
				throw new Exception("NotificationListener singleton not initialized yet");
			String key = (String) args[0];
			NotificationListener.singleton.cancelNotification(key);
			Log.i("e#notification-cancel", "key : " + key);
		} catch (Exception e) {
			Log.e("e#notification-cancel",  e.toString());
		}
	}

	public static void notificationCancelAll(Object... args) {
		try {
			if (NotificationListener.singleton == null)
				throw new Exception("NotificationListener singleton not initialized yet");
			NotificationListener.singleton.cancelAllNotifications();
			Log.i("e#notification-cancel-all", "cleared all notifications");
		} catch (Exception e) {
			Log.e("e#notification-cancel-all",  e.toString());
		}
	}

	public static Socket getSocket() {
		return socket;
	}

	public static Socket getNewSocket() {
		return getNewSocket(URI.create(Constants.SOCKET_URL));
	}

	public static Socket getNewSocket(URI uri) {
		return socket = IOSocket.getSocket(uri);
	}
}
