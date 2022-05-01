package com.example.mathspace;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import com.example.mathspace.hs.HighScore;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private AppCompatButton playButton;
    private AppCompatButton settingButton;
    private TextView highScoreLabel;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playButton = findViewById(R.id.play);
        settingButton = findViewById(R.id.settings);
        highScoreLabel = findViewById(R.id.highScore);

        sharedPreferences = getSharedPreferences("GAME_DATA", 0);

        ActivityUtil.setFlags(this.getWindow());


        playButton.setOnClickListener(view -> {
            Toast.makeText(MainActivity.this, "Please wait while game loads...", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), GameActivity.class);
            startActivity(intent);
        });

        settingButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        updateHighScoreLabel();
        ActivityUtil.setFlags(this.getWindow());
    }

    public void updateHighScoreLabel() {
        TextView highScoreLabel = (TextView) findViewById(R.id.highScore);
        sharedPreferences = getSharedPreferences("GAME_DATA", 0);

        String gameCode = sharedPreferences.getString("SAVEDCODE", "");
        boolean useDefault = sharedPreferences.getBoolean("USEDEFAULT", true);

        if (useDefault) {
            highScoreLabel.setText("High score: " + sharedPreferences.getInt("HIGH_SCORE", 0));
        } else {
            highScoreLabel.setText("High score for " + gameCode + " is : " +
                    sharedPreferences.getInt("HIGH_SCORE" + gameCode, 0));
        }
    }

}
