package com.example.mathspace.fallingobj;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import com.example.mathspace.visual.Saw;

public class Circle extends FallingObject {
    private int radius;

    public Circle(String text){
       super(text, Shape.CIRCLE);
       this.radius = (int) (80);
    }


    @Override
    public void drawFallingObject(Canvas canvas) {
        //postavit boju na skup nekih
        Paint paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(50);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(this.getCenterX(), this.getCenterY(), radius, paint);
        //postavit boju na neku da izgleda dobro na svim pozadinama
        paint.setColor(Color.BLACK);
        canvas.drawText(this.getText(),this.getCenterX(), this.getCenterY(),paint); // treba staviti na sredinu text
    }


    @Override
    void checkCollision(Saw saw) {

    }
}