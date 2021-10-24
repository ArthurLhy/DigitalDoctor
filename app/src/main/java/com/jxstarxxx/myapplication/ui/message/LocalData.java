package com.jxstarxxx.myapplication.ui.message;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class LocalData {

    public static void saveLastMessage(String chatID, String message, Context context) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(chatID+"_last.txt", Context.MODE_PRIVATE);
            fileOutputStream.write(message.getBytes(StandardCharsets.UTF_8));
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getLastMessage(String chatID, Context context) {
        String message = "0";
        try {
            FileInputStream fileInputStream = context.openFileInput(chatID+"_last.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String thisline;
            while((thisline = bufferedReader.readLine())!= null) {
                stringBuilder.append(thisline);
            }
            message = stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }
}
