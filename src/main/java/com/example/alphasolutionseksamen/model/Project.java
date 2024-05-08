package com.example.alphasolutionseksamen.model;

import java.util.ArrayList;
import java.util.List;

public class Project {
    private String name;
    private String description;
    private String deadline;

    private double estimatedHours;

    private double usedHours;

    private String username;

    private List<Subproject>subprojects = new ArrayList<>();

    private List<User>assignedUsers;

    public Project(String name, String description, String deadline, double estimatedHours, double usedHours, String username){
        this.name =name;
        this.description = description;
        this.deadline=deadline;
        this.estimatedHours=estimatedHours;
        this.usedHours=usedHours;
        this.username = username;
        this.assignedUsers = new ArrayList<>();
    }
    public Project(String name, String description, String deadline, String username){
        this.name =name;
        this.description = description;
        this.deadline=deadline;
        this.estimatedHours=0;
        this.usedHours=usedHours;
        this.username = username;
        this.assignedUsers = new ArrayList<>();
    }

    public Project(){}

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

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public double getEstimatedHours(){
        return estimatedHours;
    }


    public void addSubproject(Subproject subproject){
        subprojects.add(subproject);
    }

    public void removeSubproject(Subproject subproject){
        subprojects.remove(subproject);
    }

    public List<Subproject> getSubprojects(){
        return subprojects;
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

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public List<User>getAssignedUsers(){
        return assignedUsers;
    }

    public void addUser(User user){
        assignedUsers.add(user);
    }
}
