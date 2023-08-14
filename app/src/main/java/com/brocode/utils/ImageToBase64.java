package com.brocode.utils;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ImageToBase64 {
    public static void main(String[] args) {
        // Specify the path to the image file
        String imagePath = "path_to_your_image.png"; // Update with your image path

        try {
            // Read the image file as bytes
            byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));

            // Encode the image bytes to base64
            String base64Encoded = Base64.getEncoder().encodeToString(imageBytes);

            // Print the base64 encoded string
            System.out.println("Base64 encoded image:\n" + base64Encoded);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public  static  String getbase64Image(String imagePAth){
        String imagePath = imagePAth; // Update with your image path

        try {
            // Read the image file as bytes
            byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));

            // Encode the image bytes to base64
            String base64Encoded = Base64.getEncoder().encodeToString(imageBytes);

            // Print the base64 encoded string
            return  base64Encoded;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}

