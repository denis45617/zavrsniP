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
import android.widget.Toast;
import com.example.mathspace.fallingobj.FallingObject;
import com.example.mathspace.hs.HighScore;
import com.example.mathspace.task.*;
import com.example.mathspace.visual.Background;
import com.example.mathspace.visual.Saw;


import java.util.*;


@SuppressLint("ViewConstructor")
public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    private boolean isNotPaused;
    /**
     * Array containing all background photos
     */
    private final Background[] backgrounds;
    /**
     * Background down
     */
    private Background currentDownBackground;
    /**
     * Background up
     */
    private Background currentUpBackground;


    private int currentBackgroundIndex = 1;
    private final int screenX;
    private final int screenY;
    private final Paint paint = new Paint();
    private final Paint taskPaint = new Paint();
    private int score = 0;
    private int numberOfLives = 3;
    private final Bitmap heart;
    private final Saw saw;
    private final List<FallingObject> fallingObjectList = new ArrayList<>();
    private final List<Task> tasks;
    private int currentTaskIndex;
    private Timer fallingObjectTimer;
    private Timer changeTaskTimer;
    private int fallingObjectTimerPeriod = 1200;
    private boolean allowGeneratingFallingObjects = true;
    private Long showTaskTextUntil;
    private final Activity activity;
    private final List<Log> logs;
    private long sleepUntil = System.currentTimeMillis();


    @SuppressLint("ClickableViewAccessibility")
    public GameView(GameActivity activity) {
        super(activity);
        this.activity = activity;
        //get tasks

        tasks = GameViewInitUtil.getSelectedTasks(getContext().getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE));
        logs = new ArrayList<>();
        logs.add(new Log("Igra započeta"));
        currentTaskIndex = (int) Math.floor(Math.random() * tasks.size());
        logs.add(new Log("Zadatak: " + tasks.get(currentTaskIndex).getTaskText()));

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
        currentDownBackground = backgrounds[0];
        currentUpBackground = backgrounds[1];

        //get saw instance
        saw = GameViewInitUtil.getSaw(screenX, screenY, this.getResources(), R.drawable.saw1, R.drawable.saw2);

        //get heart scaled bitmap
        heart = GameViewInitUtil.getHeart(this.getResources(), this.screenX, this.screenY);

        this.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN) {
                saw.setX((int) event.getX());
            }
            return true;
        });
    }

    /**
     * Method used for changing tasks. uuuh samo problem ako thread.sleep, a mobitel...
     *
     * @throws InterruptedException cuz of thread.sleep
     */
    public void changeTask() throws InterruptedException {
        Task currentTask = tasks.get(currentTaskIndex);
        if (tasks.size() <= 1 && (
                (currentTask.getTaskType().equals(TaskType.EVEN))
                        || (currentTask.getTaskType().equals(TaskType.ODD))
                        || (currentTask.getTaskType().equals(TaskType.SHAPE))
                        || (currentTask.getTaskType().equals(TaskType.WORDCONTAINED))))
            return; //ako je samo jedan task nema se šta mijenjati! //OSIM kod onih taskova kojima treba izmijeniti relative NUMBER!


        if (currentTask instanceof ComplexTask) {
            ComplexTask complexTask = (ComplexTask) tasks.get(currentTaskIndex);
            List<Task> complexTaskTasks = complexTask.getTasks();
            for (Task task : complexTaskTasks) {
                if (task instanceof RelativeNumberTask) {
                    ((RelativeNumberTask) task).setRelativeNumber((int) (Math.random() * 20 + 1));
                }
            }
            ((ComplexTask) currentTask).changeTaskText();
        }

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

        //ako je prošli task relativenumbertask, promijeni mu relative number ako je tako zadano!
        if (tasks.get(prosliCurrentIndex) instanceof RelativeNumberTask &&
                ((RelativeNumberTask) tasks.get(prosliCurrentIndex)).isAllowRelativeNumberChange()) {
            RelativeNumberTask relativeNumberTask = (RelativeNumberTask) tasks.get(prosliCurrentIndex);
            relativeNumberTask.setRelativeNumber((int) (relativeNumberTask.getMinNumber() +
                    Math.random() * (relativeNumberTask.getMaxNumber() + 1 - relativeNumberTask.getMinNumber())));
        }

        showTaskTextUntil = System.currentTimeMillis() + 3000;

        logs.add(new Log("Changed task to " + tasks.get(currentTaskIndex).getTaskText()));
        allowGeneratingFallingObjects = true;    //dozvoli stvaranje novih objekata
    }

    @Override
    public void run() {
        while (isNotPaused) {
            updateBackground();
            updateFallingObjects();
            draw();
            score += 1;
            sleep();
            if (numberOfLives <= 0) {
                Looper.prepare();
                int highscore = score / 10;
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
                HighScore hs = new HighScore(sharedPreferences, highscore);
                hs.saveHighScore();   //spremi highscore; (ako to je highscore)
                gameOverScreen();    //pokaži game over screen

                //ako se koriste postavke s interneta, potrebno je spremiti rezultat na internet
                if (!sharedPreferences.getBoolean("USEDEFAULT", true)) { //odnosno ako nisu defaultne postavke
                    String gameCode = sharedPreferences.getString("SAVEDCODE", "");  //dohvati spremljeni kod
                    String nickname = sharedPreferences.getString("NICKNAME", "guest");  // i spremljeni nickname
                    Thread thread = new Thread(() -> {
                        Map<String, String> mapa = new HashMap<>();  //spremi sve potrebne podatke u mapu
                        mapa.put("highscore", String.valueOf(highscore));
                        mapa.put("player_nickname", nickname);
                        mapa.put("result", Log.toJson(logs));
                        mapa.put("game_code", gameCode);
                        Network.postMethod("result", mapa);  //napravi post sa mapom i njenim podacima
                    });

                    thread.start();
                    Toast.makeText(getContext(), "Post", Toast.LENGTH_LONG).show();
                }

                break;
            }
        }
    }


    private void levelUp() {
        int howToChange = (int) (Math.random() * 2.2); // 0 = frequency, 1 = max speed

        if (howToChange == 0 && (fallingObjectTimerPeriod > 400)) {
            if (score < 20000)
                fallingObjectTimerPeriod -= 30;  //svakih 1 ms češće!
            else
                fallingObjectTimerPeriod -= 15;  //svakih 1 ms češće!
            fallingObjectTimer.cancel();
            fallingObjectTimer = new Timer();
            fallingObjectTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    GameView.this.addNewFallingObjectIfNeeded();
                }
            }, fallingObjectTimerPeriod, fallingObjectTimerPeriod);
            return;
        }

        if (howToChange == 1 && FallingObject.getMinSpeed() < 22) {
            if (score < 20000)
                FallingObject.setMinSpeed(FallingObject.getMinSpeed() + 0.4);
            else
                FallingObject.setMinSpeed(FallingObject.getMinSpeed() + 0.2);
            return;
        }

        if (howToChange == 2) {
            FallingObject.setHowOftenComplexText(FallingObject.getHowOftenComplexText() + 0.1);
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private void gameOverScreen() {
        try {
            Thread.sleep(100);   //čekaj 0.1 sekundu
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);

            int highScore = sharedPreferences.getInt("HIGH_SCORE", 0);
            boolean useDefault = sharedPreferences.getBoolean("USEDEFAULT", true);
            if (!useDefault) {
                String gameCode = sharedPreferences.getString("SAVEDCODE", "");
                highScore = sharedPreferences.getInt("HIGH_SCORE" + gameCode, 0);
            }

            //pokaži taj end screen thing
            if (getHolder().getSurface().isValid()) {
                Canvas canvas = getHolder().lockCanvas();
                GameViewDrawUtil.drawBackground(canvas, currentUpBackground, currentDownBackground, paint);
                GameViewDrawUtil.drawScore(canvas, score, paint);
                GameViewDrawUtil.drawGameOverScreen(canvas, screenX, screenY, score, highScore, paint);
                getHolder().unlockCanvasAndPost(canvas);
            }

            Thread.sleep(250);

            OnTouchListener touchListener2 = (view, motionEvent) -> {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {   //menu button
                    if ((motionEvent.getX() > (0.15 * screenX)) && (motionEvent.getX() < (0.45 * screenX))
                            && motionEvent.getY() > (screenY - 0.3 * screenY) && motionEvent.getY() < (screenY - 0.2 * screenY)) {
                        activity.finish();
                    }

                    if ((motionEvent.getX() > (0.55 * screenX)) && (motionEvent.getX() < (0.85 * screenX)) //try again
                            && motionEvent.getY() > (screenY - 0.3 * screenY) && motionEvent.getY() < (screenY - 0.2 * screenY)) {
                        Intent intent = new Intent(activity.getApplicationContext(), GameActivity.class);
                        activity.startActivity(intent);
                        activity.finish();
                    }
                }
                return true;
            };

            this.setOnTouchListener(touchListener2);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void updateFallingObjects() {
        for (int i = 0; i < fallingObjectList.size(); ++i) {
            FallingObject fallingObject = fallingObjectList.get(i);
            fallingObject.setCenterY(fallingObject.getCenterY() + fallingObject.getSpeed());

            boolean isCollected;
            //provjera da li je pokupljen
            if (fallingObject.getLowestPoint() >= saw.getY()) {   //provjeravaj kolizije samo za one koji se mogu...
                isCollected = fallingObject.checkCollision(saw);
                if (isCollected) {
                    boolean shouldHaveBeenCollected = tasks.get(currentTaskIndex).checkCollectedIsValid(fallingObject);
                    if (shouldHaveBeenCollected) {
                        score += 1000;
                        logs.add(new Log("+100 Collected :" + fallingObject.getText()));
                        levelUp();
                    } else {
                        numberOfLives--;
                        logs.add(new Log("-Life !!!collected :" + fallingObject.getText() + " and lost life!!!"));
                        vibrate(300);
                        levelDownLife();
                    }

                    fallingObjectList.remove(i--);
                    continue;
                }
            }

            //provjera je li pobjegao s ekrana
            if (fallingObjectList.get(i).getCenterY() - 150 > screenY) {
                boolean shouldHaveBeenCollected = tasks.get(currentTaskIndex).checkCollectedIsValid(fallingObject);
                if (shouldHaveBeenCollected) {
                    score -= 1000;
                    vibrate(50);
                    logs.add(new Log("-100 Failed to collect: " + fallingObject.getText()));
                    levelDown();
                }
                fallingObjectList.remove(i--);
            }
        }

    }


    private void levelDownLife() {
        FallingObject.setMinSpeed(FallingObject.getMinSpeed() - 4);
        FallingObject.setHowOftenComplexText(FallingObject.getHowOftenComplexText() - 0.2);
        decreaseFrequency(200);
    }


    private void levelDown() {
        int whatToDo = (int) (Math.random() * 3);
        if (whatToDo == 0) {
            FallingObject.setMinSpeed(FallingObject.getMinSpeed() - 1);
            return;
        }
        if (whatToDo == 1) {
            FallingObject.setHowOftenComplexText(FallingObject.getHowOftenComplexText() - 0.025);
            return;
        }
        decreaseFrequency(50);
    }

    private void decreaseFrequency(int amount) {
        fallingObjectTimerPeriod += amount;
        fallingObjectTimerPeriod = Math.min(fallingObjectTimerPeriod, 1200);
        fallingObjectTimer.cancel();
        fallingObjectTimer = new Timer();
        fallingObjectTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                GameView.this.addNewFallingObjectIfNeeded();
            }
        }, fallingObjectTimerPeriod, fallingObjectTimerPeriod);
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

        if (fallingObjectList.size() < 15 && allowGeneratingFallingObjects) {
            Task task = tasks.get(currentTaskIndex);
            if (task instanceof ComplexTask) {
                List<Task> tasksFromComplexTask = ((ComplexTask) task).getTasks();
                task = tasksFromComplexTask.get((int) (Math.random() * tasksFromComplexTask.size()));
            }
            fallingObjectList.add(task.makeFallingObject());
        }

    }


    /**
     * Updates background
     */
    private void updateBackground() {
        currentDownBackground.setY(currentDownBackground.getY() + 5);
        currentUpBackground.setY(currentUpBackground.getY() + 5);

        if (currentDownBackground.getY() > screenY) {
            currentDownBackground = currentUpBackground;
            if (currentBackgroundIndex < backgrounds.length - 1)       //ako ima još različitih slika
                currentUpBackground = backgrounds[++currentBackgroundIndex];  //neka bude sljedeća
            else {
                currentDownBackground = backgrounds[backgrounds.length - 2];  //Inače vrti zadnju u krug
                currentDownBackground.setY(0);
                currentUpBackground = backgrounds[backgrounds.length - 1];
            }
            currentUpBackground.setY(-screenY);
        }
    }


    /**
     * Draw all content on the screen
     */
    private void draw() {
        boolean flag = true;
        while (flag) {
            if (getHolder().getSurface().isValid()) {
                Canvas canvas = getHolder().lockCanvas();
                //pozadina
                GameViewDrawUtil.drawBackground(canvas, currentUpBackground, currentDownBackground, paint);
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
                flag = false;
                getHolder().unlockCanvasAndPost(canvas);
            }
        }
    }


    /**
     * Ovo zapravo nije niti potrebno, surface view dozvoljava osvježvanje svakih 16ms, što je meni taman,
     * no ipak je... jer ako je češće zna usporiti procesor i previše!
     */
    private void sleep() {

        while (System.currentTimeMillis() < sleepUntil) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        sleepUntil = System.currentTimeMillis() + 16;
    }

    public void resume() {
        isNotPaused = true;
        thread = new Thread(this);
        thread.start();
        showTaskTextUntil = System.currentTimeMillis() + 3000;

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
