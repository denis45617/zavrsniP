package com.example.mathspace;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
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
    private AppCompatButton settingsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_settings);
        ActivityUtil.setFullScreen(this.getWindow());

        downloadButton = findViewById(R.id.downloadButton);
        clearButton = findViewById(R.id.clearSettings);
        nickname = findViewById(R.id.nickname);
        code = findViewById(R.id.settingsCode);
        settingsText = findViewById(R.id.getSettingsText);

        SharedPreferences sharedPreferences = getSharedPreferences("GAME_DATA", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String savedNickname = sharedPreferences.getString("NICKNAME", "");
        nickname.setText(savedNickname);

        String savedCode = sharedPreferences.getString("SAVEDCODE", "");
        code.setText(savedCode);

        code.setEnabled(!savedNickname.equals(""));
        downloadButton.setEnabled(!savedCode.equals(""));

        //==========================================OVISNO O NICKNAME I NICKNAME========================================
        if (!nickname.getText().toString().equals("")) {
            code.setHint("Input game code");
        } else {
            code.setHint("Set nickname first!");
        }

        nickname.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                code.setEnabled(!nickname.getText().toString().equals(""));
                if (!nickname.getText().toString().equals("")) {
                    code.setHint("Input game code");
                } else {
                    code.setHint("Set nickname first!");
                }
                downloadButton.setEnabled(!code.getText().toString().equals("") && !nickname.getText().toString().equals(""));
                editor.putString("NICKNAME", nickname.getText().toString());
                editor.apply();
            }
        });

        //==========================================OVISNO CODE I CODE =================================================
        code.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                downloadButton.setEnabled(!code.getText().toString().equals("") && !nickname.getText().toString().equals(""));
                editor.putString("SAVEDCODE", code.getText().toString());
                editor.apply();
            }
        });


        //==========================================DOWNLOAD===========================================================
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DownloadSettingsActivity.this, "Settings downloading...", Toast.LENGTH_LONG).show();
                Thread thread = new Thread(() -> {  //Network ne smije biti na main thread!
                    String downloaded_settings = Network.getMethod("settings/" + sharedPreferences.getString("SAVEDCODE", "1"));
                    if (!downloaded_settings.equals("null")) {
                        editor.putBoolean("HASDOWNLOADED", true);
                        editor.putString("DOWNLOADED_SETTINGS", downloaded_settings);
                    } else {
                        editor.putString("SAVEDCODE", "");
                        editor.putBoolean("HASDOWNLOADED", false);
                    }
                    editor.apply();

                });
                thread.start();
            }
        });

        //==========================================CLEAR===============================================================
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DownloadSettingsActivity.this, "Setting cleared", Toast.LENGTH_SHORT).show();
                code.setText("");
                editor.putString("SAVEDCODE", "");
                editor.putBoolean("HASDOWNLOADED", false);
                editor.putBoolean("USEDEFAULT", true);
                editor.putString("DOWNLOADED_SETTINGS", "");
                editor.apply();
            }
        });

        //==========================================SETTING TEXT========================================================
        settingsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = sharedPreferences.getString("DOWNLOADED_SETTINGS", "null");
                Toast.makeText(DownloadSettingsActivity.this, text, Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityUtil.setFullScreen(this.getWindow());
    }

}
