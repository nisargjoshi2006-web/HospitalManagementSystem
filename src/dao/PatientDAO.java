package dao;

import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class PatientDAO {

    public void addPatient(String name, String gender, int age) {

        try {
            Connection con = DBConnection.getConnection();

            String query =
                "INSERT INTO Patients(patient_name, gender, age) VALUES (?, ?, ?)";

            PreparedStatement pst = con.prepareStatement(query);

            pst.setString(1, name);
            pst.setString(2, gender);
            pst.setInt(3, age);

            int rows = pst.executeUpdate();
            System.out.println("Rows inserted = " + rows);

            if(rows > 0) {
                System.out.println("Patient Added Successfully");
            } else {
                System.out.println("Insert Failed");
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}