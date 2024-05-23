package com.example.alphasolutionseksamen.repository;

import com.example.alphasolutionseksamen.Priority;
import com.example.alphasolutionseksamen.model.Project;
import com.example.alphasolutionseksamen.model.Subproject;
import com.example.alphasolutionseksamen.model.Task;
import com.example.alphasolutionseksamen.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Repository
public class ProjectRepositoryDB {
    @Value("${spring.datasource.url}")
    private String db_url;
    @Value("${spring.datasource.username}")
    private String SQLusername;
    @Value("${spring.datasource.password}")
    private String pwd;


    public int getProjectID(String projectName) {
        int id = 0;
        try (Connection connection = DriverManager.getConnection(db_url, SQLusername, pwd)) {
            String SQL = "SELECT ID FROM PROJECTS WHERE NAME=?;";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, projectName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                id = (rs.getInt(1));
        }

    } catch (SQLException e) {
            throw new RuntimeException(e);}
            return id;
        }

    public int getSubprojectID(String projectName, String subprojectName) {
        int projectID = getProjectID(projectName);
        int subprojectID = 0;
        try (Connection connection = DriverManager.getConnection(db_url, SQLusername, pwd)) {
            String SQL = "SELECT ID FROM SUBPROJECTS WHERE PROJECT_ID=? AND NAME=?;";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, projectID);
            ps.setString(2, subprojectName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                subprojectID = (rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return subprojectID;
    }

    public void createProject(Project project) {
        try (Connection connection = DriverManager.getConnection(db_url, SQLusername, pwd)) {
            String SQL = "insert into projects (name, description, deadline, estimatedhours, usedhours, username) values (?, ?, ?, ?, ?, ?);";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, project.getName());
            ps.setString(2, project.getDescription());
            ps.setString(3, project.getDeadline());
            ps.setDouble(4, 0);
            ps.setDouble(5, 0);
            ps.setString(6, project.getUsername());
            int rs = ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

        public void createSubproject(Project project, Subproject subproject) {
        try (Connection connection = DriverManager.getConnection(db_url, SQLusername, pwd)) {
            int id = getProjectID(project.getName());
            String SQL = "insert into subprojects (name, description, estimatedhours, usedhours, project_id) values (?, ?, ?, ?, ?);";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, subproject.getName());
            ps.setString(2, subproject.getDescription());
            ps.setDouble(3, 0);
            ps.setDouble(4, 0);
            ps.setInt(5, id);
            int rs = ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createTask(Project project, Subproject subproject, Task task) {
        try (Connection connection = DriverManager.getConnection(db_url, SQLusername, pwd)) {
            int id = getSubprojectID(project.getName(), subproject.getName());
            double estimatedHoursSubproject = 0;
            double estimatedHoursSubprojectNew = 0;
            double estimatedHoursProject = 0;
            double estimatedHoursprojectNew = 0;
            String priority = "uncategorized";
            if (task.getStatus()==Priority.HIGH){
                priority = "high";}
            else if (task.getStatus()==Priority.MEDIUM){
                priority = "medium";
                }
            else if (task.getStatus()==Priority.LOW){
                priority = "low";
            }
            String done = "f";
            if (task.isDone()){
                done="t";
            }

            String SQL = "insert into tasks (name, description, estimatedhours, usedhours, subproject_id, done, status) values (?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, task.getName());
            ps.setString(2, task.getDescription());
            ps.setDouble(3, task.getEstimatedHours());
            ps.setDouble(4, 0);
            ps.setInt(5, id);
            ps.setString(6, done);
            ps.setString(7, priority);
            int rs = ps.executeUpdate();

            String SQL1 = "SELECT ESTIMATEDHOURS FROM PROJECTS WHERE NAME=?;";
            PreparedStatement ps1 = connection.prepareStatement(SQL1);
            ps1.setString(1, project.getName());
            ResultSet rs1 = ps1.executeQuery();
            while (rs1.next()) {
                estimatedHoursProject = (rs1.getInt(1));
            }
            estimatedHoursprojectNew = estimatedHoursProject + task.getEstimatedHours();

            String SQL2 = "SELECT ESTIMATEDHOURS FROM SUBPROJECTS WHERE NAME=?;";
            PreparedStatement ps2 = connection.prepareStatement(SQL2);
            ps2.setString(1, subproject.getName());
            ResultSet rs2 = ps2.executeQuery();
            while (rs2.next()) {
                estimatedHoursSubproject = (rs2.getInt(1));
            }
            estimatedHoursSubprojectNew = estimatedHoursSubproject + task.getEstimatedHours();

            String SQL3 = "UPDATE PROJECTS SET ESTIMATEDHOURS=? WHERE NAME=?";
            PreparedStatement ps3 = connection.prepareStatement(SQL3);
            ps3.setDouble(1, estimatedHoursprojectNew);
            ps3.setString(2, project.getName());
            int rs3 = ps3.executeUpdate();

            String SQL4 = "UPDATE SUBPROJECTS SET ESTIMATEDHOURS=? WHERE NAME=?";
            PreparedStatement ps4 = connection.prepareStatement(SQL4);
            ps4.setDouble(1, estimatedHoursSubprojectNew);
            ps4.setString(2, subproject.getName());
            int rs4 = ps4.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        createTaskSkills(project, subproject, task);
    }

    public void createTaskSkills(Project project, Subproject subproject, Task task) {
        int id = 0;
        List<Integer> skillIDs = new ArrayList<>();
        int subprojectID = getSubprojectID(project.getName(), subproject.getName());
        try (Connection connection = DriverManager.getConnection(db_url, SQLusername, pwd)) {
            String SQL = "SELECT ID FROM TASKS WHERE NAME=? AND SUBPROJECT_ID=?;";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, task.getName());
            ps.setInt(2, subprojectID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                id = (rs.getInt(1));
            }
            for (String s : task.getSkills()) {
                String SQL1 = "SELECT ID FROM SKILLS WHERE NAME=?;";
                PreparedStatement ps1 = connection.prepareStatement(SQL1);
                ps1.setString(1, s);
                ResultSet rs1 = ps1.executeQuery();
                while (rs1.next()) {
                    skillIDs.add(rs1.getInt(1));
                }
            }
            for (Integer i : skillIDs) {
                String SQL2 = "INSERT INTO SKILLS_TASKS (Skill_ID, task_id) values (?, ?);";
                PreparedStatement ps2 = connection.prepareStatement(SQL2);
                ps2.setInt(1, i);
                ps2.setInt(2, id);
                int rs2 = ps2.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }


        public Project showProject(String projectName) {
        Project project = new Project();
        try (Connection connection = DriverManager.getConnection(db_url, SQLusername, pwd)) {
            String SQL = "SELECT * FROM PROJECTS WHERE NAME=?;";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, projectName);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                project = new Project(rs.getString(2), rs.getString(3), rs.getString(4), rs.getDouble(5), rs.getDouble(6), rs.getString(7));
            }


        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return project;
    }

    public Subproject showSubproject(String projectName, String subprojectName) {
        Subproject subproject = new Subproject();

        try (Connection connection = DriverManager.getConnection(db_url, SQLusername, pwd)) {
            int id = getProjectID(projectName);

            String SQL1 = "SELECT * FROM SUBPROJECTS WHERE PROJECT_ID=? AND NAME=?";
            PreparedStatement ps1 = connection.prepareStatement(SQL1);
            ps1.setInt(1, id);
            ps1.setString(2, subprojectName);
            ResultSet rs1 = ps1.executeQuery();
            while (rs1.next()) {
                subproject = new Subproject(rs1.getString(2), rs1.getString(3), rs1.getDouble(4), rs1.getDouble(5));
            }


        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return subproject;
    }

    public Task showTask(String projectName, String subprojectName, String taskName) {
        Task task = new Task();
        int id = 0;
        List<Integer> skillIDs = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(db_url, SQLusername, pwd)) {
            int subprojectID = getSubprojectID(projectName, subprojectName);

            String SQL2 = "SELECT * FROM TASKS WHERE SUBPROJECT_ID=? AND NAME=?";
            PreparedStatement ps2 = connection.prepareStatement(SQL2);
            ps2.setInt(1, subprojectID);
            ps2.setString(2, taskName);
            ResultSet rs2 = ps2.executeQuery();
            while (rs2.next()) {
                id = (rs2.getInt(1));
                task = new Task(rs2.getString(2), rs2.getString(3), rs2.getDouble(4), rs2.getDouble(5));
                if (rs2.getString(7).equals("t")) {
                    task.setDone(true);
                }
                if (rs2.getString(8).equals("high")){
                    task.setStatus(Priority.HIGH);
                }
                else if (rs2.getString(8).equals("medium")){
                    task.setStatus(Priority.MEDIUM);
                }
                else if (rs2.getString(8).equals("low")){
                    task.setStatus(Priority.LOW);
                }
                else task.setStatus(Priority.UNCATEGORIZED);
            }
            String SQL3 = "SELECT SKILL_ID FROM SKILLS_TASKS WHERE TASK_ID=?";
            PreparedStatement ps3 = connection.prepareStatement(SQL3);
            ps3.setInt(1, id);
            ResultSet rs3 = ps3.executeQuery();
            while (rs3.next()){
                skillIDs.add(rs3.getInt(1));
            }
            List<String> skills = new ArrayList<>();
            for (Integer i : skillIDs){
            String SQL4 = "SELECT NAME FROM SKILLS WHERE ID=?";
            PreparedStatement ps4 = connection.prepareStatement(SQL4);
            ps4.setInt(1, i);
            ResultSet rs4 = ps4.executeQuery();
            while (rs4.next()){
                skills.add(rs4.getString(1));
            }
            task.setSkills(skills);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return task;
    }

    public User showUser(String username) {
        User user = new User();
        List<Integer> skillIDs = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(db_url, SQLusername, pwd)) {
            String SQL = "SELECT * FROM USERS WHERE USERNAME=?;";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                user = new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getInt(8), rs.getString(10), rs.getInt(9), rs.getString(11));
                String SQL1 = "SELECT SKILL_ID FROM SKILLS_USERS WHERE USERNAME=?";
                PreparedStatement ps1 = connection.prepareStatement(SQL1);
                ps1.setString(1, user.getUsername());
                ResultSet rs1 = ps1.executeQuery();
                while (rs1.next()){
                    skillIDs.add(rs1.getInt(1));
                }
                List<String> skills = new ArrayList<>();
                for (Integer i : skillIDs){
                    String SQL2 = "SELECT NAME FROM SKILLS WHERE ID=?";
                    PreparedStatement ps2 = connection.prepareStatement(SQL2);
                    ps2.setInt(1, i);
                    ResultSet rs2 = ps2.executeQuery();
                    while (rs2.next()){
                        skills.add(rs2.getString(1));
                    }
                    user.setSkills(skills);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return user;
    }


    public List<Project> showProjects() {
        List<Project> projects = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(db_url, SQLusername, pwd)) {
            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM PROJECTS";
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                projects.add(new Project(rs.getString(2), rs.getString(3), rs.getString(4), rs.getDouble(5), rs.getDouble(6), rs.getString(7)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return projects;
    }


    public List<Subproject> showSubprojects(String project) {
        List<Subproject> subprojects = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(db_url, SQLusername, pwd)) {
            int id = getProjectID(project);

            String SQL = "SELECT * FROM SUBPROJECTS WHERE PROJECT_ID=? ";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                subprojects.add(new Subproject(rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getDouble(5)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return subprojects;
    }

    public List<Task> showTasks(String project, String subproject) {
        List<Task> tasks = new ArrayList<>();
        Task task = new Task();
        try (Connection connection = DriverManager.getConnection(db_url, SQLusername, pwd)) {
            int subprojectID = getSubprojectID(project, subproject);

            String SQL = "SELECT * FROM TASKS WHERE SUBPROJECT_ID=? ";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, subprojectID);
            ResultSet rs = ps.executeQuery();
            int id = 0;
            while (rs.next()) {
                List<Integer> skillIDs = new ArrayList<>();
                id = rs.getInt(1);
                task = new Task(rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getDouble(5));
                if (rs.getString(7).equals("t")){
                    task.setDone(true);
                }
                if (rs.getString(7).equals("f")) {
                    task.setDone(false);
                }
                if (rs.getString(8).equals("high")){
                    task.setStatus(Priority.HIGH);
                }
                else if (rs.getString(8).equals("medium")){
                    task.setStatus(Priority.MEDIUM);
                }
                else if (rs.getString(8).equals("low")){
                    task.setStatus(Priority.LOW);
                }
                else if (rs.getString(8).equals("uncategorized")){
                task.setStatus(Priority.UNCATEGORIZED);}

                String SQL1 = "SELECT SKILL_ID FROM SKILLS_TASKS WHERE TASK_ID=?";
                PreparedStatement ps1 = connection.prepareStatement(SQL1);
                ps1.setInt(1, id);
                ResultSet rs1 = ps1.executeQuery();
                while (rs1.next()){
                    skillIDs.add(rs1.getInt(1));
                }
                List<String> skills = new ArrayList<>();
                for (Integer i : skillIDs){
                    String SQL2 = "SELECT NAME FROM SKILLS WHERE ID=?";
                    PreparedStatement ps2 = connection.prepareStatement(SQL2);
                    ps2.setInt(1, i);
                    ResultSet rs2 = ps2.executeQuery();
                    while (rs2.next()){
                        skills.add(rs2.getString(1));
                    }
                    task.setSkills(skills);
                }
                tasks.add(task);
            }

            } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tasks;
    }

    public List<String> showSkills(){
        List<String> skills = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(db_url, SQLusername, pwd)) {
            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM SKILLS";
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                skills.add(rs.getString(2));
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return skills;
    }

    public List<String> showStatus(){
        return List.of("HIGH", "MEDIUM", "LOW", "UNCATEGORIZED");
    }

    public List<User> showUsers(){
        User user = new User();

    List<User> users = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(db_url, SQLusername, pwd)) {
            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM USERS";
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                List<Integer> skillIDs = new ArrayList<>();
                user = new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(5), rs.getString(6), rs.getString(7), rs.getInt(8), rs.getInt(9), rs.getString(10), rs.getString(11));
                String SQL1 = "SELECT SKILL_ID FROM SKILLS_USERS WHERE USERNAME=?";
                PreparedStatement ps1 = connection.prepareStatement(SQL1);
                ps1.setString(1, user.getUsername());
                ResultSet rs1 = ps1.executeQuery();
                while (rs1.next()){
                    skillIDs.add(rs1.getInt(1));
                }
                List<String> skills = new ArrayList<>();
                for (Integer i : skillIDs){
                    String SQL2 = "SELECT NAME FROM SKILLS WHERE ID=?";
                    PreparedStatement ps2 = connection.prepareStatement(SQL2);
                    ps2.setInt(1, i);
                    ResultSet rs2 = ps2.executeQuery();
                    while (rs2.next()){
                        skills.add(rs2.getString(1));
                    }
                    user.setSkills(skills);
                }
                users.add(user);
            }

    } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;}

    public List<User> showUsersWithSkill(String skill){
        List<User> usersWithSkill = new ArrayList<>();
        List<User> users = showUsers();
        for (User u : users){
            for (String s : u.getSkills()){
                if(s.equals(skill)){
                    usersWithSkill.add(u);
                }
            }
        }
        return usersWithSkill;
    }

    public List<String> showUserNames(){
        List<User> users = showUsers();
        List<String> userNames = new ArrayList<>();
        for(User u : users){
            userNames.add(u.getUsername());
        }
        return userNames;
    }

        public List<Project> showsProjectsForUser(User user) {
        HashSet<Integer> taskIDS = new HashSet<>();
        HashSet<Integer> subprojectIDS = new HashSet<>();
        HashSet<Integer> projectIDS = new HashSet<>();
        List<Project> projects = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(db_url, SQLusername, pwd)) {
            String SQL = "SELECT TASK_ID FROM TASKS_USERS WHERE USERNAME=?;";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, user.getUsername());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                taskIDS.add(rs.getInt(1));
            }
            for (int i: taskIDS) {
                String SQL1 = "SELECT SUBPROJECT_ID FROM TASKS WHERE ID=?;";
                PreparedStatement ps1 = connection.prepareStatement(SQL1);
                ps1.setInt(1, i);
                ResultSet rs1 = ps1.executeQuery();
                while (rs1.next()) {
                    subprojectIDS.add(rs1.getInt(1));
                }

            }
            for (int i : subprojectIDS) {
                String SQL2 = "SELECT PROJECT_ID FROM SUBPROJECTS WHERE ID=?;";
                PreparedStatement ps2 = connection.prepareStatement(SQL2);
                ps2.setInt(1, i);
                ResultSet rs2 = ps2.executeQuery();
                while (rs2.next()) {
                    projectIDS.add(rs2.getInt(1));
                }
            }
            for (int i : projectIDS) {
                String SQL3 = "SELECT * FROM PROJECTS WHERE ID=?;";
                PreparedStatement ps3 = connection.prepareStatement(SQL3);
                ps3.setInt(1, i);
                ResultSet rs3 = ps3.executeQuery();
                while (rs3.next()) {
                    projects.add(new Project(rs3.getString(2), rs3.getString(3), rs3.getString(4), rs3.getDouble(5), rs3.getDouble(6), rs3.getString(7)));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return projects;
    }

    public List<Project> showProjectsForManagers(User user) {
        List<Project> projects = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(db_url, SQLusername, pwd)) {
            String SQL = "SELECT * FROM PROJECTS WHERE USERNAME=?;";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, user.getUsername());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                projects.add(new Project(rs.getString(2), rs.getString(3), rs.getString(4), rs.getDouble(5), rs.getDouble(6), rs.getString(7)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return projects;
    }

        public List<String> showAssignedUsers(String projectName, String subprojectName, String taskName) {
        List<String> users = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(db_url, SQLusername, pwd)) {
            int subprojectID = getSubprojectID(projectName, subprojectName);
            int taskID = 0;
            String SQL = "SELECT ID FROM TASKS WHERE NAME=? AND SUBPROJECT_ID=?;";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, taskName);
            ps.setInt(2, subprojectID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                taskID = rs.getInt(1);
            }
            String SQL1 = "SELECT USERNAME FROM TASKS_USERS WHERE TASK_ID=?;";
            PreparedStatement ps1 = connection.prepareStatement(SQL1);
            ps1.setInt(1, taskID);
            ResultSet rs1 = ps1.executeQuery();
            while (rs1.next()) {
                users.add(rs1.getString(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }




    public void updateProject(String projectName, Project project) {
        try (Connection connection = DriverManager.getConnection(db_url, SQLusername, pwd)) {
            String SQL = "UPDATE PROJECTS SET NAME=?, DESCRIPTION=?  WHERE NAME=?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, project.getName());
            ps.setString(2, project.getDescription());
            ps.setString(3, projectName);
            int rs = ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateSubproject(String projectName, String subprojectName, Subproject subproject) {
        try (Connection connection = DriverManager.getConnection(db_url, SQLusername, pwd)) {
            int id = getProjectID(projectName);
            String SQL = "UPDATE SUBPROJECTS SET NAME=?, DESCRIPTION=? WHERE NAME=? AND PROJECT_ID=?;";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, subproject.getName());
            ps.setString(2, subproject.getDescription());
            ps.setString(3, subprojectName);
            ps.setInt(4, id);
            int rs = ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateTask(String projectName, String subprojectName, Task task, String taskName) {
        try (Connection connection = DriverManager.getConnection(db_url, SQLusername, pwd)) {
            int projectID = getProjectID(projectName);
            int subprojectID = getSubprojectID(projectName, subprojectName);
            int taskID = 0;
            List<Integer>skillsIDs = new ArrayList<>();
            double estimatedHoursTask = 0;
            String SQL = "SELECT ESTIMATEDHOURS FROM TASKS WHERE NAME=? AND SUBPROJECT_ID=?;";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, taskName);
            ps.setInt(2, subprojectID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                estimatedHoursTask=rs.getDouble(1);
            }
            String SQL20 = "SELECT ID FROM TASKS WHERE NAME=? AND SUBPROJECT_ID=?;";
            PreparedStatement ps20 = connection.prepareStatement(SQL20);
            ps20.setString(1, taskName);
            ps20.setInt(2, subprojectID);
            ResultSet rs20 = ps20.executeQuery();
            while (rs20.next()){
                taskID=rs20.getInt(1);
            }

            String SQL1 = "UPDATE TASKS SET NAME=?, DESCRIPTION=?, ESTIMATEDHOURS=? WHERE NAME=? AND SUBPROJECT_ID=?;";
            PreparedStatement ps1 = connection.prepareStatement(SQL1);
            ps1.setString(1, task.getName());
            ps1.setString(2, task.getDescription());
            ps1.setDouble(3, task.getEstimatedHours());
            ps1.setString(4, taskName);
            ps1.setInt(5, subprojectID);
            int rs1 = ps1.executeUpdate();

            String SQL2 = "SELECT ESTIMATEDHOURS FROM SUBPROJECTS WHERE ID=?;";
            PreparedStatement ps2 = connection.prepareStatement(SQL2);
            ps2.setInt(1, subprojectID);
            ResultSet rs2 = ps2.executeQuery();
            double estimatedHoursSubproject = 0;
            while (rs2.next()) {
                estimatedHoursSubproject = (rs2.getDouble(1));

            }
            double estimatedHoursSubprojectNew = estimatedHoursSubproject - estimatedHoursTask + task.getEstimatedHours();
            String SQL3 = "UPDATE SUBPROJECTS SET ESTIMATEDHOURS=? WHERE NAME=? AND PROJECT_ID=?;";
            PreparedStatement ps3 = connection.prepareStatement(SQL3);
            ps3.setDouble(1, estimatedHoursSubprojectNew);
            ps3.setString(2, subprojectName);
            ps3.setInt(3, projectID);
            int rs3 = ps3.executeUpdate();

            String SQL4 = "SELECT ESTIMATEDHOURS FROM PROJECTS WHERE ID=?;";
            PreparedStatement ps4 = connection.prepareStatement(SQL4);
            ps4.setInt(1, projectID);
            ResultSet rs4 = ps4.executeQuery();
            double estimatedHoursProject = 0;
            while (rs4.next()) {
                estimatedHoursProject = (rs4.getDouble(1));

            }
            double estimatedHoursProjectNew = estimatedHoursProject - estimatedHoursSubproject+estimatedHoursSubprojectNew;

            String SQL5 = "UPDATE PROJECTS SET ESTIMATEDHOURS=? WHERE ID=?;";
            PreparedStatement ps5 = connection.prepareStatement(SQL5);
            ps5.setDouble(1, estimatedHoursProjectNew);
            ps5.setInt(2, projectID);
            int rs5 = ps5.executeUpdate();

            for (String s : task.getSkills()) {
                String SQL6 = "SELECT ID FROM SKILLS WHERE NAME=?;";
                PreparedStatement ps6 = connection.prepareStatement(SQL6);
                ps6.setString(1, s);
                ResultSet rs6 = ps6.executeQuery();
                while (rs6.next()) {
                    skillsIDs.add(rs6.getInt(1));
                }
            }
            String SQL7 = "DELETE FROM SKILLS_TASKS WHERE TASK_ID=?;";
            PreparedStatement ps7 = connection.prepareStatement(SQL7);
            ps7.setInt(1, taskID);
            int rs7 = ps7.executeUpdate();

            for (Integer i : skillsIDs){
                String SQL8 = "Insert into skills_tasks(Skill_ID, task_id) values (?, ?);";
                PreparedStatement ps8 = connection.prepareStatement(SQL8);
                ps8.setInt(1, i);
                ps8.setInt(2, taskID);
                int rs8 = ps8.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateUser(User user){
        try (Connection connection = DriverManager.getConnection(db_url, SQLusername, pwd)) {
            List<Integer> skillsIDs = new ArrayList<>();
            String SQL = "UPDATE USERS SET FIRSTNAME=?, LASTNAME=?, EMAIL=?, STREETNAME=?, STREETNUMBER=?, POSTNUMBER=?, PHONENUMBER=?, CITY=?, COUNTRY=? WHERE USERNAME=?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            System.out.println(user.getUsername());
            System.out.println(user.getStreetNumber());
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getStreetName());
            ps.setString(5, user.getStreetNumber());
            ps.setInt(6, user.getPostNumber());
            ps.setInt(7, user.getPhoneNumber());
            ps.setString(8, user.getCity());
            ps.setString(9, user.getCountry());
            ps.setString(10, user.getUsername());
            int rs = ps.executeUpdate();

            for (String s : user.getSkills()) {
                String SQL1 = "SELECT ID FROM SKILLS WHERE NAME=?;";
                PreparedStatement ps1 = connection.prepareStatement(SQL1);
                ps1.setString(1, s);
                ResultSet rs1 = ps1.executeQuery();
                while (rs1.next()) {
                    skillsIDs.add(rs1.getInt(1));
                }
            }
            String SQL2 = "DELETE FROM SKILLS_USERS WHERE USERNAME=?;";
            PreparedStatement ps2 = connection.prepareStatement(SQL2);
            ps2.setString(1, user.getUsername());
            int rs2 = ps2.executeUpdate();

            for (Integer i : skillsIDs){
                String SQL3 = "Insert into skills_users(Skill_ID, username) values (?, ?);";
                PreparedStatement ps3 = connection.prepareStatement(SQL3);
                ps3.setInt(1, i);
                ps3.setString(2, user.getUsername());
                int rs3 = ps3.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

        public void updateHours(String projectName, String subprojectName, String taskName, Task task) {
        try (Connection connection = DriverManager.getConnection(db_url, SQLusername, pwd)) {
            int projectID = getProjectID(projectName);
            int subprojectID = getSubprojectID(projectName, subprojectName);
            String SQL = "SELECT USEDHOURS FROM TASKS WHERE NAME=? AND SUBPROJECT_ID=?;";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, taskName);
            ps.setInt(2, subprojectID);
            ResultSet rs = ps.executeQuery();
            double usedHoursTask=0;
            while (rs.next()){
                usedHoursTask=rs.getDouble(1);
            }
            String SQL1 = "UPDATE TASKS SET USEDHOURS=? WHERE NAME=? AND SUBPROJECT_ID=?;";
            PreparedStatement ps1 = connection.prepareStatement(SQL1);
            ps1.setDouble(1, task.getUsedHours());
            ps1.setString(2, taskName);
            ps1.setInt(3, subprojectID);
            int rs1 = ps1.executeUpdate();

            String SQL2 = "SELECT USEDHOURS FROM SUBPROJECTS WHERE ID=?;";
            PreparedStatement ps2 = connection.prepareStatement(SQL2);
            ps2.setInt(1, subprojectID);
            ResultSet rs2 = ps2.executeQuery();
            double usedHoursSubproject = 0;
            while (rs2.next()) {
                usedHoursSubproject = (rs2.getDouble(1));

            }
            double usedHoursSubprojectNew = usedHoursSubproject - usedHoursTask+task.getUsedHours();
            String SQL3 = "UPDATE SUBPROJECTS SET USEDHOURS=? WHERE NAME=? AND PROJECT_ID=?;";
            PreparedStatement ps3 = connection.prepareStatement(SQL3);
            ps3.setDouble(1, usedHoursSubprojectNew);
            ps3.setString(2, subprojectName);
            ps3.setInt(3, projectID);
            int rs3 = ps3.executeUpdate();

            String SQL4 = "SELECT USEDHOURS FROM PROJECTS WHERE ID=?;";
            PreparedStatement ps4 = connection.prepareStatement(SQL4);
            ps4.setInt(1, projectID);
            ResultSet rs4 = ps4.executeQuery();
            double usedHoursProject = 0;
            while (rs4.next()) {
                usedHoursProject = (rs4.getDouble(1));

            }
            double usedHoursProjectNew = usedHoursProject - usedHoursSubproject+usedHoursSubprojectNew;

            String SQL5 = "UPDATE PROJECTS SET USEDHOURS=? WHERE ID=?;";
            PreparedStatement ps5 = connection.prepareStatement(SQL5);
            ps5.setDouble(1, usedHoursProjectNew);
            ps5.setInt(2, projectID);
            int rs5 = ps5.executeUpdate();

    } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void finishATask(String projectName, String subprojectName, Task task) {
        int id = getSubprojectID(projectName, subprojectName);
        String isDone = "f";
        try (Connection connection = DriverManager.getConnection(db_url, SQLusername, pwd)) {
            if (task.isDone()){
                isDone="t";
            }
            String SQL = "UPDATE TASKS SET DONE=? WHERE SUBPROJECT_ID=? AND NAME=?;";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, isDone);
            ps.setInt(2, id);
            ps.setString(3, task.getName());
            int rs = ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


public void createUser(User user) {
    try (Connection connection = DriverManager.getConnection(db_url, SQLusername, pwd)) {
        String SQL = "INSERT INTO USERS (username, firstname, lastname, password, email, streetname, streetnumber, postnumber, phonenumber, city, country) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement ps = connection.prepareStatement(SQL);
        ps.setString(1, user.getUsername());
        ps.setString(2, user.getFirstName());
        ps.setString(3, user.getLastName());
        ps.setString(4, user.getPassword());
        ps.setString(5, user.getEmail());
        ps.setString(6, user.getStreetName());
        ps.setString(7, user.getStreetNumber());
        ps.setInt(8, user.getPostNumber());
        ps.setInt(9, user.getPhoneNumber());
        ps.setString(10, user.getCity());
        ps.setString(11, user.getCountry());
        int rs = ps.executeUpdate();
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    createUserSkills(user);
}

    public void createUserSkills(User user) {
        List<Integer> skillIDs = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(db_url, SQLusername, pwd)) {
            for (String s : user.getSkills()) {
                String SQL1 = "SELECT ID FROM SKILLS WHERE NAME=?;";
                PreparedStatement ps1 = connection.prepareStatement(SQL1);
                ps1.setString(1, s);
                ResultSet rs1 = ps1.executeQuery();
                while (rs1.next()) {
                    skillIDs.add(rs1.getInt(1));
                }
            }
            for (Integer i : skillIDs) {
                String SQL2 = "INSERT INTO SKILLS_USERS (Skill_ID, username) values (?, ?);";
                PreparedStatement ps2 = connection.prepareStatement(SQL2);
                ps2.setInt(1, i);
                ps2.setString(2, user.getUsername());
                int rs2 = ps2.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

public boolean checkLogin(User user) {
    try (Connection connection = DriverManager.getConnection(db_url, SQLusername, pwd)) {
        String username = "";
        String password = "";
        String SQL = "SELECT USERNAME, PASSWORD FROM USERS WHERE USERNAME=?;";
        PreparedStatement ps = connection.prepareStatement(SQL);
        ps.setString(1, user.getUsername());
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            username = (rs.getString(1));
            password = (rs.getString(2));
        }
        if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
            return true;
        }

    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    return false;
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

    public boolean checkUsers(String username){
        ArrayList<String>usernames = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(db_url, SQLusername, pwd)) {
            Statement stmt = connection.createStatement();
            String SQL = "SELECT USERNAME FROM USERS;";
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                usernames.add(rs.getString(1));
            }
            for (String s : usernames){
                if (username.equals(s)){
                    return true;
                }
        }
    } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public List<Project> checkStatusProject(List<Project>projects) {
        for (Project p : projects) {
            p.setDone(true);
            List<Subproject> subprojects = showSubprojects(p.getName());
            if (subprojects.size() < 1) {
                p.setDone(false);
            } else {
                for (Subproject s : checkStatusSubproject(p.getName(), subprojects)) {
                    if (!s.isDone()) {
                        p.setDone(false);
                    }
                }
            }
        }

        return projects;
    }

    public List<Subproject> checkStatusSubproject(String projectName, List<Subproject>subprojects){
        for (Subproject s : subprojects) {
            s.setDone(true);
            List<Task> tasks = showTasks(projectName, s.getName());
            if (tasks.size()<1){
                s.setDone(false);

            }
            else{
                for (Task t: tasks){
                    if (!t.isDone()){
                        s.setDone(false);

                    }
                }
            }
        }
        return subprojects;
    }

    public List<Project> showDoneProjects(){
        List<Project> projects = checkStatusProject(showProjects());
        List<Project> doneProjects = new ArrayList<>();
        for (Project p : projects){
            if (p.isDone()){
                doneProjects.add(p);
            }
        }
        return doneProjects;
    }
    public void addUser(String projectName, String subprojectName, String taskName, User user){
        try (Connection connection = DriverManager.getConnection(db_url, SQLusername, pwd)) {
            int subprojectID = getSubprojectID(projectName, subprojectName);
            int taskID = 0;
            String SQL = "SELECT ID FROM TASKS WHERE SUBPROJECT_ID=? AND NAME=?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, subprojectID);
            ps.setString(2, taskName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                taskID = rs.getInt(1);
            }

            String SQL1 = "INSERT INTO TASKS_USERS (task_ID, username) values (?, ?);";
            PreparedStatement ps1 = connection.prepareStatement(SQL1);
            ps1.setInt(1, taskID);
            ps1.setString(2, user.getUsername());
            int rs1 = ps1.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteProject(String projectName){
        int projectID = getProjectID(projectName);
        List<Subproject>subprojects = showSubprojects(projectName);
        for (Subproject s : subprojects){
            deleteSubproject(projectName, s.getName());
        }
        try (Connection connection = DriverManager.getConnection(db_url, SQLusername, pwd)) {
            String SQL = "DELETE FROM PROJECTS WHERE ID=?;";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, projectID);
            int rs = ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void deleteSubproject(String projectName, String subprojectName){
        int projectID = getProjectID(projectName);
        int subprojectID = getSubprojectID(projectName, subprojectName);
        List<Task>tasks = showTasks(projectName, subprojectName);
        for (Task t : tasks){
            deleteTask(projectName, subprojectName, t.getName());
        }
        try (Connection connection = DriverManager.getConnection(db_url, SQLusername, pwd)) {
            String SQL = "DELETE FROM SUBPROJECTS WHERE ID=?;";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, subprojectID);
            int rs = ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteTask(String projectName, String subprojectName, String taskName){
        int subprojectID = getSubprojectID(projectName, subprojectName);
        int taskID = 0;
        try (Connection connection = DriverManager.getConnection(db_url, SQLusername, pwd)) {
            String SQL = "SELECT ID FROM TASKS WHERE NAME=? AND SUBPROJECT_ID=?;";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, taskName);
            ps.setInt(2, subprojectID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                taskID = rs.getInt(1);
            }
            String SQL1 = "DELETE FROM TASKS_USERS WHERE TASK_ID=?;";
            PreparedStatement ps1 = connection.prepareStatement(SQL1);
            ps1.setInt(1, taskID);
            int rs1 = ps1.executeUpdate();

            String SQL3 = "DELETE FROM SKILLS_TASKS WHERE TASK_ID=?;";
            PreparedStatement ps3 = connection.prepareStatement(SQL3);
            ps3.setInt(1, taskID);
            int rs3 = ps3.executeUpdate();

            String SQL2 = "DELETE FROM TASKS WHERE ID=?;";
            PreparedStatement ps2 = connection.prepareStatement(SQL2);
            ps2.setInt(1, taskID);
            int rs2 = ps2.executeUpdate();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteUser(User user){
        try (Connection connection = DriverManager.getConnection(db_url, SQLusername, pwd)) {

            String SQL = "DELETE FROM SKILLS_USERS WHERE USERNAME=?;";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, user.getUsername());
            int rs = ps.executeUpdate();

            String SQL1 = "DELETE FROM TASKS_USERS WHERE USERNAME=?;";
            PreparedStatement ps1 = connection.prepareStatement(SQL1);
            ps1.setString(1, user.getUsername());
            int rs1 = ps1.executeUpdate();

            String SQL2 = "DELETE FROM USERS WHERE USERNAME=?;";
            PreparedStatement ps2 = connection.prepareStatement(SQL2);
            ps2.setString(1, user.getUsername());
            int rs2 = ps2.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public double calculateUserWorkload(String projectName, String subprojectName, Task task){
        int usersAmount = showAssignedUsers(projectName, subprojectName, task.getName()).size();
        double hours = task.getEstimatedHours()-task.getUsedHours();
        return hours/usersAmount;
    }

    public void unassignUser(String projectName, String subprojectName, String taskName, String username){;
        try (Connection connection = DriverManager.getConnection(db_url, SQLusername, pwd)) {
            int subprojectID = getSubprojectID(projectName, subprojectName);
            int taskID = 0;
            String SQL = "SELECT ID FROM TASKS WHERE NAME=? AND SUBPROJECT_ID=?;";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, taskName);
            ps.setInt(2, subprojectID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                taskID = rs.getInt(1);
            }
            String SQL1 = "Delete FROM TASKS_USERS WHERE TASK_ID=? AND USERNAME=?;";
            PreparedStatement ps1 = connection.prepareStatement(SQL1);
            ps1.setInt(1, taskID);
            ps1.setString(2, username);
            int rs1 = ps1.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


}