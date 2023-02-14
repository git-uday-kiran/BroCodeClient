package com.brocode.startups;

import android.util.Log;

import com.brocode.models.AudioRecorder;
import com.brocode.models.IOSocket;
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
