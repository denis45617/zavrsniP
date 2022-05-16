package com.example.mathspace;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;


public class Network {
    private static final String BASE_URL = "https://denismath.herokuapp.com/gamecode/mobile/";

    public static String getMethod(String url) {

        StringBuilder text = new StringBuilder();
        try {
            System.out.println(BASE_URL + url);
            URL reqURL = new URL(BASE_URL + url);
            HttpURLConnection request = (HttpURLConnection) (reqURL.openConnection());
            request.setRequestMethod("GET");
            request.connect();

            InputStreamReader in = new InputStreamReader((InputStream) request.getContent());
            BufferedReader buff = new BufferedReader(in);
            String line;
            do {
                line = buff.readLine();
                text.append(line);
            } while (line != null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return text.toString();
    }

    public static String postMethod(String url2, Map<String, String> body) {
        String toBeReturned = null;
        try {

            URL url = new URL(BASE_URL + url2);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //Various settings of HttpURLConnection
            //Set HTTP method to POST
            conn.setRequestMethod("POST");
            //Allow body submission of request
            conn.setDoInput(true);
            //Allow body reception of response
            conn.setDoOutput(true);
            //Specify Json format
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

            // 2.Establish a connection
            conn.connect();

            //===============================MAKING JSON FROM GIVEN body MAP============================================
            StringBuilder makingJson = new StringBuilder();
            makingJson.append("{");

            for (Map.Entry<String, String> keyvalue : body.entrySet()) {
                if (!keyvalue.getValue().startsWith("[")) {
                    makingJson.append("\"").append(keyvalue.getKey()).append("\":\"").append(keyvalue.getValue()).append("\",");
                } else {
                    makingJson.append("\"").append(keyvalue.getKey()).append("\":").append(keyvalue.getValue()).append(",");
                }

            }
            //makni zadnji zarez
            makingJson = new StringBuilder(makingJson.substring(0, makingJson.length() - 1));
            makingJson.append("}");
            String json = makingJson.toString();
            System.out.println(json);

            //===============================MAKING JSON FROM GIVEN body MAP============================================
            // 3.Write to request and body
            //Get OutputStream from HttpURLConnection and write json string
            PrintStream ps = new PrintStream(conn.getOutputStream());
            ps.print(json);
            ps.close();


            // 4.Receive a response
            //HttpStatusCode 200 is returned at the end of normal operation
            if (conn.getResponseCode() != 200) {
                //Error handling
            }

            //Get InputStream from HttpURLConnection and read
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            toBeReturned = sb.toString();

            // 5.Disconnect
            conn.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return toBeReturned;
    }
}
