package com.example.mathspace;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceView;
import com.example.mathspace.fallingobj.Circle;
import com.example.mathspace.fallingobj.FallingObject;
import com.example.mathspace.fallingobj.Shape;
import com.example.mathspace.fallingobj.Square;
import com.example.mathspace.task.*;
import com.example.mathspace.visual.Background;
import com.example.mathspace.visual.Saw;
import com.google.android.material.bottomappbar.BottomAppBar;

import java.util.*;


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
    private List<FallingObject> fallingObjectList = new ArrayList<>();
    private List<Task> tasks = new ArrayList<>();
    private int generateFallingObjectFrequency = 10;
    private int currentTaskIndex = 0;


    @SuppressLint("ClickableViewAccessibility")
    public GameView(Context context) {
        super(context);

        //tasks test
        tasks.add(new NumberTask(" even numbers", TaskType.EVEN, 0));
        tasks.add(new NumberTask(" numbers greater than 10", TaskType.GREATER, 10));
        tasks.add(new ShapeTask(" squares", TaskType.SHAPE, Shape.SQUARE));
        List<String> trazeni = new ArrayList<>();
        trazeni.add("Visibaba");
        trazeni.add("Jaglac");
        List<String> krivi = new ArrayList<>();
        krivi.add("Patliđan");
        krivi.add("Paprika");
        tasks.add(new WordsTask(" proljetnice", TaskType.WORDCONTAINED, trazeni, krivi));
        //tasks test end


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
                saw.setX((int) event.getX());
            }

            return true;
        };
        this.setOnTouchListener(touchListener);
    }

    public void changeTask() {
        currentTaskIndex = (int) Math.floor(Math.random() * tasks.size());
    }

    @Override
    public void run() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                GameView.this.addNewFallingObjectIfNeeded();
            }
        }, 0, 2500);

        Timer timer2 = new Timer();
        timer2.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                GameView.this.changeTask();
            }
        }, 0, 30000);  //mijenja task svakih pol minute


        while (isNotPaused) {
            updateBackground();
            updateFallingObjects();
            draw();
            sleep();
        }
    }

    private void updateFallingObjects() {
        for (int i = 0; i < fallingObjectList.size(); ++i) {
            fallingObjectList.get(i).setCenterY(fallingObjectList.get(i).getCenterY() + fallingObjectList.get(i).getSpeed());

            boolean isCollected = false;
            //provjera da li je pokupljen
            if (fallingObjectList.get(i).getLowestPoint() >= saw.getY()) {   //provjeravaj kolizije samo za one koji se mogu...
                isCollected = fallingObjectList.get(i).checkCollision(saw);
                if (isCollected) {
                    boolean shouldHaveBeenCollected = tasks.get(currentTaskIndex).checkCollectedIsValid(fallingObjectList.get(i));
                    if (shouldHaveBeenCollected) {
                        score += 1000;
                    } else {
                        numberOfLives--;
                        //i logaj kao pogrešku negdje
                    }

                    fallingObjectList.remove(i--);
                    continue;
                }
            }

            //provjera je li pobjegao s ekrana
            if (fallingObjectList.get(i).getCenterY() - 150 > screenY) {
                boolean shouldHaveBeenCollected = tasks.get(currentTaskIndex).checkCollectedIsValid(fallingObjectList.get(i));
                if (shouldHaveBeenCollected)
                    score -= 1000;
                fallingObjectList.remove(i--);    //dodati u listu missed

            }
        }

    }

    private void addNewFallingObjectIfNeeded() {
        if (fallingObjectList.size() < 15) {
            if(tasks.get(currentTaskIndex) instanceof NumberTask || tasks.get(currentTaskIndex) instanceof ShapeTask) {
                switch ((int) Math.floor(Math.random() * 2)) {
                    case 0:
                        fallingObjectList.add(new Square(String.valueOf((int) Math.floor(Math.random() * 21))));
                        break;
                    case 1:
                        fallingObjectList.add(new Circle(String.valueOf((int) Math.floor(Math.random() * 21))));
                }
            }else if(tasks.get(currentTaskIndex) instanceof WordsTask){
                int size =  ((WordsTask) tasks.get(currentTaskIndex)).getCorrectWords().size();
                switch ((int) Math.floor(Math.random() * 2)) {
                    case 0:
                        fallingObjectList.add(
                                new Square(((WordsTask) tasks.get(currentTaskIndex)).getCorrectWords().get((int)Math.floor(Math.random()* size))));
                        break;
                    case 1:
                        fallingObjectList.add( new Circle(((WordsTask) tasks.get(currentTaskIndex)).getIncorrectWords().get((int)Math.floor(Math.random()* size))));
                }
            }
        }
    }


    private void updateBackground() {
        currentDownBackGround.setY(currentDownBackGround.getY() + 5);
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
            drawBackground(canvas);
            //score
            drawScore(canvas);
            //faling objects
            drawFallingObjects(canvas);
            //saw and moving line
            drawSaw(canvas);
            //hearts
            drawHearts(canvas);
            //what to collect
            drawTask(canvas);
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void drawFallingObjects(Canvas canvas) {
        for (int i = 0; i < fallingObjectList.size(); ++i) {     //treba običan for ili multithread safe lista
            fallingObjectList.get(i).drawFallingObject(canvas);
        }

    }


    private void drawBackground(Canvas canvas) {
        canvas.drawBitmap(currentUpBackground.getBackground(), currentUpBackground.getX(), currentUpBackground.getY(), paint);
        canvas.drawBitmap(currentDownBackGround.getBackground(), currentDownBackGround.getX(), currentDownBackGround.getY(), paint);
    }

    private void drawScore(Canvas canvas) {
        paint.setTextSize(100);
        paint.setColor(Color.WHITE);
        canvas.drawText("Score: " + score / 10, 20, 100, paint);
    }

    private void drawTask(Canvas canvas) {
        paint.setTextSize(50);
        //moći će biti više taskova, pa za svaki...
        canvas.drawText("Collect: " + tasks.get(currentTaskIndex).getTaskText(), 20, screenY + 100, paint);
    }

    private void drawSaw(Canvas canvas) {
        //line on which saw can move
        paint.setColor(Color.RED);
        canvas.drawLine(0, saw.getY() + (float) saw.getSaw().getHeight() / 2 - 1, screenX, saw.getY() + (float) saw.getSaw().getHeight() / 2 - 1, paint);
        canvas.drawLine(0, saw.getY() + (float) saw.getSaw().getHeight() / 2 - 2, screenX, saw.getY() + (float) saw.getSaw().getHeight() / 2 - 2, paint);

        //saw object
        canvas.drawBitmap(saw.getSaw(), saw.getX() - (float) saw.getSaw().getWidth() / 2, saw.getY(), null);
    }

    private void drawHearts(Canvas canvas) {
        for (int i = 0; i < numberOfLives; ++i) {
            canvas.drawBitmap(heart, screenX - 100 - heart.getWidth() * i, 10, paint);
        }
    }

    private void sleep() {
        try {
            Thread.sleep(1); //120fps 8.333... => 8
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
