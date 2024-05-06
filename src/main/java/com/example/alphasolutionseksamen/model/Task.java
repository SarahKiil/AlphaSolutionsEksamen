package com.example.alphasolutionseksamen.model;

public class Task {
    private String name;
    private String description;
    private double estimatedHours;

    private double usedHours;

    public Task (String name, String description, double estimatedHours, double usedHours) {
        this.name = name;
        this.description = description;
        this.estimatedHours = estimatedHours;
        this.usedHours = usedHours;
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
}
