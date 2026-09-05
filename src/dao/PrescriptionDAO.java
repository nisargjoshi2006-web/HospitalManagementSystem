package dao;

import db.DBConnection;
import model.Prescription;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class PrescriptionDAO {

    // INSERT
    public void addPrescription(
            int appointmentId,
            String diagnosis,
            String medicine,
            String nextVisitDate,
            String remarks) {

        try {

            Connection con = DBConnection.getConnection();

            String query =
                "INSERT INTO Prescriptions(appointment_id, diagnosis, medicine, next_visit_date, remarks) " +
                "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement pst = con.prepareStatement(query);

            pst.setInt(1, appointmentId);
            pst.setString(2, diagnosis);
            pst.setString(3, medicine);
            pst.setDate(4, java.sql.Date.valueOf(nextVisitDate));
            pst.setString(5, remarks);

            int rows = pst.executeUpdate();

            System.out.println("Rows Inserted = " + rows);

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // VIEW
    public void viewPrescriptions() {

        try {

            Connection con = DBConnection.getConnection();

            String query = "SELECT * FROM Prescriptions";

            PreparedStatement pst = con.prepareStatement(query);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                System.out.println(
                    rs.getInt("prescription_id") + " | " +
                    rs.getInt("appointment_id") + " | " +
                    rs.getString("diagnosis") + " | " +
                    rs.getString("medicine") + " | " +
                    rs.getDate("next_visit_date") + " | " +
                    rs.getString("remarks")
                );
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // UPDATE (now updates all fields, not just medicine)
    public void updatePrescription(
            int id,
            int appointmentId,
            String diagnosis,
            String medicine,
            String nextVisitDate,
            String remarks) {

        try {

            Connection con = DBConnection.getConnection();

            String query =
                "UPDATE Prescriptions SET appointment_id=?, diagnosis=?, medicine=?, next_visit_date=?, remarks=? WHERE prescription_id=?";

            PreparedStatement pst = con.prepareStatement(query);

            pst.setInt(1, appointmentId);
            pst.setString(2, diagnosis);
            pst.setString(3, medicine);
            pst.setDate(4, java.sql.Date.valueOf(nextVisitDate));
            pst.setString(5, remarks);
            pst.setInt(6, id);

            int rows = pst.executeUpdate();

            System.out.println("Rows Updated = " + rows);

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deletePrescription(int id) {

        try {

            Connection con = DBConnection.getConnection();

            String query =
                "DELETE FROM Prescriptions WHERE prescription_id=?";

            PreparedStatement pst = con.prepareStatement(query);

            pst.setInt(1, id);

            int rows = pst.executeUpdate();

            System.out.println("Rows Deleted = " + rows);

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Prescription> getAllPrescriptions() {

        ArrayList<Prescription> list = new ArrayList<>();

        try {

            Connection con = DBConnection.getConnection();

            String query = "SELECT * FROM Prescriptions";

            PreparedStatement pst = con.prepareStatement(query);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                Prescription p = new Prescription();

                p.setPrescriptionId(rs.getInt("prescription_id"));
                p.setAppointmentId(rs.getInt("appointment_id"));
                p.setDiagnosis(rs.getString("diagnosis"));
                p.setMedicine(rs.getString("medicine"));
                p.setNextVisitDate(rs.getDate("next_visit_date") != null
                        ? rs.getDate("next_visit_date").toString() : "");
                p.setRemarks(rs.getString("remarks"));

                list.add(p);
            }

            con.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return list;
    }

    // SEARCH
    public Prescription searchPrescription(int prescriptionId) {

        Prescription p = null;

        try {

            Connection con = DBConnection.getConnection();

            String query =
                    "SELECT * FROM Prescriptions WHERE prescription_id=?";

            PreparedStatement pst = con.prepareStatement(query);

            pst.setInt(1, prescriptionId);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {

                p = new Prescription();

                p.setPrescriptionId(rs.getInt("prescription_id"));
                p.setAppointmentId(rs.getInt("appointment_id"));
                p.setDiagnosis(rs.getString("diagnosis"));
                p.setMedicine(rs.getString("medicine"));
                p.setNextVisitDate(rs.getDate("next_visit_date") != null
                        ? rs.getDate("next_visit_date").toString() : "");
                p.setRemarks(rs.getString("remarks"));
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return p;
    }

// CHECK PRESCRIPTION EXISTS

public boolean prescriptionExists(int prescriptionId) {

    try {

        Connection con = DBConnection.getConnection();

        String query =
                "SELECT * FROM Prescriptions WHERE prescription_id=?";

        PreparedStatement pst =
                con.prepareStatement(query);

        pst.setInt(1, prescriptionId);

        ResultSet rs = pst.executeQuery();

        boolean exists = rs.next();

        con.close();

        return exists;

    } catch(Exception e) {

        e.printStackTrace();
    }

    return false;
}
// COUNT PRESCRIPTIONS

public int getPrescriptionCount() {

    int count = 0;

    try {

        Connection con = DBConnection.getConnection();

        String query =
                "SELECT COUNT(*) FROM Prescriptions";

        PreparedStatement pst =
                con.prepareStatement(query);

        ResultSet rs = pst.executeQuery();

        if(rs.next()) {

            count = rs.getInt(1);
        }

        con.close();

    } catch(Exception e) {

        e.printStackTrace();
    }

    return count;
}
}