package dao;
import java.util.ArrayList;
import model.Doctor;
import src.model.Patient;
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

   public void updateDoctor(
    int id,
    String name,
    int specializationId,
    String qualification,
    double fee,
    String contact)
{
    try{

        Connection con =
            DBConnection.getConnection();

        String sql =
        "UPDATE doctor SET doctor_name=?, specialization_id=?, qualification=?, consultation_fee=?, contact=? WHERE doctor_id=?";

        PreparedStatement ps =
            con.prepareStatement(sql);

        ps.setString(1,name);
        ps.setInt(2,specializationId);
        ps.setString(3,qualification);
        ps.setDouble(4,fee);
        ps.setString(5,contact);
        ps.setInt(6,id);

        ps.executeUpdate();

        con.close();

    }catch(Exception e){
        e.printStackTrace();
    }
}

    // DELETE

    public void deleteDoctor(int id)
{
    try{

        Connection con =
            DBConnection.getConnection();

        String sql =
            "DELETE FROM doctor WHERE doctor_id=?";

        PreparedStatement ps =
            con.prepareStatement(sql);

        ps.setInt(1,id);

        ps.executeUpdate();

        con.close();

    }catch(Exception e){
        e.printStackTrace();
    }
}
public Patient searchPatient(int id) {

    Patient p = null;

    try {

        Connection con = DBConnection.getConnection();

        String query =
                "SELECT * FROM Patients WHERE patient_id=?";

        PreparedStatement pst =
                con.prepareStatement(query);

        pst.setInt(1, id);

        ResultSet rs = pst.executeQuery();

        if(rs.next()) {

            p = new Patient();

            p.setPatientId(
                    rs.getInt("patient_id"));

            p.setPatientName(
                    rs.getString("patient_name"));

            p.setGender(
                    rs.getString("gender"));

            p.setAge(
                    rs.getInt("age"));

            p.setBloodGroup(
                    rs.getString("blood_group"));

            p.setContact(
                    rs.getString("contact"));

            p.setAddress(
                    rs.getString("address"));
        }

        con.close();

    } catch(Exception e) {
        e.printStackTrace();
    }
    return doctorList;
}
}