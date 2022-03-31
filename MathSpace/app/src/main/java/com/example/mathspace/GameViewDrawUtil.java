package com.example.mathspace;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import com.example.mathspace.fallingobj.FallingObject;
import com.example.mathspace.task.Task;
import com.example.mathspace.visual.Background;
import com.example.mathspace.visual.Saw;

import java.util.List;

public class GameViewDrawUtil {

    /**
     * Method used for drawing falling object on given canvas
     * @param canvas canvas
     * @param fallingObjectList list of falling objects
     */
    static void drawFallingObjects(Canvas canvas, List<FallingObject> fallingObjectList) {
        for (int i = 0; i < fallingObjectList.size(); ++i) {     //treba običan for ili multithread safe lista
            fallingObjectList.get(i).drawFallingObject(canvas);
        }
    }

    /**
     * Method used for drawing background
     * @param canvas canvas
     * @param currentUpBackground upper background
     * @param currentDownBackGround down bacgkround
     * @param paint paint
     */
     static void drawBackground(Canvas canvas, Background currentUpBackground, Background currentDownBackGround, Paint paint) {
        canvas.drawBitmap(currentUpBackground.getBackground(), currentUpBackground.getX(), currentUpBackground.getY(), paint);
        canvas.drawBitmap(currentDownBackGround.getBackground(), currentDownBackGround.getX(), currentDownBackGround.getY(), paint);
    }

    /**
     * Method used for showing score on the screen
     * @param canvas canvas
     * @param score score value
     * @param paint paint

     */
    static void drawScore(Canvas canvas, int score, Paint paint) {
        paint.setTextSize(100);
        paint.setColor(Color.WHITE);
        canvas.drawText("Score: " + score / 10, 20, 100, paint);
    }

    /**
     * Method used for displaying task text
     * @param canvas canvas
     * @param tasks list of tasks
     * @param currentTaskIndex  index of current task
     * @param screenY position on Y axis
     * @param paint paint
     */
     static void drawTask(Canvas canvas, List<Task> tasks, int currentTaskIndex, int screenY,  Paint paint) {
        paint.setTextSize(50);
        //moći će biti više taskova, pa za svaki...
        canvas.drawText("Collect: " + tasks.get(currentTaskIndex).getTaskText(), 20, screenY + 100, paint);
    }

    /**
     * Method used for drawing saw on the screen
     * @param canvas canvas
     * @param saw saw
     * @param screenX saw position on x axis
     * @param paint paint
     */
     static void drawSaw(Canvas canvas,  Saw saw, int screenX, Paint paint) {
        //line on which saw can move
        paint.setColor(Color.RED);
        canvas.drawLine(0, saw.getY() + (float) saw.getSaw().getHeight() / 2 - 1, screenX, saw.getY() + (float) saw.getSaw().getHeight() / 2 - 1, paint);
        canvas.drawLine(0, saw.getY() + (float) saw.getSaw().getHeight() / 2 - 2, screenX, saw.getY() + (float) saw.getSaw().getHeight() / 2 - 2, paint);

        //saw object
        canvas.drawBitmap(saw.getSaw(), saw.getX() - (float) saw.getSaw().getWidth() / 2, saw.getY(), null);
    }

    /**
     * Method used for drawing number of lives on the screen in the form of hearts
     * @param canvas canvas
     * @param numberOfLives number of lives
     * @param heart heart bitmap
     * @param screenX position on X axis
     * @param paint paint
     */
     static void drawHearts(Canvas canvas, int numberOfLives, Bitmap heart, int screenX, Paint paint) {
        for (int i = 0; i < numberOfLives; ++i) {
            canvas.drawBitmap(heart, screenX - 100 - heart.getWidth() * i, 10, paint);
        }
    }

    static void drawNewTaskText(Canvas canvas, int screenX, int screenY, Task task, Paint taskPaint) {
        taskPaint.setColor(Color.WHITE);
        canvas.drawRect(150,(int)(screenY/2.0-50), screenX-150, (int)(screenY/2.0+50), taskPaint);
        taskPaint.setColor(Color.BLACK);
        canvas.drawText("Now collect : " + task.getTaskText(), (float)(screenX/2.0),(float)(screenY/2.0), taskPaint);
    }
}
