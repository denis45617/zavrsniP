package com.example.mathspace.hs;


import android.content.SharedPreferences;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mathspace.R;

public class HighScore extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private int score;

    public HighScore(SharedPreferences sharedPreferences, int score) {
        this.sharedPreferences = sharedPreferences;
        this.score = score;
    }

    public void clearHighScore(String code) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("HIGH_SCORE" + code, 0);
        editor.apply();
    }


    public void saveHighScore() {
        int highscore = sharedPreferences.getInt("HIGH_SCORE", 0);
        String whatHighscore = "HIGH_SCORE";

        boolean useDefault = sharedPreferences.getBoolean("USEDEFAULT", true);
        if (!useDefault) {
            String gameCode = sharedPreferences.getString("SAVEDCODE", "");
            highscore = sharedPreferences.getInt("HIGH_SCORE" + gameCode, 0);
            whatHighscore = "HIGH_SCORE" + gameCode;
        }


        if (score > highscore) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(whatHighscore, score);
            editor.apply();

        }
    }

}
