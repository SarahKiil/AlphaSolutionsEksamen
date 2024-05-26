package com.example.alphasolutionseksamen.repository;

import com.example.alphasolutionseksamen.model.Project;
import com.example.alphasolutionseksamen.model.Subproject;
import com.example.alphasolutionseksamen.model.Task;
import com.example.alphasolutionseksamen.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:h2schema.sql")
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
        System.out.println(actualProject.getName());
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
        projectRepositoryDB.deleteSubproject("Eksamen", "Testersubprojekt");
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
        projectRepositoryDB.deleteTask("Eksamen", "PO meeting","Testertask");

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
        int expectedSize = 2;
        assertEquals(expectedSize, projects.size());
    }

    @Test
    public void showSubprojects(){
        List<Subproject> subprojects = projectRepositoryDB.showSubprojects("Eksamen");
        int expectedSize = 2;
        assertEquals(expectedSize, subprojects.size());
    }

    @Test
    public void showTasks(){
        List<Task> tasks = projectRepositoryDB.showTasks("Eksamen", "PO meeting");
        int expectedSize = 2;
        assertEquals(expectedSize, tasks.size());
    }

    @Test
    public void updateProject(){
        Project projectToUpdate = new Project("Eksamen", "dette er en eksamen1", "2024-05-29", 13, 0, "Bobby");
        projectRepositoryDB.updateProject("Eksamen", projectToUpdate);
        String expectedResult = "dette er en eksamen1";
        Project projectToShow = projectRepositoryDB.showProject("Eksamen");
        String actualResult = projectToShow.getDescription();
        assertEquals(expectedResult, actualResult);

    }

    @Test
    public void updateTask(){
        Task task = projectRepositoryDB.showTask("Eksamen", "PO meeting", "Userstory 1");
        Task newTask = new Task("Userstory 1", "Blabla", 5, 0);
        projectRepositoryDB.updateTask("Eksamen", "PO meeting", newTask, "Userstory 1");
        Task updatedTask = projectRepositoryDB.showTask("Eksamen", "PO meeting", "Userstory 1");
        String expectedResult = "Blabla";
        String actualResult = updatedTask.getDescription();
        assertEquals(expectedResult, actualResult);
        newTask = new Task("Userstory 1", "Vis userstory til Luise", 5, 0);
        projectRepositoryDB.updateTask("Eksamen", "PO meeting", newTask, "Userstory 1");
    }

    @Test
    public void deleteTask(){
        Task task = new Task ("New Task", "This is a new task", 10, 0);
        Project project = projectRepositoryDB.showProject("Eksamen");
        Subproject subproject = projectRepositoryDB.showSubproject("Eksamen", "PO meeting");
        projectRepositoryDB.createTask(project, subproject, task);
        projectRepositoryDB.deleteTask("Eksamen", "PO meeting", "New Task");
        int expectedResult = 2;
        int actualResult = projectRepositoryDB.showTasks("Eksamen", "PO meeting").size();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void showAssignedUsers(){
        int actualResult = projectRepositoryDB.showAssignedUsers("Eksamen", "PO meeting", "Userstory 1").size();
        int expectedResult = 2;
        assertEquals(expectedResult, actualResult);
    }


}