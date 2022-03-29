package com.example.mathspace.fallingobj;

import android.graphics.Canvas;
import com.example.mathspace.visual.Saw;

public abstract class FallingObject {
    protected String text;
    protected Shape shape;
    protected int centerX;
    protected int centerY;
    protected boolean destroyed;
    protected int speed;

    public abstract void drawFallingObject(Canvas canvas);

    public abstract boolean checkCollision(Saw saw);

    public abstract int getLowestPoint();

    public FallingObject(String text, Shape shape) {
        this.text = text;
        this.shape = shape;
        this.centerX = (int) (Math.random()*1080);
        this.centerY = -150;
        this.destroyed = false;
        speed = (int) (5+Math.random()*10);
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
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
