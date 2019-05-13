package com.brodiequinlan.sprintrestrospective.database;

import com.brodiequinlan.sprintrestrospective.SprintRestrospectiveApplication;
import com.brodiequinlan.sprintrestrospective.models.Feature;
import com.brodiequinlan.sprintrestrospective.models.Project;
import com.brodiequinlan.sprintrestrospective.security.Hash;
import com.brodiequinlan.sprintrestrospective.security.Salt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class SqlConnection {
    public static String pepper;
    public static String url;
    public static String username;
    public static String password;
    private final Logger logger = LoggerFactory.getLogger(SqlConnection.class);
    private Connection con = null;

    public SqlConnection() {
        init();
    }

    public void init() {
        try {
            con = DriverManager.getConnection(url, username, password);
        } catch (SQLException ex) {
            logger.error("Error: " + ex.getMessage());
        }

    }

    public void close() {
        try {
            if (con != null) con.close();
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }


    public List<Project> getProjects(String username) {
        List<Project> projects = new ArrayList<>();
        try {

            int uId = GetIdFromUsername(username);

            String query2 = "SELECT Projects.id, Projects.name, Project_Users.user_id, Project_Users.project_id " +
                    "FROM Projects " +
                    "INNER JOIN Project_Users ON Projects.id=Project_Users.project_id " +
                    "WHERE Project_Users.user_id = ?";
            PreparedStatement ps = con.prepareStatement(query2);
            ps.setInt(1, uId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                projects.add(new Project(rs.getString(2), rs.getString(3), rs.getString(1)));
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            logger.error("Error: " + ex.getMessage());
        }
        return projects;
    }

    private int GetIdFromUsername(String username) throws SQLException {
        int uId = -1;
        String query = "select id from Users where name = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            uId = rs.getInt(1);
        }
        ps.close();
        rs.close();
        return uId;
    }

    public int addProject(Project project) {
        try {
            int oId = GetIdFromUsername(project.owner);

            String query2 = "insert into Projects (name, owner_id) values (?,?)";
            PreparedStatement ps = con.prepareStatement(query2, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, project.name);
            ps.setString(2, oId + "");
            ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int pId = generatedKeys.getInt(1);
                    String query3 = "insert into Project_Users (project_id, user_id) values (?,?)";
                    ps = con.prepareStatement(query3);
                    ps.setInt(1, pId);
                    ps.setInt(2, oId);
                    ps.executeUpdate();
                    generatedKeys.close();
                    return pId;
                }
            }
            ps.close();

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
        return -1;
    }

    public boolean validateUser(String username, String password) {
        try {
            String query = "SELECT password, salt FROM Users WHERE name = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                if (rs.getString(1).equals(Hash.sha512(password + rs.getString(2) + pepper))) {
                    stmt.close();
                    return true;
                }
            }
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        return false;
    }

    public String addUser(String username, String password) {
        try {
            String query = "SELECT * FROM Users WHERE name = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return "Account Already Exists";
            }
            rs.close();
            stmt.close();
            String query2 = " insert into Users (name,password,salt) values (?, ?, ?)";
            PreparedStatement preparedStmt = con.prepareStatement(query2);
            String salt = Salt.generateSalt();
            preparedStmt.setString(1, username);
            preparedStmt.setString(2, Hash.sha512(password + salt + pepper));
            preparedStmt.setString(3, salt);
            preparedStmt.execute();
            preparedStmt.close();
            return "success";
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return "Unknown Error! We apologize for the inconvenience.";
    }

    public List<Feature> getFeatures(String projectid) {
        String query = "select * from Features where project_id=?";
        List<Feature> features = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, projectid);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String query2 = "select Users.name from Users inner join Feature_Users on Users.id = Feature_Users.user_id where Feature_Users.feature_id = ?";
                ps = con.prepareStatement(query2);
                ps.setInt(1, rs.getInt(1));
                ResultSet rs2 = ps.executeQuery();
                List<String> users = new ArrayList<>();
                while (rs2.next()) {
                    users.add(rs2.getString(1));
                }
                String id = rs.getString(1);
                String name = rs.getString(2);
                String points = rs.getString(3);
                String requester = rs.getString(4);
                features.add(new Feature(id, name, points, requester, users));
                ps.close();
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return features;
    }

    public void DELETE_ALL() {
        if (!SprintRestrospectiveApplication.DEBUG) {
            logger.warn("Calling function intended for DEBUG only in production code could have unintended consequences!");
        }
        String drop_projects = "delete from Projects where 1";
        String drop_users = "delete from Users where 1";
        String drop_junc = "delete from Project_Users where 1";
        String drop_features = "delete from Features where 1";
        String drop_features_users = "delete from Feature_Users where 1";
        try {
            PreparedStatement ps = con.prepareStatement(drop_features_users);
            ps.executeUpdate();


            ps = con.prepareStatement(drop_features);
            ps.executeUpdate();

            ps = con.prepareStatement(drop_junc);
            ps.executeUpdate();

            ps = con.prepareStatement(drop_projects);
            ps.executeUpdate();

            ps = con.prepareStatement(drop_users);
            ps.executeUpdate();

            ps.close();
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
    }

    //this method exists purely for test case purposes.
    public void RESET_INC() {
        if (!SprintRestrospectiveApplication.DEBUG) {
            logger.warn("Calling function intended for DEBUG only in production code could have unintended consequences!");
        }
        String alterAuto = "ALTER TABLE Users AUTO_INCREMENT = 1;";
        try {
            PreparedStatement ps = con.prepareStatement(alterAuto);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
    }


    public Feature addFeature(Feature feature, String username, String projectId) {
        String uid;
        String fid;
        try {
            String query = "select id from Users where name=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                uid = rs.getString(1);
                feature.requester = uid;
            }
            String query2 = "insert into Features (name, points, requester_id, project_id) values (?,?,?,?)";
            ps = con.prepareStatement(query2, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, feature.name);
            ps.setString(2, feature.points);
            ps.setString(3, feature.requester);
            ps.setString(4, projectId);
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    fid = generatedKeys.getString(1);
                    feature.id = fid;
                }
            }
            ps.close();
            return feature;
        } catch (SQLException ex) {
            feature.id = "-2";
            logger.error(ex.getMessage());
        }
        return feature;
    }
}
