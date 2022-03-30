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
import com.example.mathspace.fallingobj.Square;
import com.example.mathspace.task.*;
import com.example.mathspace.visual.Background;
import com.example.mathspace.visual.Saw;

import java.util.*;


public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    private boolean isNotPaused;
    /**
     * Array containing all background photos
     */
    private Background[] backgrounds;
    /**
     * Background down
     */
    private Background currentDownBackGround;
    /**
     * Background up
     */
    private Background currentUpBackground;

    private int currentBackgroundIndex = 1;
    private int screenX, screenY;
    private Paint paint = new Paint();
    private int score = 0;
    private int numberOfLives = 3;
    private Bitmap heart;
    private Saw saw;
    private List<FallingObject> fallingObjectList = new ArrayList<>();
    private List<Task> tasks;
    private int generateFallingObjectFrequency = 10;
    private int currentTaskIndex = 0;
    private Timer fallingObjectTimer;
    private int fallingObjectTimerPeriod = 1500;


    @SuppressLint("ClickableViewAccessibility")
    public GameView(Context context) {
        super(context);
        //get tasks
        tasks = GameViewInitUtil.getTasks();

        //get display details
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        this.screenX = size.x;
        this.screenY = size.y;


        // get background pictures
        this.backgrounds = GameViewInitUtil.getBackgrounds(this.screenX, this.screenY, this.getResources());
        getResources();


        currentDownBackGround = backgrounds[0];
        currentUpBackground = backgrounds[1];

        //get saw instance
        saw = GameViewInitUtil.getSaw(screenX, screenY, this.getResources(), R.drawable.saw1, R.drawable.saw2);

        //get heart scaled bitmap
        heart = GameViewInitUtil.getHeart(this.getResources(), this.screenX, this.screenY);


        OnTouchListener touchListener = (v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                saw.setX((int) event.getX());
            }

            return true;
        };
        this.setOnTouchListener(touchListener);
    }


    public void changeTask() {
        int prosliCurrentIndex = currentTaskIndex;
        do {
            currentTaskIndex = (int) Math.floor(Math.random() * tasks.size());
        } while (prosliCurrentIndex == currentTaskIndex);
    }

    @Override
    public void run() {
        while (isNotPaused) {
            if (numberOfLives <= 0) pause();  // pause + animacija + novi screen vjerojatno ili tako nešta

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
        try {
            if (fallingObjectList.size() < 15) {
                if (tasks.get(currentTaskIndex) instanceof NumberTask || tasks.get(currentTaskIndex) instanceof ShapeTask) {
                    switch ((int) Math.floor(Math.random() * 2)) {
                        case 0:
                            fallingObjectList.add(new Square(String.valueOf((int) Math.floor(Math.random() * 21))));
                            break;
                        case 1:
                            fallingObjectList.add(new Circle(String.valueOf((int) Math.floor(Math.random() * 21))));
                    }
                } else if (tasks.get(currentTaskIndex) instanceof WordsTask) {
                    int size = ((WordsTask) tasks.get(currentTaskIndex)).getCorrectWords().size();
                    switch ((int) Math.floor(Math.random() * 4)) {
                        case 0:
                            fallingObjectList.add(
                                    new Square(((((WordsTask) tasks.get(currentTaskIndex)).getRandomCorrectWord()))));
                            break;
                        case 1:
                            fallingObjectList.add(new Circle(((((WordsTask) tasks.get(currentTaskIndex)).getRandomCorrectWord()))));
                            break;
                        case 2:
                            fallingObjectList.add(new Circle(((((WordsTask) tasks.get(currentTaskIndex)).getRandomIncorrectWord()))));
                            break;
                        case 3:
                            fallingObjectList.add(new Square(((((WordsTask) tasks.get(currentTaskIndex)).getRandomIncorrectWord()))));
                            break;
                    }
                }
            }
        }catch(Exception e){
            //do nothing ... dogodi se expcetion kad se novi stvara dok se istovremeno promijeni task
        }
    }


    private void updateBackground() {
        currentDownBackGround.setY(currentDownBackGround.getY() + 5);
        currentUpBackground.setY(currentUpBackground.getY() + 5);
        score += 1;

        if (currentDownBackGround.getY() > screenY) {
            currentDownBackGround = currentUpBackground;
            if (currentBackgroundIndex < backgrounds.length - 1)       //ako ima još različitih slika
                currentUpBackground = backgrounds[++currentBackgroundIndex];  //neka bude sljedeća
            else {
                currentDownBackGround = backgrounds[backgrounds.length - 2];  //Inače vrti zadnju u krug
                currentDownBackGround.setY(0);
                currentUpBackground = backgrounds[backgrounds.length - 1];
            }
            currentUpBackground.setY(-screenY);
        }
    }


    private void draw() {
        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            //pozadina
            GameViewDrawUtil.drawBackground(canvas, currentUpBackground, currentDownBackGround, paint);
            //score
            GameViewDrawUtil.drawScore(canvas, score, paint);
            //objekti
            GameViewDrawUtil.drawFallingObjects(canvas, fallingObjectList);
            //pila i linija
            GameViewDrawUtil.drawSaw(canvas, saw, screenX, paint);
            //hearts
            GameViewDrawUtil.drawHearts(canvas, numberOfLives, heart, screenX, paint);
            //what to collect
            GameViewDrawUtil.drawTask(canvas, tasks, currentTaskIndex, screenY, paint);
            getHolder().unlockCanvasAndPost(canvas);
        }
    }


    private void sleep() {
        try {
            Thread.sleep(1); //120fps 8.333... => 8 //igra se presporo obavlja već svejedno :kekl: pfffff
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        isNotPaused = true;
        thread = new Thread(this);
        thread.start();

        //dodaj falling object svakih neki period
        fallingObjectTimer = new Timer();                  //hmm jel ovo u novoj dretvi?
        fallingObjectTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                GameView.this.addNewFallingObjectIfNeeded();
            }
        }, fallingObjectTimerPeriod, fallingObjectTimerPeriod);


        //mijenja task svakih pol minute počevši nakon pola minute       //hmm jel ovo u novoj dretvi?
        Timer timer2 = new Timer();
        timer2.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                GameView.this.changeTask();
            }
        }, 30000, 30000);
    }

    public void pause() {
        try {
            fallingObjectTimer.cancel();

            isNotPaused = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
