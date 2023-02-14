package com.brocode.model;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.brocode.startup.ConManager;
import com.brocode.startup.Startup;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.Deflater;

import io.socket.emitter.Emitter;

public class AudioRecorder {

	private static byte[] buffer;
	private static AudioRecord recorder;
	private static Timer timer;

	private static int BUFFER_CAPACITY;
	private static final int SAMPLE_RATE_IN_HZ = 44100;
	private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;

	public static void init(float seconds, String  channel) {
		int CHANNEL_CONFIG = channel.equals("stereo") ? AudioFormat.CHANNEL_IN_STEREO : AudioFormat.CHANNEL_IN_MONO;
		int bits = CHANNEL_CONFIG == AudioFormat.CHANNEL_IN_STEREO ? 32 : 16;
		BUFFER_CAPACITY = (int) (((SAMPLE_RATE_IN_HZ * bits) / 8) * seconds);
		buffer = new byte[BUFFER_CAPACITY];
		try {
			if (ActivityCompat.checkSelfPermission(Startup.getApplicationContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
				recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE_IN_HZ, CHANNEL_CONFIG, AUDIO_FORMAT, BUFFER_CAPACITY);
			else
				throw new ExceptionInInitializerError("failed initialization, no permissions for audio recording");
		} catch (ExceptionInInitializerError e) {
			Log.e("AudioRecord", e.toString());
		}
	}

	private static void startRecording(float seconds, String channel) {
		try {
			Log.i("AudioRecord", "starting recording...");
			init(seconds, channel);
			recorder.startRecording();
			Log.i("AudioRecord", "started...");
		} catch (IllegalStateException e) {
			Log.e("AudioRecord", "error in startRecording " + e);
		}
	}

	public static void stopRecording() {
		try {
			sendData();
			recorder.stop();
			recorder.release();
			timer.cancel();
			Log.i("stopping the audio sending " ,"stoppeed");
		} catch (IllegalStateException e) {
			Log.e("AudioRecord", "error while stop recording " + e);
		}
	}

	public static void sendRecordingAudio(float intervalInSeconds, String channel) {
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				sendData();
			}
		};
		startRecording(intervalInSeconds, channel);
		timer = new Timer();
		 timer.scheduleAtFixedRate(task, 0, (long) (intervalInSeconds * 1000) );
		Log.i("AudioRecord", "timer scheduled to send pcm data for every " + intervalInSeconds + " seconds");
	}

	private static void sendData() {
		int readBytes = recorder.read(buffer, 0, BUFFER_CAPACITY);
		Log.i("AudioRecord", "sending buffer with length " + buffer.length + ", read from recorder length " + (readBytes / 1000f) + "kb");
		if (readBytes == 0)
			Log.w("AudioRecord", "no bytes available in audio recorder");
		Emitter emit = ConManager.getSocket().emit("e#pcm-buffer", buffer);
	}

	public static byte[] compress(byte[] input) {
		Deflater deflater = new Deflater();
		byte[] buffer = new byte[input.length];
		deflater.setInput(input);
		int compressed = deflater.deflate(buffer, 0, input.length, Deflater.FULL_FLUSH);
		return Arrays.copyOf(buffer, compressed);
	}
}