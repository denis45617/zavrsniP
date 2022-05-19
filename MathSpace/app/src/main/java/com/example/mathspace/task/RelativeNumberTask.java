package com.example.mathspace.task;

import com.example.mathspace.fallingobj.FallingObject;

public class RelativeNumberTask extends NumberTask {
    private Integer relativeNumber;
    private boolean allowRelativeNumberChange;

    public RelativeNumberTask(String taskText, TaskType taskType, Integer minNumber,
                              Integer maxNumber, Integer relativeNumber, boolean allowRelativeNumberChange) {
        super(taskText, taskType, minNumber, maxNumber);
        this.relativeNumber = relativeNumber;
        this.allowRelativeNumberChange = allowRelativeNumberChange;
    }

    public Integer getRelativeNumber() {
        return relativeNumber;
    }

    public void setRelativeNumber(Integer relativeNumber) {
        this.relativeNumber = relativeNumber;
    }

    public boolean isAllowRelativeNumberChange() {
        return allowRelativeNumberChange;
    }

    @Override
    public String getTaskText() {
        return taskText + " " + relativeNumber;
    }

    @Override
    public boolean checkCollectedIsValid(FallingObject fallingObject) {
        try {
            switch (this.getTaskType()) {
                case EQUAL:
                    return fallingObject.getValue() == this.relativeNumber;
                case GREATER:
                    return fallingObject.getValue()  > this.relativeNumber;
                case GREATEREQUAL:
                    return fallingObject.getValue()  >= this.relativeNumber;
                case LOWER:
                    return fallingObject.getValue()  < this.relativeNumber;
                case LOWEREQUAL:
                    return fallingObject.getValue()  <= this.relativeNumber;
                case DIVIDABLE:
                    return fallingObject.getValue()  % this.relativeNumber == 0;
            }
        } catch (Exception e) {  //trenutno kad se switcha sa rječi na brojeve pokuša se castati text na integer pa baca exception
            //napraviti vrijeme prijelaza između stvari da to ne bude slučaj
            //okej, vrijeme između prijelaza je dodano, ali nek ovo ostane just in case ^-^
            return true;
        }
        return false;
    }
}
