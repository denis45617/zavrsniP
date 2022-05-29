package com.example.mathspace.visual;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.LinkedList;
import java.util.List;

public class Saw {
    private int x;
    private int y;

    private Bitmap saw1;
    private Bitmap saw2;
    private Bitmap currentSaw;
    private int counter = 0;
    private int WIDTH_HEIGHT = 256;


    public Saw(int screenX, int screenY, Resources res, int image1, int image2) {
        saw1 = BitmapFactory.decodeResource(res, image1);
        saw1 = Bitmap.createScaledBitmap(saw1, WIDTH_HEIGHT, WIDTH_HEIGHT, false);

        saw2 = BitmapFactory.decodeResource(res, image2);
        saw2 = Bitmap.createScaledBitmap(saw2, WIDTH_HEIGHT, WIDTH_HEIGHT, false);

        x = screenX / 2;
        y = screenY - WIDTH_HEIGHT;
        currentSaw = saw1;
    }


    public Bitmap getSaw() {
        if (counter % 20 == 0) {
            if (currentSaw == saw1) {
                currentSaw = saw2;
            } else {
                currentSaw = saw1;
            }
            counter = 0;
        }
        counter++;
        return currentSaw;
    }

    public int getCenterX() {
        return this.getX();// + this.WIDTH_HEIGHT / 2;
    }

    public int getCenterY() {
        return this.getY() + this.WIDTH_HEIGHT / 2;
    }

    public int getRadius() {
        return (int) (this.WIDTH_HEIGHT / 2 - this.WIDTH_HEIGHT * 0.1); //-10% da ne reagira baš na najmanji detalj
    }

    public int getWIDTH_HEIGHT() {
        return WIDTH_HEIGHT;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

}
