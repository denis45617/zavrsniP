package com.example.mathspace;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.example.mathspace.adapters.TextWatcherAdapter;


public class DownloadSettingsActivity extends AppCompatActivity {
    private AppCompatButton downloadButton;
    private AppCompatButton clearButton;
    private EditText nickname;
    private EditText code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_settings);
        ActivityUtil.setFullScreen(this.getWindow());

        downloadButton = findViewById(R.id.downloadButton);
        clearButton = findViewById(R.id.clearSettings);
        nickname = findViewById(R.id.nickname);
        code = findViewById(R.id.settingsCode);

        SharedPreferences sharedPreferences = getSharedPreferences("GAME_DATA", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String savedNickname = sharedPreferences.getString("NICKNAME", "");
        nickname.setText(savedNickname);

        String savedCode = sharedPreferences.getString("SAVEDCODE", "");
        code.setText(savedCode);

        code.setEnabled(!savedNickname.equals(""));
        downloadButton.setEnabled(!savedCode.equals(""));


        nickname.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                code.setEnabled(!nickname.getText().toString().equals(""));
                downloadButton.setEnabled(!code.getText().toString().equals("") && !nickname.getText().toString().equals(""));
                editor.putString("NICKNAME", nickname.getText().toString());
                editor.apply();
            }
        });

        code.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                downloadButton.setEnabled(!code.getText().toString().equals("") && !nickname.getText().toString().equals(""));
                editor.putString("SAVEDCODE", code.getText().toString());
                editor.apply();
            }
        });


        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DownloadSettingsActivity.this, "Settings downloaded", Toast.LENGTH_SHORT).show();
                editor.putBoolean("HASDOWNLOADED", true);
                editor.apply();
            }
        });


        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DownloadSettingsActivity.this, "Setting cleared", Toast.LENGTH_SHORT).show();
                code.setText("");
                editor.putString("SAVEDCODE", "");
                editor.putBoolean("HASDOWNLOADED", false);
                editor.putBoolean("USEDEFAULT", true);
                editor.apply();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityUtil.setFullScreen(this.getWindow());
    }

}
