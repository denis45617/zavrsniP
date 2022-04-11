package com.example.mathspace.task;

import com.example.mathspace.fallingobj.FallingObject;

public class NumberTask extends Task {
    private Integer relativeNumber;

    public NumberTask(String taskText, TaskType taskType, Integer relativeNumber) {
        super(taskText, taskType);
        this.relativeNumber = relativeNumber;
    }

    @Override
    public String getTaskText() {
        if (relativeNumber != null)
            return super.getTaskText() + relativeNumber;
        return super.getTaskText();
    }

    public Integer getRelativeNumber() {
        return relativeNumber;
    }

    public void setRelativeNumber(Integer relativeNumber) {
        this.relativeNumber = relativeNumber;
    }

    @Override
    public boolean checkCollectedIsValid(FallingObject fallingObject) {
        try {
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
        } catch (Exception e) {  //trenutno kad se switcha sa rječi na brojeve pokuša se castati text na integer pa baca exception
            //napraviti vrijeme prijelaza između stvari da to ne bude slučaj
            return true;
        }
        return false;
    }
}
