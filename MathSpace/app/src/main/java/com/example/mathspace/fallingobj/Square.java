package com.example.mathspace.fallingobj;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import com.example.mathspace.visual.Saw;

public class Square extends FallingObject {
    private int width;
    private int height;


    public Square(String text){
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
        canvas.drawRect((float) (this.getCenterX() - width / 2.0), (float) (this.getCenterY() - height / 2.0),
                (float) (this.getCenterX() + width / 2.0), (float) (this.getCenterY() + height / 2.0), paint);
    }

    @Override
    void checkCollision(Saw saw) {

    }
}
