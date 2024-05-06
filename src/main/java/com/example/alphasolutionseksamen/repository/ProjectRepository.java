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

    public ProjectRepository() {
        Project project = new Project("Eksamen", "Dette er en eksamen", "29-05-2024");
        projects.add(project);
        Subproject subproject = new Subproject("PO meeting", "Mødes med Luise");
        Subproject subproject1 = new Subproject("Code review", "Mødes med Tine");
        project.addSubproject(subproject);
        project.addSubproject(subproject1);
        Task task = new Task("Userstory 1", "Vis userstory til hende", 5, 0);
        Task task1 = new Task("Userstory 2", "Vis userstory til hende", 1, 0);
        createTask(project, subproject, task);
        createTask(project, subproject, task1);

    }


    public void createProject(Project project) {
        projects.add(project);
    }

    public void createSubproject(Project project, Subproject subproject) {
        project.addSubproject(subproject);
    }

    public void createTask(Project project, Subproject subproject, Task task) {
        subproject.addTask(task);
        subproject.setEstimatedHours(subproject.getEstimatedHours() + task.getEstimatedHours());
        project.setEstimatedHours(project.getEstimatedHours() + task.getEstimatedHours());
    }

    public List<Project> showProjects() {
        return projects;
    }

    public Project showProject(String name) {
        for (Project p : projects) {
            if (name.equalsIgnoreCase(p.getName())) {
                return p;
            }
        }
        return null;
    }


    public Subproject showSubproject(Project project, String name) {
        for (Subproject s : project.getSubprojects()) {
            if (name.equalsIgnoreCase(s.getName())) {
                return s;

            }
        }
        return null;
    }

    public Task showTask(Subproject subproject, String name) {
        for (Task t : subproject.getTasks()) {
            if (name.equalsIgnoreCase(t.getName())) {
                return t;

            }
        }
        return null;
    }
    public void updateProject(String name, Project project) {
        for (Project p : projects) {
            if (p.getName().equals(name)){
                p.setName(project.getName());
                p.setDescription(project.getDescription());
            }


        }
    }

    public void updateSubProject(String subprojectName, Project project, Subproject subproject){
        for (Subproject s : project.getSubprojects()){
            if (s.getName().equals(subprojectName)){
                s.setName(subproject.getName());
                s.setDescription(subproject.getDescription());
            }
        }
    }


    public void updateTask(Project project, String taskName, Subproject subproject, Task task) {
        double estimatedHoursSubproject = subproject.getEstimatedHours();
        double estimatedHoursSubprojectNew = subproject.getEstimatedHours();
        for (Task t : subproject.getTasks()) {
            if (t.getName().equals(taskName)) {
                estimatedHoursSubprojectNew = subproject.getEstimatedHours() - t.getEstimatedHours() + task.getEstimatedHours();
                t.setName(task.getName());
                t.setDescription(task.getDescription());
                t.setEstimatedHours(task.getEstimatedHours());
                subproject.setEstimatedHours(estimatedHoursSubprojectNew);
                project.setEstimatedHours(project.getEstimatedHours() - estimatedHoursSubproject + estimatedHoursSubprojectNew);
            }
        }
    }

        public void updateHours(Project project, String taskName, Subproject subproject, Task task){
            double usedHoursSubproject = subproject.getUsedHours();
            double usedHoursSubprojectNew = subproject.getUsedHours();
            for (Task t : subproject.getTasks()){
                if (t.getName().equals(taskName)){
                    usedHoursSubprojectNew = subproject.getUsedHours() - t.getUsedHours() + task.getUsedHours();
                    t.setUsedHours(task.getUsedHours());
                    subproject.setUsedHours(usedHoursSubprojectNew);
                    project.setUsedHours(project.getUsedHours() - usedHoursSubproject + usedHoursSubprojectNew);
                }
        }
    }

}
