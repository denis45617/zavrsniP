package com.example.mathspace;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Saw {
    int x = 0;
    int y = 0;

    Bitmap saw1;
    Bitmap saw2;
    Bitmap currentSaw;
    int counter = 0;


    Saw(int screenX, int screenY, Resources res, int image1, int image2) {
        saw1 = BitmapFactory.decodeResource(res, image1);
        saw1 = Bitmap.createScaledBitmap(saw1, 256, 256, false);

        saw2 = BitmapFactory.decodeResource(res, image2);
        saw2 = Bitmap.createScaledBitmap(saw2, 256, 256, false);

        x = screenX / 2;
        y = screenY - 256;
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

}
