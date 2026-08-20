package dao;

import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PatientDAO {

    public void addPatient(String name, String gender, int age,String bloodGroup, String contact, String address, String registrationDate1) {

        try {

            Connection con = DBConnection.getConnection();

            String query =
            "INSERT INTO Patients(patient_name, gender, age, blood_group, contact, address, registration_date) VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement pst = con.prepareStatement(query);

            public void addPatient(
                String name,
                String gender,
                int age,
                String bloodGroup,
                String contact,
                String address,
                String registrationDate)
                            
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

    // SELECT
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

    // UPDATE
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
}