package com.example.alphasolutionseksamen.repository;

import com.example.alphasolutionseksamen.model.Project;
import com.example.alphasolutionseksamen.model.Subproject;
import com.example.alphasolutionseksamen.model.Task;
import com.example.alphasolutionseksamen.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class ProjectRepository {

    ArrayList<Project> projects = new ArrayList<>();

    ArrayList<User> users = new ArrayList<>();

    public ProjectRepository() {
        Project project = new Project("Eksamen", "Dette er en eksamen", "29-05-2024", "Bobby");
        Project project1 = new Project("New Project", "Test this thing", "29-05-2024", "Bobby");
        User user = new User("Bobby", "Bobby", "Bobsen", "bobby555", "bobbyersej@gmail.com", "Bobbyvej", "66", 2200, "København", 12345678, "Denmark");
        users.add(new User("abc", "Harry", "Potter", "password123", "harrypotter@mail.co.uk", "Londonroad", "29", 10000, "London", 1234567899, "England"));
        users.add(new User("HanneRocks", "Hanne", "Jensen", "1234", "HanneFraMarketing@gmail.com", "Taffelæblevej", "13", 4000, "Roskilde", 87654321, "Sverige"));
        projects.add(project);
        users.add(user);
        Subproject subproject = new Subproject("PO meeting", "Mødes med Luise");
        Subproject subproject1 = new Subproject("Code review", "Mødes med Tine");
        Subproject subproject2 = new Subproject("Business", "Stuff");
        project.addSubproject(subproject);
        project.addSubproject(subproject1);
        Task task = new Task("Userstory 1", "Vis userstory til hende", 5, 0);
        Task task1 = new Task("Userstory 2", "Vis userstory til hende", 1, 0);
        Task task2 = new Task("Blablabla", "Hello", 1, 0);
        createTask(project, subproject, task);
        createTask(project, subproject, task1);
        createTask(project1, subproject2, task2);
        task.addUser(user);
        task1.addUser(user);
        task2.addUser(user);

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

    public List<Project> showsProjectsForUser(User user){
        List<Project>assignedProjects =new ArrayList<>();
        for (Project p : projects){
            for (Subproject s : p.getSubprojects()){
                for (Task t : s.getTasks()){
                    for (User u : t.getAssignedUsers()){
                        if (u.getUsername().equals(user.getUsername())){
                            assignedProjects.add(p);
                        }
                    }
                }
            }
        }
        return assignedProjects;
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
            if (p.getName().equals(name)) {
                p.setName(project.getName());
                p.setDescription(project.getDescription());
            }


        }
    }

    public void updateSubProject(String subprojectName, Project project, Subproject subproject) {
        for (Subproject s : project.getSubprojects()) {
            if (s.getName().equals(subprojectName)) {
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

    public void updateHours(Project project, String taskName, Subproject subproject, Task task) {
        double usedHoursSubproject = subproject.getUsedHours();
        double usedHoursSubprojectNew = subproject.getUsedHours();
        for (Task t : subproject.getTasks()) {
            if (t.getName().equals(taskName)) {
                usedHoursSubprojectNew = subproject.getUsedHours() - t.getUsedHours() + task.getUsedHours();
                t.setUsedHours(task.getUsedHours());
                subproject.setUsedHours(usedHoursSubprojectNew);
                project.setUsedHours(project.getUsedHours() - usedHoursSubproject + usedHoursSubprojectNew);
            }
        }
    }

    public void createUser(User user) {
        users.add(user);
    }



    public boolean checkLogin(User user){
        for (User u : users){
            if (user.getUsername().equals(u.getUsername())){
                if (user.getPassword().equals(u.getPassword())){
                    return true;
                }
            }
        }
        return false;
    }

    public User showUser(User user){
        for (User u : users){
            if (u.getUsername().equals(user.getUsername())){
                return u;
            }
        }
        return null;
    }

    public boolean checkMail(User user){
        if (user.getEmail().contains("@")){
        if (user.getEmail().contains(".")){
            String[]mail = user.getEmail().split("\\.");
                int last = mail.length;
                if (mail[last-1].length()<6){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkNumber(User user){
        String numberToCheck = String.valueOf(user.getPhoneNumber());
        if (numberToCheck.length()>7&&numberToCheck.length()<13){
            return true;
        }
        return false;
    }

    public User showUser(String username){
        for (User u : users){
            if (u.getUsername().equals(username)){
                return u;
            }
        }
        return null;
    }

    public boolean checkUsers(String username){
        for (User u : users){
            if (u.getUsername().equals(username)){
                return true;
            }
        }
        return false;
    }

    public void addUser(Project project, Subproject subproject, Task task, User user){
        task.addUser(user);
        subproject.addUser(user);
        project.addUser(user);
    }
}


