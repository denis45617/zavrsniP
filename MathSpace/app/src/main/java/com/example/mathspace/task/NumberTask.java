package com.example.mathspace.task;

import com.example.mathspace.fallingobj.FallingObject;

public abstract class NumberTask extends Task {
    private Integer minNumber;
    private Integer maxNumber;

    public NumberTask(String taskText, TaskType taskType, Integer minNumber, Integer maxNumber ) {
        super(taskText, taskType);
        this.minNumber = minNumber;
        this.maxNumber = maxNumber;
    }

    public Integer getMinNumber() {
        return minNumber;
    }

    public Integer getMaxNumber() {
        return maxNumber;
    }

    @Override
    public  abstract String getTaskText();


    @Override
    public abstract boolean checkCollectedIsValid(FallingObject fallingObject);
}
