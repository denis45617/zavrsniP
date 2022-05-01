package com.example.mathspace;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class Network {
    private static String BASE_URL = "https://denismath.herokuapp.com/gamecode/mobile/";

    public static String getMethod(String url) {

        StringBuilder text = new StringBuilder();
        try {
            System.out.println(BASE_URL+url);
            URL reqURL = new URL(BASE_URL + url);
            HttpURLConnection request = (HttpURLConnection) (reqURL.openConnection());
            request.setRequestMethod("GET");
            request.connect();

            InputStreamReader in = new InputStreamReader((InputStream) request.getContent());
            BufferedReader buff = new BufferedReader(in);
            String line = "";
            do {
                line = buff.readLine();
                text.append(line);
            } while (line != null);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return text.toString();
    }

}
