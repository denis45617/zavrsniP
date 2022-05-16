package com.example.mathspace.task;

import com.example.mathspace.fallingobj.Circle;
import com.example.mathspace.fallingobj.FallingObject;
import com.example.mathspace.fallingobj.Square;

public abstract class NumberTask extends Task {
    private Integer minNumber;
    private Integer maxNumber;

    public NumberTask(String taskText, TaskType taskType, Integer minNumber, Integer maxNumber) {
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
    public abstract String getTaskText();

    @Override
    public abstract boolean checkCollectedIsValid(FallingObject fallingObject);

    @Override
    public FallingObject makeFallingObject() {
        if ((int) Math.floor(Math.random() * 2) == 0) {
            return new Square(String.valueOf((int) (this.minNumber +
                    Math.random() * (this.maxNumber + 1 - this.minNumber))));
        }
        return new Circle(String.valueOf((int) (this.minNumber +
                Math.random() * (this.maxNumber + 1 - this.minNumber))));
    }
}
