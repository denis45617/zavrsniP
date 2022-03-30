package com.example.mathspace.task;

import com.example.mathspace.fallingobj.FallingObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WordsTask extends Task {
    private Set<String> correctWords;
    private Set<String> incorrectWords;
    private List<String> correctWordsList = new ArrayList<>();
    private List<String> incorrectWordsList = new ArrayList<>();


    public WordsTask(String taskText, TaskType taskType, Set<String> correctWords, Set<String> incorrectWords) {
        super(taskText, taskType);
        this.correctWords = correctWords;
        this.incorrectWords = incorrectWords;
        this.correctWordsList.addAll(correctWords);
        this.correctWordsList.addAll(incorrectWords);

    }

    public Set<String> getCorrectWords() {
        return correctWords;
    }

    public Set<String> getIncorrectWords() {
        return incorrectWords;
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
}
