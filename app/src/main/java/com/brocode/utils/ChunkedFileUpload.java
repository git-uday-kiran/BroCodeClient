package com.brocode.utils;
import android.util.Log;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ChunkedFileUpload {

    public static void main(String[] args) {
        String serverUrl = "http://your-server-url/upload";
        String filePath = "path/to/your/bigfile.ext";

        try {
            uploadFileInChunks(serverUrl, filePath);
            System.out.println("File uploaded successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void uploadFileInChunks(String serverUrl, String filePath) throws IOException {
        File file = new File(filePath);
        long fileSize = file.length();
        int chunkSize = 500 * 1024; // 500 kb chunk size (adjust as needed)
        byte[] buffer = new byte[chunkSize];

        FileInputStream fis = new FileInputStream(file);
        URL url = new URL(serverUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/zip");

        for (int bytesRead; (bytesRead = fis.read(buffer)) != -1; ) {
            Log.d("fileUpload", "sending Bytes to The Srever");
            connection.getOutputStream().write(buffer, 0, bytesRead);
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            Log.d("U-sucess","upload sucess to the server");
        } else {
            Log.d("u-fail","faoled");
        }

        fis.close();
        connection.disconnect();
    }
}
