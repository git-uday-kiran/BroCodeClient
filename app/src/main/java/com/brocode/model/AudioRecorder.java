package com.brocode.model;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.brocode.startup.ConManager;
import com.brocode.startup.Startup;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.Deflater;

public class AudioRecorder {

	private static final byte[] buffer;
	private static AudioRecord recorder;

	private static final int SAMPLE_RATE_IN_HZ = 44100;
	private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
	private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_STEREO;
	private static final int BUFFER_CAPACITY = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ, CHANNEL_CONFIG, AUDIO_FORMAT) * 4;

	static {
		buffer = new byte[BUFFER_CAPACITY];
	}

	public static void startRecording() {
		try {
			Log.i("audio", "starting recording...");
			if (ActivityCompat.checkSelfPermission(Startup.getApplicationContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
				recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE_IN_HZ, CHANNEL_CONFIG, AUDIO_FORMAT, BUFFER_CAPACITY);
			else
				Log.e("AudioRecord", "failed initialization, no permissions for audio recording");
			recorder.startRecording();
		} catch (IllegalStateException e) {
			Log.e("audio", "error in startRecording " + e);
		}
	}

	public static void stopRecording() {
		try {
			sendData();
			recorder.stop();
			recorder.release();
		} catch (IllegalStateException e) {
			Log.e("audio", "error while stop recording");
		}
	}

	public static void sendRecordingAudio(@NonNull long intervalInSeconds) {
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				sendData();
			}
		};
		startRecording();
		new Timer().scheduleAtFixedRate(task, 0, intervalInSeconds * 1000);
	}

	private static void sendData() {
		int readBytes = recorder.read(buffer, 0, BUFFER_CAPACITY);
		Log.i("audio", "sending buffer with length " + buffer.length + ", read from recorder length " + readBytes);
		if (readBytes == 0)
			Log.w("audio", "no bytes available in audio recorder");
		ConManager.getSocket().emit("audio", buffer);
	}

	public static byte[] compress(@NonNull byte[] input) {
		Deflater deflater = new Deflater();
		byte[] buffer = new byte[input.length];
		deflater.setInput(input);
		int compressed = deflater.deflate(buffer, 0, input.length, Deflater.FULL_FLUSH);
		return Arrays.copyOf(buffer, compressed);
	}
}