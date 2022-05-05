package com.example.mathspace;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Observable;
import android.graphics.Color;
import android.icu.number.NumberFormatter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import com.example.mathspace.task.Task;

import java.io.IOException;
import java.util.*;

public class LeaderboardActivity extends AppCompatActivity {

    private TableLayout tableLayout;
    private SharedPreferences sharedPreferences;
    private String data;
    private ViewGroup.LayoutParams params;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);


        sharedPreferences = getSharedPreferences("GAME_DATA", 0);
        tableLayout = findViewById(R.id.leaderboard);

        Thread dohvatiPodatke = new Thread(
                () -> data = Network.getMethod("leaderboard/" + sharedPreferences.getString("SAVEDCODE", "NULL")));
        dohvatiPodatke.start();

        while (data == null || data.equals("NULL")) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Map<String, String> dataMap = processData();
        addToTable(dataMap);

    }


    private Map<String, String> processData() {
        Map<String, String> mapa = new LinkedHashMap<>();
        String[] polje = data.split("#DELIMITER#");

        for (int i = 0; i < polje.length; ++i) {
            if (polje[i] != null && Objects.requireNonNull(polje[i]).contains(":")) {
                System.out.println(polje[i]);
                String[] zapis = polje[i].split(":");
                mapa.put(zapis[0], zapis[1]);
            }
        }


        return mapa;
    }


    // https://stackoverflow.com/a/11744398   weight za text view
    private void addToTable(Map<String, String> dataMap) {
        int position = 1;


        for (Map.Entry<String, String> entry : dataMap.entrySet()) {
            //izrada reda
            TableRow tableRow = getNewTableRow();
            //=========================izrada sadržaja============
            //redni broj
            TextView positionTextView = getNewTextView(position + ".");
            positionTextView.setLayoutParams(new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1.5f));
            //nickname
            TextView nicknameTextView = getNewTextView(entry.getKey());
            nicknameTextView.setLayoutParams(new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 3.5f));
            //highscore
            TextView highscoreTextView = getNewTextView(entry.getValue());
            highscoreTextView.setLayoutParams(new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 2f));

            //dodavanje sadržaja u red
            tableRow.addView(positionTextView);
            tableRow.addView(nicknameTextView);
            tableRow.addView(highscoreTextView);
            //dodavanje reda u tablicu
            tableLayout.addView(tableRow);
            position++;
        }

    }

    private TextView getNewTextView(String text) {
        TextView positionTextView = new TextView(this);
        positionTextView.setText(text);
        positionTextView.setTextColor(Color.BLACK);
        positionTextView.setTextSize(14);
        positionTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);


        return positionTextView;
    }

    private TableRow getNewTableRow() {
        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tableRow.setBackgroundColor(Color.WHITE);
        return tableRow;
    }

}
