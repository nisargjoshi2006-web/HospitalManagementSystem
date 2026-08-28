package dao;

import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

    public ResultSet getAllPrescriptions() {

        try {

            Connection con = DBConnection.getConnection();

            String query = "SELECT * FROM Prescriptions";

            PreparedStatement pst =
                    con.prepareStatement(query);

            return pst.executeQuery();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }
    // SEARCH
public ResultSet searchPrescription(int prescriptionId) {
    try {
        Connection con = DBConnection.getConnection();

        String query =
                "SELECT * FROM Prescriptions WHERE prescription_id=?";

        PreparedStatement pst =
                con.prepareStatement(query);

        pst.setInt(1, prescriptionId);

        return pst.executeQuery();

    } catch (Exception e) {
        e.printStackTrace();
    }

    return null;
}
}