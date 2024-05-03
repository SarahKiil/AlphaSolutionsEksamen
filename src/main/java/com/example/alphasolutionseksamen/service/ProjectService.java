package com.example.alphasolutionseksamen.service;

import com.example.alphasolutionseksamen.model.Project;
import com.example.alphasolutionseksamen.model.Subproject;
import com.example.alphasolutionseksamen.model.Task;
import com.example.alphasolutionseksamen.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ProjectService {
private ProjectRepository rp;
public ProjectService (ProjectRepository rp) {
    this.rp = rp;
}

public void createProject(Project project){
    rp.createProject(project);
}

public List<Project> showProjects(){
    return rp.showProjects();
}

public Project showProject(String name){
    return rp.showProject(name);
}

public Subproject showSubproject(Project project, String name) {
    return rp.showSubproject(project, name);
}

public void createTask (Project project, Subproject subproject, Task task) {
    rp.createTask(project, subproject, task);
}

public void createSubproject (Project project, Subproject subproject) {
    rp.createSubproject(project, subproject);
}

public void updateProject(String name, Project project){
    rp.updateProject(name, project);
}
public void updateSubject(String subprojectName, Project project, Subproject subproject){
    rp.updateSubProject(subprojectName, project, subproject);
}
    public void updateTask(Subproject subproject, Task task){
        rp.updateTask(subproject, task);
    }
}