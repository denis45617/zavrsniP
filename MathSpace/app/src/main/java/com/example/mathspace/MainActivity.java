package com.example.mathspace;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.view.WindowCompat;
import com.example.mathspace.hs.HighScore;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sharedPreferences = getSharedPreferences("GAME_DATA", 0);
        HighScore hs = new HighScore(sharedPreferences, 0);
        TextView highScoreLabel = findViewById(R.id.highScore);
        highScoreLabel.setText("High score: " + sharedPreferences.getInt("HIGH_SCORE", 0));


        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


        findViewById(R.id.clearHighScore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hs.clearHighScore("");
                updateHighScoreLabel();
            }
        });

        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateHighScoreLabel();

    }

    public void updateHighScoreLabel() {
        TextView highScoreLabel =(TextView) findViewById(R.id.highScore);
        highScoreLabel.setText("High score: " + sharedPreferences.getInt("HIGH_SCORE", 0));
    }



}
