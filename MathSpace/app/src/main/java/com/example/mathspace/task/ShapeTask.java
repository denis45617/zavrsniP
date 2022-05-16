package com.example.mathspace.task;

import com.example.mathspace.fallingobj.Circle;
import com.example.mathspace.fallingobj.FallingObject;
import com.example.mathspace.fallingobj.Shape;
import com.example.mathspace.fallingobj.Square;

public class ShapeTask extends Task {
    private Shape askedShape;

    public ShapeTask(String taskText, TaskType taskType, Shape askedShape) {
        super(taskText, taskType);
        this.askedShape = askedShape;
    }

    @Override
    public boolean checkCollectedIsValid(FallingObject fallingObject) {
        return fallingObject.getShape() == askedShape;
    }

    @Override
    public FallingObject makeFallingObject() {
        if ((int) Math.floor(Math.random() * 2) == 0) {
            return new Square(String.valueOf((int) Math.floor(Math.random() * 21)));
        }
        return new Circle(String.valueOf((int) Math.floor(Math.random() * 21)));
    }
}
