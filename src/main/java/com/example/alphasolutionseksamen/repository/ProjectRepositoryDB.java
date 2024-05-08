package com.example.alphasolutionseksamen.repository;

import com.example.alphasolutionseksamen.model.Project;
import com.example.alphasolutionseksamen.model.Subproject;
import com.example.alphasolutionseksamen.model.Task;
import com.example.alphasolutionseksamen.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
@Repository
public class ProjectRepositoryDB {
    @Value("${spring.datasource.url}")
    private String db_url;
    @Value("${spring.datasource.username}")
    private String SQLusername;
    @Value("${spring.datasource.password}")
    private String pwd;

    //ArrayList<Project> projects = new ArrayList<>();

    //ArrayList<User> users = new ArrayList<>();

    public void createProject(Project project) {
        try (Connection connection = DriverManager.getConnection(db_url, SQLusername, pwd)) {
            String SQL = "insert into projects (name, description, deadline, username) values (?, ?, ?, ?);";
            PreparedStatement ps = connection.prepareStatement(SQL);
            //ps.setInt(1, id);
            ps.setString(1, project.getName());
            ps.setString(2, project.getDescription());
            ps.setString(3, project.getDeadline());
            ps.setString(4, project.getUsername());
            int rs = ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void createSubproject(Project project, Subproject subproject) {
        try (Connection connection = DriverManager.getConnection(db_url, SQLusername, pwd)) {
            int id = 0;
            String SQL = "SELECT ID FROM PROJECTS WHERE NAME=?;";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, project.getName());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                id = (rs.getInt(1));
            }
            String SQL1 = "insert into subprojects (name, description, project_id) values (?, ?, ?);";
            PreparedStatement ps1 = connection.prepareStatement(SQL1);
            //ps.setInt(1, id);
            ps1.setString(1, subproject.getName());
            ps1.setString(2, subproject.getDescription());
            ps1.setDouble(3, 0);
            ps1.setDouble(4, 0);
            ps1.setInt(5, id);
            int rs1 = ps1.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createTask(Project project, Subproject subproject, Task task) {
        try (Connection connection = DriverManager.getConnection(db_url, SQLusername, pwd)) {
            int id = 0;
            double estimatedHoursSubproject = 0;
            double estimatedHoursSubprojectNew = 0;
            double estimatedHoursProject = 0;
            double estimatedHoursprojectNew = 0;
            String SQL = "SELECT ID FROM SUBPROJECTS WHERE NAME=?;";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, subproject.getName());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                id = (rs.getInt(1));
            }
            String SQL1 = "insert into tasks (name, description, estimatedhours, subproject_id) values (?, ?, ?, ?);";
            PreparedStatement ps1 = connection.prepareStatement(SQL1);
            ps1.setString(1, task.getName());
            ps1.setString(2, task.getDescription());
            ps1.setDouble(3, task.getEstimatedHours());
            ps1.setInt(4, id);
            int rs1 = ps1.executeUpdate();

            String SQL2 = "SELECT ESTIMATEDHOURS FROM PROJECTS WHERE NAME=?;";
            PreparedStatement ps2 = connection.prepareStatement(SQL2);
            ps2.setString(1, project.getName());
            ResultSet rs2 = ps2.executeQuery();
            while (rs2.next()) {
                estimatedHoursProject = (rs2.getInt(1));
            }
            estimatedHoursprojectNew = estimatedHoursProject + task.getEstimatedHours();

            String SQL3 = "SELECT ESTIMATEDHOURS FROM SUBPROJECTS WHERE NAME=?;";
            PreparedStatement ps3 = connection.prepareStatement(SQL3);
            ps2.setString(1, subproject.getName());
            ResultSet rs3 = ps2.executeQuery();
            while (rs3.next()) {
                estimatedHoursSubproject = (rs3.getInt(1));
            }
            estimatedHoursSubproject = estimatedHoursSubproject + task.getEstimatedHours();

            String SQL4 = "UPDATE PROJECTS SET ESTIMATEDHOURS=? WHERE NAME=?";
            PreparedStatement ps4 = connection.prepareStatement(SQL4);
            ps4.setDouble(1, estimatedHoursprojectNew);
            ps4.setString(2, project.getName());
            int rs4 = ps4.executeUpdate();
            String SQL5 = "UPDATE SUBPROJECTS SET ESTIMATEDHOURS=? WHERE NAME=?";
            PreparedStatement ps5 = connection.prepareStatement(SQL5);
            ps5.setDouble(1, estimatedHoursSubprojectNew);
            ps5.setString(2, subproject.getName());
            int rs5 = ps5.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}


