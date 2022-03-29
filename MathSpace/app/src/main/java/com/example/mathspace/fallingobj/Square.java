package com.example.mathspace.fallingobj;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import com.example.mathspace.visual.Point;
import com.example.mathspace.visual.Saw;

import java.util.LinkedList;
import java.util.List;

public class Square extends FallingObject {
    private int width;
    private int height;
    private Rect rect;



    public Square(String text) {
        super(text, Shape.SQUARE);
        this.width = (int) (150);
        this.height = (int) (150);
    }


    @Override
    public void drawFallingObject(Canvas canvas) {
        //postavit boju na skup nekih
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(50);
        drawRectangle(canvas, paint);

        //postavit boju na neku da izgleda dobro na svim pozadinama
        paint.setColor(Color.WHITE);
        canvas.drawText(this.getText(), (float) (this.getCenterX()), this.getCenterY(), paint); // treba staviti na sredinu text

    }

    private void drawRectangle(Canvas canvas, Paint paint) {
        rect = new Rect((int) (this.getCenterX() - width / 2.0), (int) (this.getCenterY() - height / 2.0),
                (int) (this.getCenterX() + width / 2.0), (int) (this.getCenterY() + height / 2.0));
        canvas.drawRect(rect, paint);
    }

    @Override
    public boolean checkCollision(Saw saw) {
        List<Point> points = saw.getPoints();
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
