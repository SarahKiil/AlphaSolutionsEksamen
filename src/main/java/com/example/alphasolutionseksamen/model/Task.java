package com.example.alphasolutionseksamen.model;

import com.example.alphasolutionseksamen.Priority;

import java.util.ArrayList;
import java.util.List;

public class Task {
    private String name;
    private String description;
    private double estimatedHours;

    private double usedHours;
    private boolean done;

    private List<User> assignedUsers;

    private List<String> skills;

    public Priority getStatus() {
        return status;
    }

    public void setStatus(Priority status) {
        this.status = status;
    }

    private Priority status;

    public Task (String name, String description, double estimatedHours, double usedHours) {
        this.name = name;
        this.description = description;
        this.estimatedHours = estimatedHours;
        this.usedHours = usedHours;
        this.assignedUsers = new ArrayList<>();
        this.skills = new ArrayList<>();
        this.done =false;
    }

    public Task () {}

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

    public void addUser(User user){
        assignedUsers.add(user);
    }

    public List<User> getAssignedUsers() {
        return assignedUsers;
    }

    public void setAssignedUsers(List<User> assignedUsers) {
        this.assignedUsers = assignedUsers;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public List<String> getSkills() {
        return skills;
    }

    public String getSkillsSetup() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : skills) {
            stringBuilder.append(s);
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }
    public void setSkills(List<String> skills) {
        this.skills = skills;
    }
}
