package com.example.mathspace.task;

import com.example.mathspace.fallingobj.FallingObject;

import java.util.List;

public class ComplexTask extends Task {
    private List<Task> tasks;
    private String taskText;

    public ComplexTask(String taskText, TaskType taskType, List<Task> tasks) {
        super(taskText, taskType);
        this.tasks = tasks;
        this.taskText = generateTaskText();
    }

    private String generateTaskText() {
        StringBuilder sb = new StringBuilder();
        for (Task task : tasks) {
            sb.append(task.getTaskText()).append(",");
        }
        sb = new StringBuilder(sb.substring(0, sb.length() - 1));

        return sb.toString();
    }

    public void changeTaskText() {
        this.taskText = generateTaskText();
    }

    public List<Task> getTasks() {
        return tasks;
    }

    @Override
    public boolean checkCollectedIsValid(FallingObject fallingObject) {
        for (Task task : tasks) {
            if (!task.checkCollectedIsValid(fallingObject)) return false;
        }
        return true;
    }

    @Override
    public FallingObject makeFallingObject() {
        int randomTask = (int) (Math.random() * tasks.size());
        return tasks.get(randomTask).makeFallingObject();
    }


    @Override
    public String getTaskText() {
        return this.taskText;
    }

}
