package com.example.mathspace.fallingobj;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.fonts.Font;
import com.example.mathspace.visual.Point;
import com.example.mathspace.visual.Saw;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Square extends FallingObject {
    private int width;
    private int height;
    private Rect rect;
    private final Paint paint = new Paint();


    public Square(String text) {
        super(text, Shape.SQUARE);
        this.width = 150;
        this.height = 150;
    }


    @Override
    public void drawFallingObject(Canvas canvas) {
        //postavit boju na skup nekih
        paint.setColor(Color.RED);
        drawRectangle(canvas, 2);

        paint.setARGB(255, color[0], color[1], color[2]);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(51);
        Paint.FontMetrics fm = paint.getFontMetrics();

        int textWidth = 0;
        do {
            Rect bounds = new Rect();
            paint.getTextBounds(text, 0, text.length(), bounds);
            textWidth = bounds.width();
            paint.setTextSize(paint.getTextSize() - 1);

        } while (textWidth > width);

        drawRectangle(canvas, 0);

        //postavit boju na neku da izgleda dobro na svim pozadinama
        paint.setColor(Color.WHITE);
        canvas.drawText(this.text, (float) (this.centerX), this.centerY, paint); // treba staviti na sredinu text

    }

    private void drawRectangle(Canvas canvas, int extra) {
        rect = new Rect((int) (this.centerX - width / 2.0 - extra), (int) (this.centerY - height / 2.0 - extra),
                (int) (this.centerX + width / 2.0 + extra), (int) (this.centerY + height / 2.0 + extra));
        canvas.drawRect(rect, paint);
    }

    @Override
    public boolean checkCollision(Saw saw) {
        List<Point> points = new LinkedList<>();

        int sawCenterX = saw.getCenterX();
        int sawCenterY = saw.getCenterY();
        int sawRadius = saw.getRadius();
        int xyNE = (int) (sawRadius / 1.4142);

        points.add(new Point(sawCenterX - sawRadius, sawCenterY));  //west
        points.add(new Point(sawCenterX + sawRadius, sawCenterY));  //north
        points.add(new Point(sawCenterX, sawCenterY - sawRadius));    //east
        points.add(new Point(sawCenterX, sawCenterY + sawRadius));    //south
        points.add(new Point(sawCenterX + xyNE, sawCenterY + xyNE)); //north-east
        points.add(new Point(sawCenterX - xyNE, sawCenterY - xyNE)); //south-west
        points.add(new Point(sawCenterX + xyNE, sawCenterY - xyNE)); //south-east
        points.add(new Point(sawCenterX - xyNE, sawCenterY + xyNE)); //north-west

        for (Point point : points) {
            if (rect.contains(point.getX(), point.getY()))
                return true;
        }

        return false;
    }

    @Override
    public int getLowestPoint() {
        return this.getCenterY() + this.height / 2;
    }
}
