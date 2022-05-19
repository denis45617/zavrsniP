package com.example.mathspace.fallingobj;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import com.example.mathspace.visual.Saw;

public class Circle extends FallingObject {
    private final int radius;
    private static final Paint paint = new Paint();

    public Circle(String text) {
        super(text, Shape.CIRCLE);
        this.radius = 80;
    }


    @Override
    public void drawFallingObject(Canvas canvas) {
        //postavit boju na skup nekih
        paint.setColor(Color.RED);
        canvas.drawCircle(this.centerX, this.centerY, radius + 3, paint);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(50);
        paint.setARGB(255, color[0], color[1], color[2]);
        canvas.drawCircle(this.centerX, this.centerY, radius, paint);
        //postavit boju na neku da izgleda dobro na svim pozadinama
        paint.setColor(Color.WHITE);


        int textWidth = 0;
        do {
            Rect bounds = new Rect();
            paint.getTextBounds(text, 0, text.length(), bounds);
            textWidth = bounds.width();
            paint.setTextSize(paint.getTextSize() - 1);

        } while (textWidth > 2 * radius);

        canvas.drawText(this.text, this.centerX, this.centerY, paint); // treba staviti na sredinu text

    }


    @Override
    public boolean checkCollision(Saw saw) {  //https://www.geeksforgeeks.org/check-two-given-circles-touch-intersect/
        int sawCenterX = saw.getCenterX();
        int sawCenterY = saw.getCenterY();
        int sawRadius = saw.getRadius();

        int distSq = (int) (Math.pow(sawCenterX - this.getCenterX(), 2) +
                Math.pow(sawCenterY - this.getCenterY(), 2));
        int radSumSq = (sawRadius + this.radius) * (sawRadius + this.radius);
        if (distSq == radSumSq)
            return true;
        return distSq <= radSumSq;

    }

    @Override
    public int getLowestPoint() {
        return this.getCenterY() + this.radius;
    }
}
