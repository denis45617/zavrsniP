package com.example.mathspace;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.*;
import android.os.Build;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;
import com.example.mathspace.fallingobj.Circle;
import com.example.mathspace.fallingobj.FallingObject;
import com.example.mathspace.fallingobj.Square;
import com.example.mathspace.hs.HighScore;
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
    private Paint taskPaint = new Paint();
    private int score = 0;
    private int numberOfLives = 3;
    private Bitmap heart;
    private Saw saw;
    private List<FallingObject> fallingObjectList = new ArrayList<>();
    private List<Task> tasks;
    private int generateFallingObjectFrequency = 10;
    private int currentTaskIndex;
    private Timer fallingObjectTimer;
    private Timer changeTaskTimer;
    private int fallingObjectTimerPeriod = 1500;
    private boolean allowGeneratingFallingObjects = true;
    private Long showTaskTextUntil;
    private Activity activity;


    @SuppressLint("ClickableViewAccessibility")
    public GameView(GameActivity activity) {
        super(activity);
        this.activity = activity;
        //get tasks
        tasks = GameViewInitUtil.getSelectedTasks(getContext().getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE));

        currentTaskIndex = (int) Math.floor(Math.random() * tasks.size());

        taskPaint.setTextAlign(Paint.Align.CENTER);
        taskPaint.setTextSize(30);

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


    /**
     * Method used for changing tasks. uuuh samo problem ako thread.sleep, a mobitel...
     *
     * @throws InterruptedException cuz of thread.sleep
     */
    public void changeTask() throws InterruptedException {

        if (tasks.size() <= 1 && (tasks.get(currentTaskIndex).getTaskType().equals(TaskType.EVEN))
                || (tasks.get(currentTaskIndex).getTaskType().equals(TaskType.ODD))
                || (tasks.get(currentTaskIndex).getTaskType().equals(TaskType.SHAPE))
                || (tasks.get(currentTaskIndex).getTaskType().equals(TaskType.WORDCONTAINED)))
            return; //ako je samo jedan task nema se šta mijenjati! //OSIM kod onih taskova kojima treba izmijeniti relative NUMBER!

        allowGeneratingFallingObjects = false;   //zabrani stvaranje novih objekata


        while (fallingObjectList.size() > 0) {   //čekaj BAREM 4 sekund od kako nema niti jednog
            Thread.sleep(4000);
        }

        //dohvati novi task :)
        int prosliCurrentIndex = currentTaskIndex;
        do {
            if (tasks.size() == 1) break;
            currentTaskIndex = (int) Math.floor(Math.random() * tasks.size());
        } while (prosliCurrentIndex == currentTaskIndex);

        if (tasks.get(prosliCurrentIndex) instanceof NumberTask) {  //ako je prošli task matematički, promijeni mu relative number
            ((NumberTask) tasks.get(prosliCurrentIndex)).setRelativeNumber((int) (Math.random() * 20 + 1));
        }


        showTaskTextUntil = System.currentTimeMillis() + 4000;
        //sad ovdje možda ispisati na ekranu NEW TASK JE::::..


        allowGeneratingFallingObjects = true;    //dozvoli stvaranje novih objekata
    }

    @Override
    public void run() {
        while (isNotPaused) {
            updateBackground();
            updateFallingObjects();
            draw();
            sleep();
            if (numberOfLives <= 0) { // pause + animacija + novi screen vjerojatno ili tako nešta
                Looper.prepare();
                HighScore hs = new HighScore(getContext().getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE), score / 10);
                hs.saveHighScore();
                gameOverScreen();
                break;
            }
        }
    }

    private void gameOverScreen() {
        try {
            Thread.sleep(100);   //čekaj 0.1 sekundu

            SharedPreferences sharedPreferences = getContext().getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
            int highScore = sharedPreferences.getInt("HIGH_SCORE", 0);
            //pokaži taj end screen thing
            if (getHolder().getSurface().isValid()) {
                Canvas canvas = getHolder().lockCanvas();
                GameViewDrawUtil.drawBackground(canvas, currentUpBackground, currentDownBackGround, paint);
                GameViewDrawUtil.drawScore(canvas, score, paint);
                GameViewDrawUtil.drawGameOverScreen(canvas, screenX, screenY, score, highScore, paint);
                getHolder().unlockCanvasAndPost(canvas);
            }

            Thread.sleep(250);

            OnTouchListener touchListener2 = new OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        if ((motionEvent.getX() > (0.15 * screenX)) && (motionEvent.getX() < (0.45 * screenX))
                                && motionEvent.getY() > (screenY - 0.3 * screenY) && motionEvent.getY() < (screenY - 0.2 * screenY)) {
                            activity.finish();
                        }

                        if ((motionEvent.getX() > (0.55 * screenX)) && (motionEvent.getX() < (0.85 * screenX))
                                && motionEvent.getY() > (screenY - 0.3 * screenY) && motionEvent.getY() < (screenY - 0.2 * screenY)) {
                            Intent intent = new Intent(activity.getApplicationContext(), GameActivity.class);
                            activity.startActivity(intent);
                            activity.finish();
                        }
                    }

                    return true;
                }
            };

            this.setOnTouchListener(touchListener2);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void updateFallingObjects() {
        for (int i = 0; i < fallingObjectList.size(); ++i) {
            fallingObjectList.get(i).setCenterY(fallingObjectList.get(i).getCenterY() + fallingObjectList.get(i).getSpeed());

            boolean isCollected;
            //provjera da li je pokupljen
            if (fallingObjectList.get(i).getLowestPoint() >= saw.getY()) {   //provjeravaj kolizije samo za one koji se mogu...
                isCollected = fallingObjectList.get(i).checkCollision(saw);
                if (isCollected) {
                    boolean shouldHaveBeenCollected = tasks.get(currentTaskIndex).checkCollectedIsValid(fallingObjectList.get(i));
                    if (shouldHaveBeenCollected) {
                        score += 1000;
                    } else {
                        numberOfLives--;
                        vibrate(300);
                        //i logaj kao pogrešku negdje
                    }

                    fallingObjectList.remove(i--);
                    continue;
                }
            }

            //provjera je li pobjegao s ekrana
            if (fallingObjectList.get(i).getCenterY() - 150 > screenY) {
                boolean shouldHaveBeenCollected = tasks.get(currentTaskIndex).checkCollectedIsValid(fallingObjectList.get(i));
                if (shouldHaveBeenCollected) {
                    score -= 1000;
                    vibrate(50);
                }
                fallingObjectList.remove(i--);    //dodati u listu missed

            }
        }

    }


    private void vibrate(int miliseconds) {
        Vibrator v = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(miliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(miliseconds);
        }
    }

    /**
     * Adds new falling object to list of falling objects
     */
    private void addNewFallingObjectIfNeeded() {
        try {
            if (fallingObjectList.size() < 15 && allowGeneratingFallingObjects) {
                if (tasks.get(currentTaskIndex) instanceof NumberTask || tasks.get(currentTaskIndex) instanceof ShapeTask) {
                    switch ((int) Math.floor(Math.random() * 2)) {
                        case 0:
                            fallingObjectList.add(new Square(String.valueOf((int) Math.floor(Math.random() * 21))));
                            break;
                        case 1:
                            fallingObjectList.add(new Circle(String.valueOf((int) Math.floor(Math.random() * 21))));
                    }
                } else if (tasks.get(currentTaskIndex) instanceof WordsTask) {
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
        } catch (Exception e) {
            //do nothing ... dogodi se expcetion kad se novi stvara dok se istovremeno promijeni task
        }
    }


    /**
     * Updates background
     */
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


    /**
     * Draw all content on the screen
     */
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

            if (showTaskTextUntil != null && System.currentTimeMillis() < showTaskTextUntil) {
                GameViewDrawUtil.drawNewTaskText(canvas, screenX, screenY, tasks.get(currentTaskIndex), taskPaint);
            } else {
                showTaskTextUntil = null;
            }

            getHolder().unlockCanvasAndPost(canvas);
        }
    }


    /**
     * Eh joj ovo će biti problemi (prebrzo na dobrim mobitelima, presporo na lošim); AAAAAAAAAA
     */
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
        fallingObjectTimer = new Timer();
        fallingObjectTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                GameView.this.addNewFallingObjectIfNeeded();
            }
        }, fallingObjectTimerPeriod, fallingObjectTimerPeriod);


        //mijenja task svakih pol minute počevši nakon pola minute
        changeTaskTimer = new Timer();                             //nije najbolje mjesto za inicijalizaciju jer
        changeTaskTimer.scheduleAtFixedRate(new TimerTask() {      //bi se pause mogao abuse da stalno bude isti task
            @Override
            public void run() {
                try {
                    GameView.this.changeTask();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 30000, 30000);
    }

    public void pause() {
        fallingObjectTimer.cancel();
        changeTaskTimer.cancel();

        isNotPaused = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
