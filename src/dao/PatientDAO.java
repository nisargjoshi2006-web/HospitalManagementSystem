package dao;
import java.util.ArrayList;
import model.Patient;
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

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement("INSERT INTO Patients(patient_name, gender, age, blood_group, contact, address, registration_date) VALUES (?, ?, ?, ?, ?, ?, ?)")) {

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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // VIEW ALL PATIENTS
    public void viewPatients() {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement("SELECT * FROM Patients");
             ResultSet rs = pst.executeQuery()) {

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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // UPDATE PATIENT NAME
   // UPDATE PATIENT
public void updatePatient(
        int id,
        String name,
        String gender,
        int age,
        String bloodGroup,
        String contact,
        String address) {

    try (Connection con = DBConnection.getConnection();
         PreparedStatement pst = con.prepareStatement(
                "UPDATE Patients SET " +
                "patient_name=?, " +
                "gender=?, " +
                "age=?, " +
                "blood_group=?, " +
                "contact=?, " +
                "address=? " +
                "WHERE patient_id=?")) {

        pst.setString(1, name);
        pst.setString(2, gender);
        pst.setInt(3, age);
        pst.setString(4, bloodGroup);
        pst.setString(5, contact);
        pst.setString(6, address);
        pst.setInt(7, id);

        int rows = pst.executeUpdate();

        System.out.println("Rows Updated = " + rows);

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    // DELETE PATIENT
   public void deletePatient(int id) {

    System.out.println("Deleting ID = " + id);

    try (Connection con = DBConnection.getConnection()) {
        con.setAutoCommit(false);
        try {
            String[] queries = {
                "DELETE FROM Bed_Allocation WHERE patient_id=?",
                "DELETE FROM Lab_Tests WHERE patient_id=?",
                "DELETE FROM Prescriptions WHERE appointment_id IN (SELECT appointment_id FROM Appointments WHERE patient_id=?)",
                "DELETE FROM Billing WHERE appointment_id IN (SELECT appointment_id FROM Appointments WHERE patient_id=?)",
                "DELETE FROM Appointments WHERE patient_id=?",
                "DELETE FROM Feedback WHERE patient_id=?",
                "DELETE FROM Emergency WHERE patient_id=?"
            };

            for (String query : queries) {
                try (PreparedStatement pst = con.prepareStatement(query)) {
                    pst.setInt(1, id);
                    pst.executeUpdate();
                }
            }

            try (PreparedStatement pst = con.prepareStatement("DELETE FROM Patients WHERE patient_id=?")) {
                pst.setInt(1, id);
                System.out.println("Rows Deleted = " + pst.executeUpdate());
            }
            con.commit();
        } catch (Exception e) {
            con.rollback();
            e.printStackTrace();
        } finally {
            con.setAutoCommit(true);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    public ArrayList<Patient> getAllPatients() {

    ArrayList<Patient> patients = new ArrayList<>();

    try (Connection con = DBConnection.getConnection();
         PreparedStatement pst = con.prepareStatement("SELECT * FROM Patients");
         ResultSet rs = pst.executeQuery()) {

        while (rs.next()) {

            Patient p = new Patient();

            p.setPatientId(rs.getInt("patient_id"));
            p.setPatientName(rs.getString("patient_name"));
            p.setGender(rs.getString("gender"));
            p.setAge(rs.getInt("age"));
            p.setBloodGroup(rs.getString("blood_group"));
            p.setContact(rs.getString("contact"));
            p.setAddress(rs.getString("address"));
            p.setRegistrationDate(rs.getDate("registration_date") != null ? rs.getDate("registration_date").toString() : "");

            patients.add(p);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return patients;
} 
public Patient searchPatient(int id) {

    Patient p = null;

    try (Connection con = DBConnection.getConnection();
         PreparedStatement pst = con.prepareStatement("SELECT * FROM Patients WHERE patient_id=?")) {

        pst.setInt(1, id);

        try (ResultSet rs = pst.executeQuery()) {
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
                
                p.setRegistrationDate(rs.getDate("registration_date") != null ? rs.getDate("registration_date").toString() : "");
            }
        }

    } catch(Exception e) {
        e.printStackTrace();
    }

    return p;
}
public int getPatientCount() {

    int count = 0;

    try (Connection con = DBConnection.getConnection();
         PreparedStatement pst = con.prepareStatement("SELECT COUNT(*) FROM Patients");
         ResultSet rs = pst.executeQuery()) {

        if(rs.next()) {
            count = rs.getInt(1);
        }

    } catch(Exception e) {
        e.printStackTrace();
    }

    return count;
}
public boolean patientExists(int patientId) {

    boolean exists = false;

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement("SELECT * FROM patients WHERE patient_id=?")) {

        ps.setInt(1, patientId);

        try (ResultSet rs = ps.executeQuery()) {
            if(rs.next()) {
                exists = true;
            }
        }

    } catch(Exception e) {
        e.printStackTrace();
    }

    return exists;
}

    // SMART MULTI-CRITERIA SEARCH (NAME, CONTACT, BLOOD GROUP)
    public ArrayList<Patient> searchPatientsByKeyword(String keyword) {
        ArrayList<Patient> list = new ArrayList<>();
        String sql = "SELECT * FROM Patients WHERE patient_name LIKE ? OR contact LIKE ? OR blood_group = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String pattern = "%" + keyword + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, keyword);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Patient p = new Patient();
                    p.setPatientId(rs.getInt("patient_id"));
                    p.setPatientName(rs.getString("patient_name"));
                    p.setGender(rs.getString("gender"));
                    p.setAge(rs.getInt("age"));
                    p.setBloodGroup(rs.getString("blood_group"));
                    p.setContact(rs.getString("contact"));
                    p.setAddress(rs.getString("address"));
                    p.setRegistrationDate(rs.getDate("registration_date") != null ? rs.getDate("registration_date").toString() : "");
                    list.add(p);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}

