package com.example.alphasolutionseksamen.service;

import com.example.alphasolutionseksamen.model.Project;
import com.example.alphasolutionseksamen.model.Subproject;
import com.example.alphasolutionseksamen.model.Task;
import com.example.alphasolutionseksamen.model.User;
import com.example.alphasolutionseksamen.repository.ProjectRepositoryDB;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
private ProjectRepositoryDB rpd;
public ProjectService (ProjectRepositoryDB rpd) {
    this.rpd = rpd;
}

public void createProject(Project project){
    rpd.createProject(project);
}

public Project showProject(String projectName) {
    return rpd.showProject(projectName);
}



    public void createSubproject(Project project, Subproject subproject) {
    rpd.createSubproject(project, subproject);
    }

    public Subproject showSubproject(String projectName, String subprojectName) {
    return rpd.showSubproject(projectName, subprojectName);
    }

    public Task showTask(String projectName, String subprojectName, String taskName) {
    return rpd.showTask(projectName, subprojectName, taskName);
    }

    public User showUser(String username) {
    return rpd.showUser(username);
    }

    public List<Project> showProjects() {
        return rpd.showProjects();
    }

    public List<Project> showDoneProjects(){
    return rpd.showDoneProjects();
    }

    public List<Subproject> showSubprojects(String project) {
    return rpd.showSubprojects(project);
    }

    public List<Task> showTasks(String project, String subproject) {
    return rpd.showTasks(project, subproject);
    }
    public List<String> showSkills () { return rpd.showSkills();}
    public List <String> showStatus () {
    return rpd.showStatus();
    }
    public List<User> showUsers(){
    return rpd.showUsers();
    }

    public List<User> showUsersWithSkill(String skill){
    return rpd.showUsersWithSkill(skill);
    }

    public List<String> showUserNames(){
    return rpd.showUserNames();
    }

    public List<String> showAssignedUsers(String projectName, String subprojectName, String taskName) {
    return rpd.showAssignedUsers(projectName, subprojectName, taskName);
    }

    public List<Project> showsProjectsForUser(User user) {
    return rpd.showsProjectsForUser(user);
    }

    public List<Project> showProjectsForManagers(User user) {
    return rpd.showProjectsForManagers(user);
    }

    public void createTask(Project project, Subproject subproject, Task task) {
    rpd.createTask(project, subproject, task);
    }

    public void createUser(User user) {
    rpd.createUser(user);
    }

    public void updateProject(String projectName, Project project) {
    rpd.updateProject(projectName, project);
    }

    public void updateSubproject(String projectName, String subprojectName, Subproject subproject) {
    rpd.updateSubproject(projectName, subprojectName, subproject);
    }

    public void updateTask(String projectName, String subprojectName, Task task, String taskName) {
    rpd.updateTask(projectName, subprojectName, task, taskName);
    }

    public void updateUser (User user) {
    rpd.updateUser(user);
    }
    public void deleteUser (User user) {
    rpd.deleteUser(user);
    }

    public void finishATask(String projectName, String subprojectName, Task task){
    rpd.finishATask(projectName, subprojectName, task);
    }

    public void updateHours(String projectName, String subprojectName, String taskName, Task task) {
    rpd.updateHours(projectName, subprojectName, taskName, task);
    }

    public boolean checkMail(User user){
    return rpd.checkMail(user);
    }

    public boolean checkNumber(User user){
    return rpd.checkNumber(user);
    }

    public boolean checkLogin(User user) {
    return rpd.checkLogin(user);
    }

    public boolean checkUsers(String username){
    return rpd.checkUsers(username);
    }

    public List<Project> checkStatusProject(List<Project> projects){
    return rpd.checkStatusProject(projects);
    }

    public List<Subproject> checkStatusSubproject(String projectName, List<Subproject>subprojects){
    return rpd.checkStatusSubproject(projectName, subprojects);
    }

    public void addUser(String projectName, String subprojectName, String taskName, User user){
    rpd.addUser(projectName, subprojectName, taskName, user);
    }

    public void deleteProject(String projectName){
    rpd.deleteProject(projectName);
    }

    public void deleteSubproject(String projectName, String subprojectName){
    rpd.deleteSubproject(projectName, subprojectName);
    }

    public void deleteTask(String projectName, String subprojectName, String taskName){
    rpd.deleteTask(projectName, subprojectName, taskName);
    }

    public Double calculateUserWorkload(String projectName, String subprojectName, Task task){
    return rpd.calculateUserWorkload(projectName, subprojectName, task);
    }

    public void unassignUser(String projectName, String subprojectName, String taskName, String username){
    rpd.unassignUser(projectName, subprojectName, taskName, username);
    }
}
