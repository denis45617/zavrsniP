package com.example.mathspace.task;

import com.example.mathspace.fallingobj.FallingObject;

public class NumberTask extends Task {
    private Integer relativeNumber;

    public NumberTask(String taskText, TaskType taskType, Integer relativeNumber) {
        super(taskText, taskType);
        this.relativeNumber = relativeNumber;
    }

    public Integer getRelativeNumber() {
        return relativeNumber;
    }

    @Override
    public boolean checkCollectedIsValid(FallingObject fallingObject) {
        switch (this.getTaskType()) {    //neki strategy  I guess bolje!
            case EVEN:
                return Integer.parseInt(fallingObject.getText()) % 2 == 0;
            case ODD:
                return Integer.parseInt(fallingObject.getText()) % 2 != 0;
            case EQUAL:
                return Integer.parseInt(fallingObject.getText()) == this.relativeNumber;
            case GREATER:
                return Integer.parseInt(fallingObject.getText()) > this.relativeNumber;
            case GREATEREQUAL:
                return Integer.parseInt(fallingObject.getText()) >= this.relativeNumber;
            case LOWER:
                return Integer.parseInt(fallingObject.getText()) < this.relativeNumber;
            case LOWEREQUAL:
                return Integer.parseInt(fallingObject.getText()) <= this.relativeNumber;
        }

        return false;
    }
}
