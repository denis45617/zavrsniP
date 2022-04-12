package com.example.mathspace;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mathspace.hs.HighScore;
import com.example.mathspace.task.Task;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    private Switch settingsToUse;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ActivityUtil.setFlags(this.getWindow());
        settingsToUse = findViewById(R.id.usedSettingsSwitch);

        sharedPreferences = getSharedPreferences("GAME_DATA", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        HighScore hs = new HighScore(sharedPreferences, 0);

        setDynamicElements();


        findViewById(R.id.defaultSettings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DefaultSettingsActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.downloadSettings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DownloadSettingsActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.clearHighScore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hs.clearHighScore("");
            }
        });

        settingsToUse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                editor.putBoolean("USEDEFAULT", !settingsToUse.isChecked());
                editor.apply();
            }
        });

    }

    private void setDynamicElements() {
        boolean hasDownloadedSettings = sharedPreferences.getBoolean("HASDOWNLOADED", false);
        settingsToUse.setEnabled(hasDownloadedSettings);

        boolean useDefaultSettings = sharedPreferences.getBoolean("USEDEFAULT", true);
        settingsToUse.setChecked(!useDefaultSettings);

    }


    @Override
    protected void onResume() {
        super.onResume();
        ActivityUtil.setFlags(this.getWindow());
        setDynamicElements();
    }
}
