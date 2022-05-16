package com.example.mathspace.task;

import com.example.mathspace.fallingobj.FallingObject;
import com.example.mathspace.fallingobj.Shape;

import java.util.Set;

public abstract class Task {
    protected String taskText;
    protected TaskType taskType;


    public Task(String taskText, TaskType taskType) {
        this.taskText = taskText;
        this.taskType = taskType;
    }

    public abstract boolean checkCollectedIsValid(FallingObject fallingObject);

    public abstract FallingObject makeFallingObject();

    public String getTaskText() {
        return taskText;
    }

    public void setTaskText(String taskText) {
        this.taskText = taskText;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    @Override
    public String toString() {
        return taskText;
    }
}
