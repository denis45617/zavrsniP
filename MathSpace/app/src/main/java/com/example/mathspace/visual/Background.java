package com.example.mathspace.visual;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Background {
    private int x = 0, y = 0;

    private Bitmap background;

    public Background(int screenX, int screenY, Resources res, int image) {
        background = BitmapFactory.decodeResource(res, image);
        background = Bitmap.createScaledBitmap(background, screenX, screenY, false);
    }

    public Bitmap getBackground() {
        return background;
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
