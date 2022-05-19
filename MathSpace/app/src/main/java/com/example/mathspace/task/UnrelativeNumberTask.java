package com.example.mathspace.task;

import com.example.mathspace.fallingobj.FallingObject;

public class UnrelativeNumberTask extends NumberTask {

    public UnrelativeNumberTask(String taskText, TaskType taskType, Integer minNumber, Integer maxNumber) {
        super(taskText, taskType, minNumber, maxNumber);
    }

    @Override
    public String getTaskText() {
        return taskText;
    }

    @Override
    public boolean checkCollectedIsValid(FallingObject fallingObject) {
        try {
            switch (this.getTaskType()) {
                case EVEN:
                    return fallingObject.getValue() % 2 == 0;
                case ODD:
                    return fallingObject.getValue() % 2 != 0;
            }
        } catch (Exception e) {  //trenutno kad se switcha sa rječi na brojeve pokuša se castati text na integer pa baca exception
            //napraviti vrijeme prijelaza između stvari da to ne bude slučaj
            //okej, vrijeme između prijelaza je dodano, ali nek ovo ostane just in case ^-^
            return true;  //ako se dogodi exception baš u krivo vrijeme, priznaj odgovor
        }
        return false;
    }
}
