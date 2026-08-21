package dao;

import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PatientDAO {

    // INSERT PATIENT
    public void addPatient(
            String name,
            String gender,
            int age,
            String bloodGroup,
            String contact,
            String address,
            String registrationDate) {

        try {

            Connection con = DBConnection.getConnection();

            String query =
                    "INSERT INTO Patients(patient_name, gender, age, blood_group, contact, address, registration_date) VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement pst = con.prepareStatement(query);

            pst.setString(1, name);
            pst.setString(2, gender);
            pst.setInt(3, age);
            pst.setString(4, bloodGroup);
            pst.setString(5, contact);
            pst.setString(6, address);
            pst.setDate(7, java.sql.Date.valueOf(registrationDate));

            int rows = pst.executeUpdate();

            System.out.println("Rows Inserted = " + rows);

            if (rows > 0) {
                System.out.println("Patient Added Successfully");
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // VIEW ALL PATIENTS
    public void viewPatients() {

        try {

            Connection con = DBConnection.getConnection();

            String query = "SELECT * FROM Patients";

            PreparedStatement pst = con.prepareStatement(query);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                System.out.println(
                        rs.getInt("patient_id") + " | " +
                        rs.getString("patient_name") + " | " +
                        rs.getString("gender") + " | " +
                        rs.getInt("age") + " | " +
                        rs.getString("blood_group") + " | " +
                        rs.getString("contact") + " | " +
                        rs.getString("address") + " | " +
                        rs.getDate("registration_date")
                );
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // UPDATE PATIENT NAME
    public void updatePatient(int id, String newName) {

        try {

            Connection con = DBConnection.getConnection();

            String query =
                    "UPDATE Patients SET patient_name=? WHERE patient_id=?";

            PreparedStatement pst = con.prepareStatement(query);

            pst.setString(1, newName);
            pst.setInt(2, id);

            int rows = pst.executeUpdate();

            System.out.println("Rows Updated = " + rows);

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DELETE PATIENT
   public void deletePatient(int id) {

    System.out.println("Deleting ID = " + id);

    try {

        Connection con = DBConnection.getConnection();

        String query =
            "DELETE FROM Patients WHERE patient_id=?";

        PreparedStatement pst = con.prepareStatement(query);

        pst.setInt(1, id);

        int rows = pst.executeUpdate();

        System.out.println("Rows Deleted = " + rows);

        con.close();

    } catch (Exception e) {
        e.printStackTrace();
    }
}
}