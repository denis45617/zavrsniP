package com.example.mathspace;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import com.example.mathspace.fallingobj.Shape;
import com.example.mathspace.task.*;
import com.example.mathspace.visual.Background;
import com.example.mathspace.visual.Saw;

import java.net.*;
import java.io.*;

import java.io.IOException;
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

        Task evenTask = new UnrelativeNumberTask(" even numbers", TaskType.EVEN, 0, 500);
        tasks.add(evenTask);
        //odd
        Task oddTask = new UnrelativeNumberTask(" odd numbers", TaskType.ODD, 100, 250);
        tasks.add(oddTask);

        int relativeNumber = (int) (1 + Math.floor(Math.random() * 20));
        //greater
        tasks.add(new RelativeNumberTask(" numbers greater than ", TaskType.GREATER, -50, 50, 5, true));

        //greaterequal
        relativeNumber = (int) (1 + Math.floor(Math.random() * 20));
        Task greaterEqualTask = new RelativeNumberTask(" numbers greater or equal than ", TaskType.GREATEREQUAL, 0, 20, relativeNumber, true);
        tasks.add(greaterEqualTask);

        //lower
        relativeNumber = (int) (5 + Math.floor(Math.random() * 20));
        Task lowerTask = new RelativeNumberTask(" numbers lower than ", TaskType.LOWER, 0, 20, relativeNumber, true);
        tasks.add(lowerTask);

        //lowerequal
        relativeNumber = (int) (5 + Math.floor(Math.random() * 20));
        tasks.add(new RelativeNumberTask(" numbers lower or equal than ", TaskType.LOWEREQUAL, 0, 20, relativeNumber, true));

        //shape square
        Task squareTask = new ShapeTask(" squares", TaskType.SHAPE, Shape.SQUARE);
        tasks.add(squareTask);

        //shape circle
        Task circleTask = new ShapeTask(" circles", TaskType.SHAPE, Shape.CIRCLE);
        tasks.add(circleTask);

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
        Task geomLikoviTask = new WordsTask(" geometrijske likove", TaskType.WORDCONTAINED, geomLikovi, geomTijela);
        tasks.add(geomLikoviTask);
        tasks.add(new WordsTask(" geometrijska tijela", TaskType.WORDCONTAINED, geomTijela, geomLikovi));


        //parni broj koji je veći ili jednak
        List<Task> complexTasks = new ArrayList<>();
        complexTasks.add(greaterEqualTask);
        complexTasks.add(evenTask);
        Task greaterOrEqualEvenNumber = new ComplexTask("greater or equal to x numbers that are even",
                TaskType.COMPLEXTASK, complexTasks);

        tasks.add(greaterOrEqualEvenNumber);

        //geometrijski likovi unutar kruga -- nema previše smisla
        List<Task> complexTasks2 = new ArrayList<>();
        complexTasks2.add(geomLikoviTask);
        complexTasks2.add(circleTask);
        Task geometrijskiLikoviInsideCircle = new ComplexTask("Geometrijski likovi u kružnici",
                TaskType.COMPLEXTASK, complexTasks2);

        tasks.add(geometrijskiLikoviInsideCircle);

        //neparni broj, manji jednak u kvadratu
        List<Task> complexTasks3 = new ArrayList<>();
        complexTasks3.add(squareTask);
        complexTasks3.add(oddTask);
        complexTasks3.add(lowerTask);
        Task squareLowerThenOdd = new ComplexTask("Square, odd, lower then x ",
                TaskType.COMPLEXTASK, complexTasks3);

        tasks.add(squareLowerThenOdd);

        return tasks;
    }

    /**
     * Method that returns only default tasks that are enabled in settings
     *
     * @param sharedPreferences shared preferences
     * @return List of tasks
     */
    public static List<Task> getSelectedTasks(SharedPreferences sharedPreferences) {
        List<Task> selectedTasks = new ArrayList<>();
        List<Task> allTasks;
        boolean useDefaultSettings = sharedPreferences.getBoolean("USEDEFAULT", true);
        if (useDefaultSettings) {
            allTasks = getAllTasks();
        } else {
            allTasks = getTasksFromInternet();
        }

        for (int i = 0; i < allTasks.size(); ++i) {
            if (sharedPreferences.getBoolean("DEFAULT_SETTING:" + i, true)) {
                selectedTasks.add(allTasks.get(i));
            }
        }

        return selectedTasks;
    }

    private static List<Task> getTasksFromInternet() {
        List<Task> tasks = new ArrayList<>();
        Object content = null;
        try {
            URL reqURL = new URL("https://denismath.herokuapp.com/gamecode/mobile/settings/1"); //the URL we will send the request to
            HttpURLConnection request = (HttpURLConnection) (reqURL.openConnection());
            request.setRequestMethod("GET");
            request.connect();
            content = request.getContent();

        } catch (IOException e) {
            e.printStackTrace();
        }


        String content2 = (String) content;
        Log.e("response", content2);
        return tasks;
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
