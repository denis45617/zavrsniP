package com.example.mathspace.task;

import com.example.mathspace.fallingobj.Circle;
import com.example.mathspace.fallingobj.FallingObject;
import com.example.mathspace.fallingobj.Square;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WordsTask extends Task {
    private final Set<String> correctWords;
    private final Set<String> incorrectWords;
    private final List<String> correctWordsList = new ArrayList<>();
    private final List<String> incorrectWordsList = new ArrayList<>();


    public WordsTask(String taskText, TaskType taskType, Set<String> correctWords, Set<String> incorrectWords) {
        super(taskText, taskType);
        this.correctWords = correctWords;
        this.incorrectWords = incorrectWords;
        this.correctWordsList.addAll(correctWords);
        this.correctWordsList.addAll(incorrectWords);

    }


    public String getRandomCorrectWord() {
        return correctWordsList.get((int) Math.floor(Math.random() * correctWordsList.size()));
    }

    public String getRandomIncorrectWord() {
        return incorrectWordsList.get((int) Math.floor(Math.random() * incorrectWords.size()));
    }


    @Override
    public boolean checkCollectedIsValid(FallingObject fallingObject) {
        return correctWords.contains(fallingObject.getText());
    }

    @Override
    public FallingObject makeFallingObject() {
        switch ((int) Math.floor(Math.random() * 4)) {
            case 0:
                return new Square(this.getRandomCorrectWord());
            case 1:
                return new Circle(this.getRandomCorrectWord());
            case 2:
                return new Circle(this.getRandomIncorrectWord());
            default:
                return new Square(this.getRandomIncorrectWord());
        }
    }
}
