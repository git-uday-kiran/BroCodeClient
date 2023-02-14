package com.brocode.models;

import android.util.Log;

import com.brocode.utils.Constants;

import java.net.URI;

import io.socket.client.IO;
import io.socket.client.Socket;

public class IOSocket {
	private static final IO.Options options;

	static {
		options = IO.Options.builder()
				.setForceNew(true)
				.setReconnection(true)
				.setReconnectionDelay(1_000)
				.setReconnectionDelayMax(5_000)
				.setReconnectionAttempts(Integer.MAX_VALUE)
				.setRandomizationFactor(0.5d)
				.setTimeout(20_000)
				.setUpgrade(true)
				.build();
	}

	public static Socket getSocket(URI uri) {
		Log.d("socket", "connecting socket...");
		try {
			Socket socket = IO.socket(uri, options).connect();
			Log.i("socket", "successfully created socket " + socket);
			return socket;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Socket getSocket() {
		return getSocket(URI.create(Constants.SOCKET_URL));
	}
}