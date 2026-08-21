package dao;

import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AppointmentDAO {

    // INSERT
    public void addAppointment(
            int patientId,
            int doctorId,
            String appointmentDate,
            String appointmentTime,
            String roomNumber,
            String status) {

        try {

            Connection con = DBConnection.getConnection();

            String query =
                "INSERT INTO Appointments(patient_id, doctor_id, appointment_date, appointment_time, room_number, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement pst = con.prepareStatement(query);

            pst.setInt(1, patientId);
            pst.setInt(2, doctorId);
            pst.setDate(3, java.sql.Date.valueOf(appointmentDate));
            pst.setTime(4, java.sql.Time.valueOf(appointmentTime));
            pst.setString(5, roomNumber);
            pst.setString(6, status);

            int rows = pst.executeUpdate();

            System.out.println("Rows Inserted = " + rows);

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // VIEW
    public void viewAppointments() {

        try {

            Connection con = DBConnection.getConnection();

            String query = "SELECT * FROM Appointments";

            PreparedStatement pst = con.prepareStatement(query);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                System.out.println(
                    rs.getInt("appointment_id") + " | " +
                    rs.getInt("patient_id") + " | " +
                    rs.getInt("doctor_id") + " | " +
                    rs.getDate("appointment_date") + " | " +
                    rs.getTime("appointment_time") + " | " +
                    rs.getString("room_number") + " | " +
                    rs.getString("status")
                );
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // UPDATE
    public void updateAppointment(int id, String newStatus) {

        try {

            Connection con = DBConnection.getConnection();

            String query =
                "UPDATE Appointments SET status=? WHERE appointment_id=?";

            PreparedStatement pst = con.prepareStatement(query);

            pst.setString(1, newStatus);
            pst.setInt(2, id);

            int rows = pst.executeUpdate();

            System.out.println("Rows Updated = " + rows);

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deleteAppointment(int id) {

        try {

            Connection con = DBConnection.getConnection();

            String query =
                "DELETE FROM Appointments WHERE appointment_id=?";

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