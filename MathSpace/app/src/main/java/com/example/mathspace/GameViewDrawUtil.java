package com.example.mathspace;

import android.graphics.*;
import com.example.mathspace.fallingobj.FallingObject;
import com.example.mathspace.task.ComplexTask;
import com.example.mathspace.task.Task;
import com.example.mathspace.visual.Background;
import com.example.mathspace.visual.Saw;

import java.util.List;

/**
 * Util class for drawing objects on given canvases
 */
public class GameViewDrawUtil {

    /**
     * Method used for drawing falling object on given canvas
     *
     * @param canvas            canvas
     * @param fallingObjectList list of falling objects
     */
    static void drawFallingObjects(Canvas canvas, List<FallingObject> fallingObjectList) {
        for (int i = 0; i < fallingObjectList.size(); ++i) {     //treba običan for ili multithread safe lista
            fallingObjectList.get(i).drawFallingObject(canvas);
        }
    }

    /**
     * Method used for drawing background
     *
     * @param canvas                canvas
     * @param currentUpBackground   upper background
     * @param currentDownBackGround down bacgkround
     * @param paint                 paint
     */
    static void drawBackground(Canvas canvas, Background currentUpBackground, Background currentDownBackGround, Paint paint) {
        canvas.drawBitmap(currentUpBackground.getBackground(), currentUpBackground.getX(), currentUpBackground.getY(), paint);
        canvas.drawBitmap(currentDownBackGround.getBackground(), currentDownBackGround.getX(), currentDownBackGround.getY(), paint);
    }

    /**
     * Method used for showing score on the screen
     *
     * @param canvas canvas
     * @param score  score value
     * @param paint  paint
     */
    static void drawScore(Canvas canvas, int score, Paint paint) {
        paint.setTextSize(100);
        paint.setColor(Color.WHITE);
        canvas.drawText("Score: " + score / 10, 20, 100, paint);
    }

    /**
     * Method used for displaying task text
     *
     * @param canvas           canvas
     * @param tasks            list of tasks
     * @param currentTaskIndex index of current task
     * @param screenY          position on Y axis
     * @param paint            paint
     */
    static void drawTask(Canvas canvas, List<Task> tasks, int currentTaskIndex, int screenY, Paint paint) {
        if (tasks.get(currentTaskIndex) instanceof ComplexTask) {
            paint.setTextSize(25);
        } else {
            paint.setTextSize(50);
        }
        //moći će biti više taskova, pa za svaki...
        canvas.drawText("Collect: " + tasks.get(currentTaskIndex).getTaskText(), 20, screenY + 100, paint);
    }

    /**
     * Method used for drawing saw on the screen
     *
     * @param canvas  canvas
     * @param saw     saw
     * @param screenX saw position on x axis
     * @param paint   paint
     */
    static void drawSaw(Canvas canvas, Saw saw, int screenX, Paint paint) {
        //line on which saw can move
        paint.setColor(Color.RED);
        canvas.drawLine(0, saw.getY() + (float) saw.getSaw().getHeight() / 2 - 1, screenX, saw.getY() + (float) saw.getSaw().getHeight() / 2 - 1, paint);
        canvas.drawLine(0, saw.getY() + (float) saw.getSaw().getHeight() / 2 - 2, screenX, saw.getY() + (float) saw.getSaw().getHeight() / 2 - 2, paint);

        //saw object
        canvas.drawBitmap(saw.getSaw(), saw.getX() - (float) saw.getSaw().getWidth() / 2, saw.getY(), null);
    }

    /**
     * Method used for drawing number of lives on the screen in the form of hearts
     *
     * @param canvas        canvas
     * @param numberOfLives number of lives
     * @param heart         heart bitmap
     * @param screenX       position on X axis
     * @param paint         paint
     */
    static void drawHearts(Canvas canvas, int numberOfLives, Bitmap heart, int screenX, Paint paint) {
        for (int i = 0; i < numberOfLives; ++i) {
            canvas.drawBitmap(heart, screenX - 100 - heart.getWidth() * i, 10, paint);
        }
    }

    static void drawNewTaskText(Canvas canvas, int screenX, int screenY, Task task, Paint taskPaint) {
        taskPaint.setColor(Color.WHITE);
        canvas.drawRect(150, (int) (screenY / 2.0 - 50), screenX - 150, (int) (screenY / 2.0 + 50), taskPaint);
        taskPaint.setColor(Color.BLACK);
        canvas.drawText("Now collect : " + task.getTaskText(), (float) (screenX / 2.0), (float) (screenY / 2.0), taskPaint);
    }


    public static void drawGameOverScreen(Canvas canvas, int screenX, int screenY, int score, int highScore, Paint paint) {
        //===============================================POZADINA=======================================================
        //crveni obrbu
        paint.setColor(Color.RED);
        canvas.drawRoundRect((float) 0.1 * screenX - 4, (float) 0.15 * screenY - 4,
                (float) (screenX - 0.1 * screenX + 4), (float) (screenY - 0.10 * screenY + 4),
                100, 100, paint);
        //pozadina - pozadina
        paint.setColor(Color.BLACK);
        canvas.drawRoundRect((float) 0.1 * screenX, (float) 0.15 * screenY,
                (float) (screenX - 0.1 * screenX), (float) (screenY - 0.10 * screenY),
                100, 100, paint);

        paint.setTextAlign(Paint.Align.CENTER);

        //===============================================G4M3 0V3R======================================================
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setTextSize(120);
        paint.setColor(Color.WHITE);
        canvas.drawText("G4M3 0V3R", (float) (screenX / 2.0), (float) (0.26 * screenY), paint);

        paint.setTextSize(80);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        //===============================================HIGHSCORE======================================================
        paint.setColor(Color.WHITE);
        canvas.drawText("HIGH SCORE: ", (float) (screenX / 2.0), (float) (0.37 * screenY), paint);
        canvas.drawText(String.valueOf(highScore), (float) (screenX / 2.0), (float) (0.42 * screenY), paint);

        paint.setTextSize(100);
        //===================================================SCORE======================================================
        paint.setColor(Color.WHITE);
        canvas.drawText("SCORE: ", (float) (screenX / 2.0), (float) (0.52 * screenY), paint);
        canvas.drawText(String.valueOf(score / 10), (float) (screenX / 2.0), (float) (0.57 * screenY), paint);


        paint.setTextSize(75);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        //==============================================MAIN-MENU=======================================================
        //pozadina od buttona
        paint.setColor(Color.WHITE);
        canvas.drawRoundRect((float) 0.15 * screenX, (float) (screenY - 0.3 * screenY),
                (float) (0.45 * screenX), (float) (screenY - 0.2 * screenY), 20, 20, paint);
        //text na battonu za main menu
        paint.setColor(Color.RED);
        canvas.drawText("MENU", (float) 0.3 * screenX, (float) (screenY - 0.25 * screenY - fontMetrics.ascent / 4), paint);

        //==============================================TRY-AGAIN=======================================================

        paint.setColor(Color.WHITE);
        canvas.drawRoundRect((float) 0.55 * screenX, (float) (screenY - 0.3 * screenY),
                (float) (0.85 * screenX), (float) (screenY - 0.2 * screenY), 20, 20, paint);
        //text na buttonu za main menu
        paint.setColor(Color.RED);
        canvas.drawText("PLAY", (float) 0.7 * screenX,
                (float) (screenY - 0.25 * screenY + fontMetrics.ascent / 2 - fontMetrics.ascent / 4), paint);
        canvas.drawText("AGAIN", (float) 0.7 * screenX,
                (float) (screenY - 0.25 * screenY - fontMetrics.ascent / 2 - fontMetrics.ascent / 4), paint);
        //==============================================TRY-AGAIN=======================================================

    }
}
