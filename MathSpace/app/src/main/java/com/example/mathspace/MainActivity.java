package com.example.mathspace;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.mathspace.hs.HighScore;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sharedPreferences = getSharedPreferences("GAME_DATA", 0);
        TextView highScoreLabel = findViewById(R.id.highScore);
        highScoreLabel.setText("High score: " + sharedPreferences.getInt("HIGH_SCORE", 0));

        ActivityUtil.setFlags(this.getWindow());


        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
            }
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
        highScoreLabel.setText("High score: " + sharedPreferences.getInt("HIGH_SCORE", 0));
    }

}
