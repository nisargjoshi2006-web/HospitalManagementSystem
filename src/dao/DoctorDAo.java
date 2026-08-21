package dao;

import db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DoctorDAO {

    // INSERT

    public void addDoctor(String name,
                          int specializationId,
                          String qualification,
                          double fee,
                          String contact) {

        try {

            Connection con = DBConnection.getConnection();

            String query =
                "INSERT INTO Doctor(doctor_name,specialization_id,qualification,consultation_fee,contact) VALUES(?,?,?,?,?)";

            PreparedStatement pst =
                con.prepareStatement(query);

            pst.setString(1, name);
            pst.setInt(2, specializationId);
            pst.setString(3, qualification);
            pst.setDouble(4, fee);
            pst.setString(5, contact);

            int rows = pst.executeUpdate();

            System.out.println("Rows Inserted = " + rows);

            con.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    // VIEW

    public void viewDoctors() {

        try {

            Connection con = DBConnection.getConnection();

            String query = "SELECT * FROM Doctor";

            PreparedStatement pst =
                con.prepareStatement(query);

            ResultSet rs = pst.executeQuery();

            while(rs.next()) {

                System.out.println(
                    rs.getInt("doctor_id") + " | " +
                    rs.getString("doctor_name") + " | " +
                    rs.getInt("specialization_id") + " | " +
                    rs.getString("qualification") + " | " +
                    rs.getDouble("consultation_fee") + " | " +
                    rs.getString("contact")
                );
            }

            con.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    // UPDATE

    public void updateDoctor(int id, String newName) {

        try {

            Connection con = DBConnection.getConnection();

            String query =
                "UPDATE Doctor SET doctor_name=? WHERE doctor_id=?";

            PreparedStatement pst =
                con.prepareStatement(query);

            pst.setString(1, newName);
            pst.setInt(2, id);

            int rows = pst.executeUpdate();

            System.out.println("Rows Updated = " + rows);

            con.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    // DELETE

    public void deleteDoctor(int id) {

        try {

            Connection con = DBConnection.getConnection();

            String query =
                "DELETE FROM Doctor WHERE doctor_id=?";

            PreparedStatement pst =
                con.prepareStatement(query);

            pst.setInt(1, id);

            int rows = pst.executeUpdate();

            System.out.println("Rows Deleted = " + rows);

            con.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}