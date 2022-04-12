package com.example.mathspace;

import android.app.Activity;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import static java.security.AccessController.getContext;

public class GameActivity extends AppCompatActivity {
    private int screenX;
    private int screenY;
    private GameView gameView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUtil.setFlags(this.getWindow());
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        gameView = new GameView(this);
        setContentView(gameView);

    }



    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
        ActivityUtil.setFlags(this.getWindow());
    }


    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
        finish();
    }
}
