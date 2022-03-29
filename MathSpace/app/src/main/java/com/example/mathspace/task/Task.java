package com.example.mathspace.task;

import com.example.mathspace.fallingobj.Shape;

import java.util.Set;

public class Task {
    private String taskText;
    private TaskType taskType;
    private Integer relativeNumber;
    private Shape askedShape;
    private Set<String> words;


    public Task(String taskText, TaskType taskType, Integer relativeNumber, Shape askedShape, Set<String> words) {
        this.taskText = taskText;
        this.taskType = taskType;
        this.relativeNumber = relativeNumber;
        this.askedShape = askedShape;
        this.words = words;
    }

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

    public Integer getRelativeNumber() {
        return relativeNumber;
    }

    public void setRelativeNumber(Integer relativeNumber) {
        this.relativeNumber = relativeNumber;
    }

    public Shape getAskedShape() {
        return askedShape;
    }

    public void setAskedShape(Shape askedShape) {
        this.askedShape = askedShape;
    }

    public Set<String> getWords() {
        return words;
    }

    public void setWords(Set<String> words) {
        this.words = words;
    }
}
