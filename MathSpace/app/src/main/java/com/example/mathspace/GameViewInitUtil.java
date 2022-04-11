package com.example.mathspace;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.example.mathspace.fallingobj.Shape;
import com.example.mathspace.task.*;
import com.example.mathspace.visual.Background;
import com.example.mathspace.visual.Saw;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameViewInitUtil {
    /**
     * Method for getting all tasks
     *
     * @return List of tasks
     */
    public static List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();

        //========================================== default tasks =====================================================
        //even

        tasks.add(new NumberTask(" even numbers", TaskType.EVEN, null));
        //odd
        tasks.add(new NumberTask(" odd numbers", TaskType.ODD, null));

        int relativeNumber = (int) (1 + Math.floor(Math.random() * 1000));
        //greater
        relativeNumber = (int) (1 + Math.floor(Math.random() * 20));
        tasks.add(new NumberTask(" numbers greater than ", TaskType.GREATER, relativeNumber));

        //greaterequal
        relativeNumber = (int) (1 + Math.floor(Math.random() * 20));
        tasks.add(new NumberTask(" numbers greater or equal than ", TaskType.GREATEREQUAL, relativeNumber));

        //lower
        relativeNumber = (int) (5 + Math.floor(Math.random() * 20));
        tasks.add(new NumberTask(" numbers lower than ", TaskType.LOWER, relativeNumber));

        //lowerequal
        relativeNumber = (int) (5 + Math.floor(Math.random() * 20));
        tasks.add(new NumberTask(" numbers lower or equal than ", TaskType.LOWEREQUAL, relativeNumber));

        //shape square
        tasks.add(new ShapeTask(" squares", TaskType.SHAPE, Shape.SQUARE));

        //shape circle
        tasks.add(new ShapeTask(" circles", TaskType.SHAPE, Shape.CIRCLE));

        //wordcontained
        Set<String> geomLikovi = new HashSet<>();
        geomLikovi.add("Krug");
        geomLikovi.add("Kvadrat");
        geomLikovi.add("Pravokutnik");
        geomLikovi.add("Trokut");

        Set<String> geomTijela = new HashSet<>();
        geomTijela.add("Piramida");
        geomTijela.add("Valjak");
        geomTijela.add("Stožac");
        geomTijela.add("Kugla");
        tasks.add(new WordsTask(" geometrijske likove", TaskType.WORDCONTAINED, geomLikovi, geomTijela));
        tasks.add(new WordsTask(" geometrijska tijela", TaskType.WORDCONTAINED, geomTijela, geomLikovi));

        return tasks;
    }

    /**
     * Method that returns only default tasks that are enabled in settings
     * @param sharedPreferences shared preferences
     * @return List of tasks
     */
    public static List<Task> getSelectedTasks(SharedPreferences sharedPreferences) {
        List<Task> selectedTasks = new ArrayList<>();
        List<Task> allTasks = getAllTasks();

        for (int i = 0; i < allTasks.size(); ++i) {
            if (sharedPreferences.getBoolean("DEFAULT_SETTING:" + i, true)) {
                selectedTasks.add(allTasks.get(i));
            }
        }

        return selectedTasks;
    }


    /**
     * Method for making Array of background, takes screen size to be scaled on and reference to resources
     *
     * @param screenX   screen width (in pixels)
     * @param screenY   screen height (in pixels)
     * @param resources resources
     * @return Background[] full of background photos
     */
    public static Background[] getBackgrounds(int screenX, int screenY, Resources resources) {
        Background[] backgrounds = new Background[19];
        //initialize backgrounds
        backgrounds[0] = new Background(screenX, screenY, resources, R.drawable.a1);
        backgrounds[1] = new Background(screenX, screenY, resources, R.drawable.a2);
        backgrounds[2] = new Background(screenX, screenY, resources, R.drawable.a3);
        backgrounds[3] = new Background(screenX, screenY, resources, R.drawable.a4);
        backgrounds[4] = new Background(screenX, screenY, resources, R.drawable.a5);
        backgrounds[5] = new Background(screenX, screenY, resources, R.drawable.a6);
        backgrounds[6] = new Background(screenX, screenY, resources, R.drawable.a7);
        backgrounds[7] = new Background(screenX, screenY, resources, R.drawable.a8);
        backgrounds[8] = new Background(screenX, screenY, resources, R.drawable.a9);
        backgrounds[9] = new Background(screenX, screenY, resources, R.drawable.a10);
        backgrounds[10] = new Background(screenX, screenY, resources, R.drawable.a11);
        backgrounds[11] = new Background(screenX, screenY, resources, R.drawable.a12);
        backgrounds[12] = new Background(screenX, screenY, resources, R.drawable.a13);
        backgrounds[13] = new Background(screenX, screenY, resources, R.drawable.a14);
        backgrounds[14] = new Background(screenX, screenY, resources, R.drawable.a15);
        backgrounds[15] = new Background(screenX, screenY, resources, R.drawable.a16);
        backgrounds[16] = new Background(screenX, screenY, resources, R.drawable.a17);
        backgrounds[17] = new Background(screenX, screenY, resources, R.drawable.a18);
        backgrounds[18] = new Background(screenX, screenY, resources, R.drawable.a18);

        //
        backgrounds[0].setY(0);
        backgrounds[1].setY(-screenY);

        return backgrounds;
    }

    /**
     * Method for getting saw instance
     *
     * @param screenX   screen width (in pixels)
     * @param screenY   screen height (in pixels)
     * @param resources resources
     * @param saw1      saw resource1
     * @param saw2      saw resource 2
     * @return Saw instance
     */
    public static Saw getSaw(int screenX, int screenY, Resources resources, int saw1, int saw2) {
        return new Saw(screenX, screenY, resources, saw1, saw2);
    }

    /**
     * Method for getting scaled heart instance
     *
     * @param resources resource
     * @return Bitmap of heart
     */
    public static Bitmap getHeart(Resources resources, int screenX, int screenY) {
        Bitmap heart;
        heart = BitmapFactory.decodeResource(resources, R.drawable.heart);
        heart = Bitmap.createScaledBitmap(heart, 100, 100, false);
        return heart;
    }
}
