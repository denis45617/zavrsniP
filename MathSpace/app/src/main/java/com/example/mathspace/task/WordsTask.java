package com.example.mathspace.task;

import com.example.mathspace.fallingobj.FallingObject;

import java.util.Set;

public class WordsTask extends Task{
    private Set<String> correctWords;
    private Set<String> incorrectWords;


    public WordsTask(String taskText, TaskType taskType, Set<String> correctWords, Set<String> incorrectWords) {
        super(taskText, taskType);
        this.correctWords = correctWords;
        this.incorrectWords = incorrectWords;
    }

    public Set<String> getCorrectWords() {
        return correctWords;
    }

    public Set<String> getIncorrectWords() {
        return incorrectWords;
    }

    @Override
    public boolean checkCollectedIsValid(FallingObject fallingObject) {
        return correctWords.contains(fallingObject.getText());
    }
}
