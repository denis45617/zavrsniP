package com.example.mathspace.task;

import com.example.mathspace.fallingobj.FallingObject;
import com.example.mathspace.fallingobj.Shape;

public class ShapeTask extends Task{
    private Shape askedShape;

    public ShapeTask(String taskText, TaskType taskType, Shape askedShape) {
        super(taskText, taskType);
        this.askedShape = askedShape;
    }

    @Override
    public boolean checkCollectedIsValid(FallingObject fallingObject) {
        return fallingObject.getShape() == askedShape;
    }
}
