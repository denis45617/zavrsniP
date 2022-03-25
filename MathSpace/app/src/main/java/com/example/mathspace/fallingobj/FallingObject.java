package com.example.mathspace.fallingobj;

import android.graphics.Canvas;

public abstract class FallingObject {
    private String text;
    private Shape shape;
    private int centerX;
    private int centerY;
    private ICollisionChecker collisionChecker;
    private boolean destroyed;

    abstract void drawFallingObject(Canvas canvas);


}
