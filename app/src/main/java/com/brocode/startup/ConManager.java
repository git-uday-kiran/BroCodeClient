package com.brocode.startup;

import android.util.Log;

import com.brocode.model.AudioRecorder;
import com.brocode.model.IOSocket;
import com.brocode.utils.Constants;

import java.net.URI;

import io.socket.client.Socket;

public class ConManager {

	private static Socket socket = IOSocket.getSocket();

	public static void initEvents() {
		Log.i("Socket initEvents", "initializing event listeners");

		socket.on("e#pcm-start", args -> AudioRecorder.startRecording());
		socket.on("e#pcm-stop", args -> AudioRecorder.stopRecording());
		socket.on("e#pcm-send", args -> AudioRecorder.sendRecordingAudio(2));

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
