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
        FallingObject objectToBeReturned;
        int number = (int) (this.minNumber +
                Math.random() * (this.maxNumber + 1 - this.minNumber));

        if ((int) Math.floor(Math.random() * 2) == 0) {
            objectToBeReturned = new Square(String.valueOf(number));
        } else {
            objectToBeReturned = new Circle(String.valueOf(number));
        }

        if (Math.random() < FallingObject.getHowOftenComplexText()) {
            objectToBeReturned.setText(generateMathTextBasedOnNumber(number));
        }

        objectToBeReturned.setValue(number);

        return objectToBeReturned;
    }

    private String generateMathTextBasedOnNumber(int number) {
        if (number > 0)
            switch ((int) (Math.random() * 4)) {
                case 0: {
                    return generatePlusText(number);
                }
                case 1: {
                    return generateMinusText(number);
                }

                case 2: {
                    String text = generateMultiplyText(number);
                    if (text != null) {
                        return text;
                    }
                }

                case 3: {
                    return generateDivideText(number);
                }
            }

        return String.valueOf(number);
    }

    private String generatePlusText(int number) {
        int first = (int) (Math.random() * number);
        int second = number - first;
        return first + " + " + second;
    }


    private String generateMinusText(int number) {
        int first = (int) (number + number * Math.random());
        int second = first - number;
        return first + " - " + second;
    }

    private String generateMultiplyText(int number) {
        for (int i = 2; i <= 10; ++i) {
            if (number % i == 0) {
                int first = number / i;
                return first + " * " + i;
            }
        }

        return null;
    }

    private String generateDivideText(int number) {
        int multiplicator = (int) (Math.random() * 6) + 1;
        int numb = number * multiplicator;

        return numb + " / " + multiplicator;
    }

}
