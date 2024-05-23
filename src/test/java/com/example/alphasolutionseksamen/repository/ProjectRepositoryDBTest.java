package com.example.alphasolutionseksamen.repository;

import com.example.alphasolutionseksamen.model.Project;
import com.example.alphasolutionseksamen.model.Subproject;
import com.example.alphasolutionseksamen.model.Task;
import com.example.alphasolutionseksamen.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProjectRepositoryDBTest {

    @Autowired
    ProjectRepositoryDB projectRepositoryDB;
    @Test
    public void getProjectId(){
        int projectId = projectRepositoryDB.getProjectID("Eksamen");
        int expectedID = 1;
        assertEquals(1, projectId);
    }

    @Test
    public void getSubprojectID(){
        int subprojectId = projectRepositoryDB.getSubprojectID("Eksamen", "Code review");
        int expectedID = 2;
        assertEquals(2, subprojectId);
    }

    @Test
    public void showProject(){
        Project project = projectRepositoryDB.showProject("Eksamen");
        String expectedName = "Eksamen";
        assertEquals(expectedName, project.getName());
    }

    @Test
    public void showSubproject(){
        Subproject subproject = projectRepositoryDB.showSubproject("Eksamen", "Code review");
        String expectedName = "Code review";
        assertEquals(expectedName, subproject.getName());
    }

    @Test
    public void showTask(){
        Task task = projectRepositoryDB.showTask("Eksamen", "PO meeting", "Userstory 2");
        String expectedName = "Userstory 2";
        assertEquals(expectedName, task.getName());
    }

    @Test
    public void createProject(){
        Project project = new Project ("Testerprojekt", "Tester projekt", "2024-05-29", "abc");
        projectRepositoryDB.createProject(project);
        Project actualProject = projectRepositoryDB.showProject("Testerprojekt");
        String expectedName = "Testerprojekt";
        assertEquals(expectedName, actualProject.getName());
    }

    @Test
    public void createSubproject(){
        Project project = projectRepositoryDB.showProject("Eksamen");
        Subproject subproject = new Subproject ("Testersubprojekt", "Tester subprojekt");
        projectRepositoryDB.createSubproject(project, subproject);
        Subproject actualSubproject = projectRepositoryDB.showSubproject("Eksamen", "Testersubprojekt");
        String expectedName = "Testersubprojekt";
        assertEquals(expectedName, actualSubproject.getName());
    }
    @Test
    public void createTask(){
        Project project = projectRepositoryDB.showProject("Eksamen");
        Subproject subproject = projectRepositoryDB.showSubproject("Eksamen", "PO meeting");
        Task task = new Task ("Testertask", "Tester", 5.0, 0.0);
        projectRepositoryDB.createTask(project, subproject, task);
        Task actualTask = projectRepositoryDB.showTask("Eksamen", "PO meeting", "Testertask");
        String expectedName = "Testertask";
        assertEquals(expectedName, actualTask.getName());
    }

    @Test
    public void showUser(){
        User user = projectRepositoryDB.showUser("Bobby");
        String expectedName = "Bobby";
        assertEquals(expectedName, user.getUsername());

    }

    @Test
    public void showProjects() {
        List<Project> projects = projectRepositoryDB.showProjects();
        int expectedSize = 7;
        assertEquals(expectedSize, projects.size());
    }

    @Test
    public void showSubprojects(){
        List<Subproject> subprojects = projectRepositoryDB.showSubprojects("Eksamen");
        int expectedSize = 7;
        assertEquals(expectedSize, subprojects.size());
    }

    @Test
    public void showTasks(){
        List<Task> tasks = projectRepositoryDB.showTasks("Eksamen", "PO meeting");
        int expectedSize = 6;
        assertEquals(6, tasks.size());
    }



}