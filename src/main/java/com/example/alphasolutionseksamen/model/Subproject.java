package com.example.alphasolutionseksamen.model;

import java.util.ArrayList;
import java.util.List;

public class Subproject {
    private String name;
    private String description;
    private double estimatedHours;

    private List<Task>tasks = new ArrayList<>() {
    };

    public Subproject (String name, String description) {
        this.name = name;
        this.description = description;
        this.estimatedHours = 0;
    }

    public Subproject() {}

    public void addTask(Task task){
        tasks.add(task);
    }

    public void removeTask(Task task){
        tasks.remove(task);
    }

    public List<Task> getTasks(){
        return tasks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getEstimatedHours() {
        return estimatedHours;
    }




    public void setEstimatedHours(double estimatedHours) {
        this.estimatedHours = estimatedHours;
    }
}
