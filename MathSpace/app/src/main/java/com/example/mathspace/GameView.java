package com.example.mathspace;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceView;
import com.example.mathspace.visual.Background;
import com.example.mathspace.visual.Saw;


public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    private boolean isNotPaused;
    private final Background[] backgrounds = new Background[19];
    private Background currentDownBackGround;
    private Background currentUpBackground;
    private int currentBackgroundIndex = 1;
    private int screenX, screenY;
    private Paint paint;
    private int score = 0;
    private int numberOfLives = 3;
    private Bitmap heart;
    private String whatToCollect = "even numbers";
    private Saw saw;


    @SuppressLint("ClickableViewAccessibility")
    public GameView(Context context) {
        super(context);

        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        this.screenX = size.x;
        this.screenY = size.y;

        paint = new Paint();
        backgrounds[0] = new Background(screenX, screenY, getResources(), R.drawable.a1);
        backgrounds[1] = new Background(screenX, screenY, getResources(), R.drawable.a2);
        backgrounds[2] = new Background(screenX, screenY, getResources(), R.drawable.a3);
        backgrounds[3] = new Background(screenX, screenY, getResources(), R.drawable.a4);
        backgrounds[4] = new Background(screenX, screenY, getResources(), R.drawable.a5);
        backgrounds[5] = new Background(screenX, screenY, getResources(), R.drawable.a6);
        backgrounds[6] = new Background(screenX, screenY, getResources(), R.drawable.a7);
        backgrounds[7] = new Background(screenX, screenY, getResources(), R.drawable.a8);
        backgrounds[8] = new Background(screenX, screenY, getResources(), R.drawable.a9);
        backgrounds[9] = new Background(screenX, screenY, getResources(), R.drawable.a10);
        backgrounds[10] = new Background(screenX, screenY, getResources(), R.drawable.a11);
        backgrounds[11] = new Background(screenX, screenY, getResources(), R.drawable.a12);
        backgrounds[12] = new Background(screenX, screenY, getResources(), R.drawable.a13);
        backgrounds[13] = new Background(screenX, screenY, getResources(), R.drawable.a14);
        backgrounds[14] = new Background(screenX, screenY, getResources(), R.drawable.a15);
        backgrounds[15] = new Background(screenX, screenY, getResources(), R.drawable.a16);
        backgrounds[16] = new Background(screenX, screenY, getResources(), R.drawable.a17);
        backgrounds[17] = new Background(screenX, screenY, getResources(), R.drawable.a18);
        backgrounds[18] = new Background(screenX, screenY, getResources(), R.drawable.a18);

        backgrounds[0].setY(0);
        backgrounds[1].setY(-screenY);

        currentDownBackGround = backgrounds[0];
        currentUpBackground = backgrounds[1];


        saw = new Saw(screenX, screenY, getResources(), R.drawable.saw1, R.drawable.saw2);

        heart = BitmapFactory.decodeResource(getResources(), R.drawable.heart);
        heart = Bitmap.createScaledBitmap(heart, 100, 100, false);
        OnTouchListener touchListener = (v, event) -> {

            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                saw.setX((int)event.getX());
            }

            return true;
        };
        this.setOnTouchListener(touchListener);
    }

    @Override
    public void run() {
        while (isNotPaused) {
            updateBackground();
            draw();
            sleep();
        }
    }


    private void updateBackground() {
        currentDownBackGround.setY( currentDownBackGround.getY() + 5);
        currentUpBackground.setY(currentUpBackground.getY() + 5);
        score += 1;

        if (currentDownBackGround.getY() > screenY) {
            currentDownBackGround = currentUpBackground;
            if (currentBackgroundIndex < 18)       //ako ima još različitih slika
                currentUpBackground = backgrounds[++currentBackgroundIndex];  //neka bude sljedeća
            else {
                currentDownBackGround = backgrounds[17];  //Inače vrti zadnju u kruh
                currentDownBackGround.setY(0);
                currentUpBackground = backgrounds[18];
            }
            currentUpBackground.setY(-screenY);
        }
    }


    private void draw() {
        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            //pozadina
            canvas.drawBitmap(currentUpBackground.getBackground(), currentUpBackground.getX(), currentUpBackground.getY(), paint);
            canvas.drawBitmap(currentDownBackGround.getBackground(), currentDownBackGround.getX(), currentDownBackGround.getY(), paint);

            //score
            paint.setTextSize(100);
            paint.setColor(Color.WHITE);
            canvas.drawText("Score: " + score / 10, 20, 100, paint);

            //what to collect
            paint.setTextSize(50);
            canvas.drawText("Collect: " + whatToCollect, 20, screenY + 100, paint);

            paint.setColor(Color.RED);
            canvas.drawLine(0, saw.getY() + (float) saw.getSaw().getHeight() / 2 - 1, screenX, saw.getY() + (float) saw.getSaw().getHeight() / 2 - 1, paint);
            canvas.drawLine(0, saw.getY() + (float) saw.getSaw().getHeight() / 2 - 2, screenX, saw.getY() + (float) saw.getSaw().getHeight() / 2 - 2, paint);
            canvas.drawLine(0, saw.getY() + (float) saw.getSaw().getHeight() / 2 - 3, screenX, saw.getY() + (float) saw.getSaw().getHeight() / 2 - 3, paint);
            canvas.drawLine(0, saw.getY() + (float) saw.getSaw().getHeight() / 2 - 4, screenX, saw.getY() + (float) saw.getSaw().getHeight() / 2 - 4, paint);
            canvas.drawLine(0, saw.getY() + (float) saw.getSaw().getHeight() / 2 - 5, screenX, saw.getY() + (float) saw.getSaw().getHeight() / 2 - 5, paint);
            canvas.drawLine(0, saw.getY() + (float) saw.getSaw().getHeight() / 2 - 6, screenX, saw.getY() + (float) saw.getSaw().getHeight() / 2 - 6, paint);

            canvas.drawBitmap(saw.getSaw(), saw.getX() - (float) saw.getSaw().getWidth() / 2, saw.getY(), null);



            //hearts
            for (int i = 0; i < numberOfLives; ++i) {
                canvas.drawBitmap(heart, screenX - 100 - heart.getWidth() * i, 10, paint);
            }

            if (score == 1000) {
                numberOfLives--;
                whatToCollect = "numbers greater than 10";
            }

            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void sleep() {
        try {
            Thread.sleep(8); //120fps 8.333... => 8
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        isNotPaused = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause() {
        try {
            isNotPaused = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
