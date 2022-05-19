package com.example.mathspace;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;


public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private AppCompatButton playButton;
    private AppCompatButton settingButton;
    private AppCompatButton leaderboardButton;
    private TextView highScoreLabel;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playButton = findViewById(R.id.play);
        settingButton = findViewById(R.id.settings);
        highScoreLabel = findViewById(R.id.highScore);
        leaderboardButton = findViewById(R.id.leaderboardButton);

        sharedPreferences = getSharedPreferences("GAME_DATA", 0);


        if (sharedPreferences.getString("HAS_SOFTWARE_BUTTON", null) == null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("HAS_SOFTWARE_BUTTON", hasSoftKeys());
            editor.apply();
        }
        GameViewDrawUtil.has_software_keys = sharedPreferences.getString("HAS_SOFTWARE_BUTTON", null).equals("true");
        Toast.makeText(getApplicationContext(), sharedPreferences.getString("HAS_SOFTWARE_BUTTON", null),
                Toast.LENGTH_LONG).show();


        ActivityUtil.setFlags(this.getWindow());

        playButton.setOnClickListener(view -> {
            playButton.setText("Loading...");
            Intent intent = new Intent(getApplicationContext(), GameActivity.class);
            startActivity(intent);
        });

        settingButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        });

        leaderboardButton.setOnClickListener(view -> {
            leaderboardButton.setText("Loading...");
            Intent intent = new Intent(getApplicationContext(), LeaderboardActivity.class);
            startActivity(intent);

        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        playButton.setText("Play");
        leaderboardButton.setText("Leaderboard");

        updateHighScoreLabel();
        updateLeaderboardButtonVisibility();
        ActivityUtil.setFlags(this.getWindow());
    }

    private void updateLeaderboardButtonVisibility() {
        boolean useDefault = sharedPreferences.getBoolean("USEDEFAULT", true);

        if (useDefault) {
            leaderboardButton.setVisibility(View.INVISIBLE);
        } else {
            leaderboardButton.setVisibility(View.VISIBLE);
        }
    }

    public void updateHighScoreLabel() {
        TextView highScoreLabel = (TextView) findViewById(R.id.highScore);
        sharedPreferences = getSharedPreferences("GAME_DATA", 0);

        String gameCode = sharedPreferences.getString("SAVEDCODE", "");
        boolean useDefault = sharedPreferences.getBoolean("USEDEFAULT", true);

        if (useDefault) {
            highScoreLabel.setText("High score: " + sharedPreferences.getInt("HIGH_SCORE", 0));
            leaderboardButton.setVisibility(View.INVISIBLE);
        } else {
            highScoreLabel.setText("High score for " + gameCode + " is : " +
                    sharedPreferences.getInt("HIGH_SCORE" + gameCode, 0));
            leaderboardButton.setVisibility(View.VISIBLE);
        }
    }

    //https://stackoverflow.com/questions/14853039/how-to-tell-whether-an-android-device-has-hard-keys/14871974#14871974
    public String hasSoftKeys() {
        boolean hasSoftwareKeys = true;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Display d = getApplicationContext().getDisplay();

            DisplayMetrics realDisplayMetrics = new DisplayMetrics();
            d.getRealMetrics(realDisplayMetrics);

            int realHeight = realDisplayMetrics.heightPixels;
            int realWidth = realDisplayMetrics.widthPixels;

            DisplayMetrics displayMetrics = new DisplayMetrics();
            d.getMetrics(displayMetrics);

            int displayHeight = displayMetrics.heightPixels;
            int displayWidth = displayMetrics.widthPixels;

            hasSoftwareKeys = (realWidth - displayWidth) > 0 ||
                    (realHeight - displayHeight) > 0;
        } else {
            boolean hasMenuKey = ViewConfiguration.get(getApplicationContext()).hasPermanentMenuKey();
            boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            hasSoftwareKeys = !hasMenuKey && !hasBackKey;
        }

        if (hasSoftwareKeys)
            return "true";
        return "false";
    }

}
