package com.example.mathspace.task;

import com.example.mathspace.fallingobj.FallingObject;

import java.util.List;
import java.util.Set;

public class WordsTask extends Task{
    private List<String> correctWords;
    private List<String> incorrectWords;


    public WordsTask(String taskText, TaskType taskType, List<String> correctWords, List<String> incorrectWords) {
        super(taskText, taskType);
        this.correctWords = correctWords;
        this.incorrectWords = incorrectWords;
    }

    public List<String> getCorrectWords() {
        return correctWords;
    }

    public List<String> getIncorrectWords() {
        return incorrectWords;
    }

    @Override
    public boolean checkCollectedIsValid(FallingObject fallingObject) {
        return correctWords.contains(fallingObject.getText());
    }
}
