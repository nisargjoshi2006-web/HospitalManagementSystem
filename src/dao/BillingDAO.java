package dao;

import db.DBConnection;
import model.Billing;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

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
    public ArrayList<Billing> getAllBills() {

        ArrayList<Billing> list = new ArrayList<>();

        try {

            Connection con = DBConnection.getConnection();

            String query = "SELECT * FROM Billing";

            PreparedStatement pst = con.prepareStatement(query);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                Billing b = new Billing();

                b.setBillId(rs.getInt("bill_id"));
                b.setAppointmentId(rs.getInt("appointment_id"));
                b.setAmount(rs.getDouble("amount"));
                b.setBillDate(rs.getDate("bill_date") != null
                        ? rs.getDate("bill_date").toString() : "");
                b.setPaymentMethod(rs.getString("payment_method"));
                b.setPaymentStatus(rs.getString("payment_status"));

                list.add(b);
            }

            con.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return list;
    }

// SEARCH BILL

public boolean billExists(int billId) {

    try {

        Connection con = DBConnection.getConnection();

        String query =
                "SELECT * FROM Billing WHERE bill_id=?";

        PreparedStatement pst =
                con.prepareStatement(query);

        pst.setInt(1, billId);

        ResultSet rs = pst.executeQuery();

        boolean exists = rs.next();

        con.close();

        return exists;

    } catch(Exception e) {

        e.printStackTrace();
    }

    return false;
}
// COUNT BILLS

public int getBillCount() {

    int count = 0;

    try {

        Connection con = DBConnection.getConnection();

        String query =
                "SELECT COUNT(*) FROM Billing";

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