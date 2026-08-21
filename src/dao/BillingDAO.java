package dao;

import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BillingDAO {

    // INSERT
    public void addBill(
            int appointmentId,
            String billDate,
            String paymentMethod,
            String paymentStatus) {

        try {

            Connection con = DBConnection.getConnection();

            // Get consultation fee of the doctor for this appointment
            String feeQuery =
                "SELECT d.consultation_fee " +
                "FROM Appointments a " +
                "JOIN Doctor d ON a.doctor_id = d.doctor_id " +
                "WHERE a.appointment_id = ?";

            PreparedStatement feePst =
                con.prepareStatement(feeQuery);

            feePst.setInt(1, appointmentId);

            ResultSet rs = feePst.executeQuery();

            if (rs.next()) {

                double amount = rs.getDouble("consultation_fee");

                String query =
                    "INSERT INTO Billing(appointment_id, amount, bill_date, payment_method, payment_status) " +
                    "VALUES (?, ?, ?, ?, ?)";

                PreparedStatement pst =
                    con.prepareStatement(query);

                pst.setInt(1, appointmentId);
                pst.setDouble(2, amount);
                pst.setDate(3, java.sql.Date.valueOf(billDate));
                pst.setString(4, paymentMethod);
                pst.setString(5, paymentStatus);

                int rows = pst.executeUpdate();

                System.out.println("Bill Amount = " + amount);
                System.out.println("Rows Inserted = " + rows);
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // VIEW
    public void viewBills() {

        try {

            Connection con = DBConnection.getConnection();

            String query = "SELECT * FROM Billing";

            PreparedStatement pst =
                con.prepareStatement(query);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                System.out.println(
                    rs.getInt("bill_id") + " | " +
                    rs.getInt("appointment_id") + " | " +
                    rs.getDouble("amount") + " | " +
                    rs.getDate("bill_date") + " | " +
                    rs.getString("payment_method") + " | " +
                    rs.getString("payment_status")
                );
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // UPDATE
    public void updateBill(
            int id,
            String newPaymentMethod,
            String newPaymentStatus) {

        try {

            Connection con = DBConnection.getConnection();

            String query =
                "UPDATE Billing SET payment_method=?, payment_status=? WHERE bill_id=?";

            PreparedStatement pst =
                con.prepareStatement(query);

            pst.setString(1, newPaymentMethod);
            pst.setString(2, newPaymentStatus);
            pst.setInt(3, id);

            int rows = pst.executeUpdate();

            System.out.println("Rows Updated = " + rows);

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deleteBill(int id) {

        try {

            Connection con = DBConnection.getConnection();

            String query =
                "DELETE FROM Billing WHERE bill_id=?";

            PreparedStatement pst =
                con.prepareStatement(query);

            pst.setInt(1, id);

            int rows = pst.executeUpdate();

            System.out.println("Rows Deleted = " + rows);

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public ResultSet getAllBills() {

    try {

        Connection con = DBConnection.getConnection();

        String query = "SELECT * FROM Billing";

        PreparedStatement pst =
                con.prepareStatement(query);

        return pst.executeQuery();

    }

    catch(Exception e) {

        e.printStackTrace();
    }

    return null;
}
}