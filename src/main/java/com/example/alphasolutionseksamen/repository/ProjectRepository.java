package com.example.alphasolutionseksamen.repository;

import com.example.alphasolutionseksamen.model.Project;
import com.example.alphasolutionseksamen.model.Subproject;
import com.example.alphasolutionseksamen.model.Task;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class ProjectRepository {

    ArrayList<Project> projects = new ArrayList<>();

    public ProjectRepository(){
        Project project = new Project("Eksamen", "Dette er en eksamen", "29-05-2024");
        projects.add(project);
        Subproject subproject = new Subproject("PO meeting", "Mødes med Luise");
        Subproject subproject1 = new Subproject("Code review", "Mødes med Tine");
        project.addSubproject(subproject);
        project.addSubproject(subproject1);
        Task task = new Task("Userstory 1", "Vis userstory til hende", 5);
        Task task1 = new Task("Userstory 2", "Vis userstory til hende", 1);
        addTask(subproject, task);
        addTask(subproject, task1);
        project.setEstimatedHours(project.getEstimatedHours()+subproject.getEstimatedHours());
        project.setEstimatedHours(project.getEstimatedHours()+subproject1.getEstimatedHours());

    }

    public void addTask(Subproject subproject, Task task){
        subproject.addTask(task);
        subproject.setEstimatedHours(subproject.getEstimatedHours()+ task.getEstimatedHours());
    }

    public void createProject(Project project){
        projects.add(project);
    }

    public void createSubproject(Project project, Subproject subproject){
        project.addSubproject(subproject);
    }

    public void createTask(Project project, Subproject subproject, Task task){
        subproject.addTask(task);
        subproject.setEstimatedHours(subproject.getEstimatedHours()+ task.getEstimatedHours());
        project.setEstimatedHours(project.getEstimatedHours()+ task.getEstimatedHours());
    }

    public List<Project>showProjects(){
        return projects;
    }

    public Project showProject(String name){
        for (Project p : projects){
            if (name.equalsIgnoreCase(p.getName())){
                return p;
            }
        }
        return null;
    }


    public Subproject showSubproject(Project project, String name){
        for (Subproject s : project.getSubprojects()){
            if (name.equalsIgnoreCase(s.getName())){
                return s;

            }
        }
        return null;
    }
}
