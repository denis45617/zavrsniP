package com.example.mathspace;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Log {
    private String event;
    private String time;

    public Log(String event) {
        this.event = event;
        this.time = getCurrentTime();
    }

    public static String toJson(List<Log> logs) {
        StringBuilder jsonSb = new StringBuilder();
        jsonSb.append("[");
        for (Log l : logs) {
            jsonSb.append("{\"event\":\"").append(l.event).append("\",")
                    .append("\"time\":\"").append(l.time).append("\"}").append(",");
        }
        jsonSb = new StringBuilder(jsonSb.substring(0, jsonSb.length() - 1));
        jsonSb.append("]");

        return jsonSb.toString();
    }


    private static String getCurrentTime() {
        DateTimeFormatter dtf = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dtf = DateTimeFormatter.ofPattern("dd/MM/yyy HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            return dtf.format(now);
        }

        return "SDK version too low";
    }

}
