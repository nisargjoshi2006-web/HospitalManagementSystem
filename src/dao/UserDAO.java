package dao;

import db.DBConnection;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    // AUTHENTICATE
    public User authenticate(String username, String password) {

        User user = null;

        try {

            Connection con = DBConnection.getConnection();

            String sql =
                    "SELECT * FROM Users WHERE username=? AND password=SHA2(?, 256)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                user = new User();

                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setFullName(rs.getString("full_name"));
                user.setRole(rs.getString("role"));
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    // ADD USER
    public void addUser(
            String username,
            String password,
            String fullName,
            String role) {

        try {

            Connection con = DBConnection.getConnection();

            String sql =
                    "INSERT INTO Users(username, password, full_name, role) " +
                    "VALUES (?, SHA2(?, 256), ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, fullName);
            ps.setString(4, role);

            int rows = ps.executeUpdate();

            System.out.println("Rows Inserted = " + rows);

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // CHECK IF USERNAME EXISTS
    public boolean userExists(String username) {

        boolean exists = false;

        try {

            Connection con = DBConnection.getConnection();

            String sql =
                    "SELECT * FROM Users WHERE username=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                exists = true;
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return exists;
    }
}
