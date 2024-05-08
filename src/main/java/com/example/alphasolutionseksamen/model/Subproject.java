package com.example.alphasolutionseksamen.model;

import java.util.ArrayList;
import java.util.List;

public class Subproject {
    private String name;
    private String description;
    private double estimatedHours;

    private double usedHours;

    private List<Task>tasks = new ArrayList<>() {
    };

    private List<User>assignedUsers;

    public Subproject (String name, String description, double estimatedHours, double usedHours) {
        this.name = name;
        this.description = description;
        this.estimatedHours = estimatedHours;
        this.usedHours = usedHours;
        this.assignedUsers = new ArrayList<>();
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

    public double getUsedHours(){
        return usedHours;
    }

    public void setUsedHours(double usedHours){
        this.usedHours = usedHours;
    }

    public List<User> getAssignedUsers(){
        return assignedUsers;
    }

    public void addUser(User user){
        assignedUsers.add(user);
    }
}
