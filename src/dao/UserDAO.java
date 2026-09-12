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

        String sql =
                "SELECT * FROM Users WHERE username=? AND password=SHA2(?, 256)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    user = new User();

                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setFullName(rs.getString("full_name"));
                    user.setRole(rs.getString("role"));
                }
            }

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

        String sql =
                "INSERT INTO Users(username, password, full_name, role) " +
                "VALUES (?, SHA2(?, 256), ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, fullName);
            ps.setString(4, role);

            int rows = ps.executeUpdate();

            System.out.println("Rows Inserted = " + rows);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // CHECK IF USERNAME EXISTS
    public boolean userExists(String username) {

        boolean exists = false;

        String sql =
                "SELECT * FROM Users WHERE username=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    exists = true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return exists;
    }

    // CHANGE PASSWORD WITH SHA-256 VERIFICATION
    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        String verifySql = "SELECT user_id FROM Users WHERE user_id=? AND password=SHA2(?, 256)";
        String updateSql = "UPDATE Users SET password=SHA2(?, 256) WHERE user_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement verifyPs = con.prepareStatement(verifySql)) {
            verifyPs.setInt(1, userId);
            verifyPs.setString(2, oldPassword);
            try (ResultSet rs = verifyPs.executeQuery()) {
                if (rs.next()) {
                    try (PreparedStatement updatePs = con.prepareStatement(updateSql)) {
                        updatePs.setString(1, newPassword);
                        updatePs.setInt(2, userId);
                        return updatePs.executeUpdate() > 0;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
