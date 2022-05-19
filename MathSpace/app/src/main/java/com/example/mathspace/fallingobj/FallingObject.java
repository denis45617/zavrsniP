package com.example.mathspace.fallingobj;

import android.graphics.Canvas;
import com.example.mathspace.visual.Saw;

public abstract class FallingObject {
    protected String text;
    protected int value;
    protected Shape shape;
    protected int centerX;
    protected int centerY;
    protected boolean destroyed;
    protected int speed;
    protected int[] color = new int[3];
    protected static double howOftenComplexText;
    private static double minSpeed = 5;

    public abstract void drawFallingObject(Canvas canvas);

    public abstract boolean checkCollision(Saw saw);

    public abstract int getLowestPoint();

    public FallingObject(String text, Shape shape) {
        this.text = text;
        this.shape = shape;
        this.centerX = (int) (Math.random() * 1080);
        this.centerY = -150;
        this.destroyed = false;
        this.speed = (int) (minSpeed + Math.random() * 10);
        this.color[0] = (int) (Math.random() * 220);
        this.color[1] = (int) (Math.random() * 220);
        this.color[2] = (int) (Math.random() * 220);
    }


    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static void setHowOftenComplexText(double howOftenComplexText) {
        FallingObject.howOftenComplexText = Math.max(howOftenComplexText, 0);
    }

    public static double getHowOftenComplexText() {
        return howOftenComplexText;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public static void setMinSpeed(double minSpeed) {
        FallingObject.minSpeed = Math.max(minSpeed, 5);
    }

    public static double getMinSpeed() {
        return minSpeed;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public int getCenterX() {
        return centerX;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }
}
