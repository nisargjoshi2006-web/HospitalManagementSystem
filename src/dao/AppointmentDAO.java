package dao;

import java.util.ArrayList;
import model.Appointment;
import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AppointmentDAO {

    /** Returns whether the appointment date is on or after the patient's registration date. */
    public boolean isAppointmentDateValid(int patientId, String appointmentDate) {
        String query = "SELECT registration_date FROM Patients WHERE patient_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, patientId);

            try (ResultSet rs = pst.executeQuery()) {
                if (!rs.next()) {
                    return false;
                }

                java.sql.Date registrationDate = rs.getDate("registration_date");
                java.sql.Date scheduledDate = java.sql.Date.valueOf(appointmentDate);
                return registrationDate == null || !scheduledDate.before(registrationDate);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to validate the appointment date", e);
        }
    }

    /** Returns false when the doctor is already booked at the requested time. */
    public boolean isDoctorAvailable(int doctorId, String appointmentDate,
                                     String appointmentTime, Integer excludedAppointmentId) {
        String query = "SELECT COUNT(*) FROM Appointments " +
                "WHERE doctor_id=? AND appointment_date=? AND appointment_time=?" +
                (excludedAppointmentId == null ? "" : " AND appointment_id<>?");

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, doctorId);
            pst.setDate(2, java.sql.Date.valueOf(appointmentDate));
            pst.setTime(3, java.sql.Time.valueOf(appointmentTime));
            if (excludedAppointmentId != null) {
                pst.setInt(4, excludedAppointmentId);
            }

            try (ResultSet rs = pst.executeQuery()) {
                return rs.next() && rs.getInt(1) == 0;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to check doctor availability", e);
        }
    }

    // INSERT
    public boolean addAppointment(
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

            PreparedStatement pst =
                    con.prepareStatement(query);

            pst.setInt(1, patientId);
            pst.setInt(2, doctorId);
            pst.setDate(3, java.sql.Date.valueOf(appointmentDate));
            pst.setTime(4, java.sql.Time.valueOf(appointmentTime));
            pst.setString(5, roomNumber);
            pst.setString(6, status);

            int rows = pst.executeUpdate();

            con.close();

            return rows == 1;

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }

    // VIEW
    public ArrayList<Appointment> getAllAppointments() {

        ArrayList<Appointment> appointmentList =
                new ArrayList<>();

        try {

            Connection con =
                    DBConnection.getConnection();

            String query =
                    "SELECT * FROM Appointments";

            PreparedStatement pst =
                    con.prepareStatement(query);

            ResultSet rs =
                    pst.executeQuery();

            while (rs.next()) {

                Appointment a =
                        new Appointment();

                a.setAppointmentId(
                        rs.getInt("appointment_id"));

                a.setPatientId(
                        rs.getInt("patient_id"));

                a.setDoctorId(
                        rs.getInt("doctor_id"));

                a.setAppointmentDate(
                        rs.getDate("appointment_date") != null
                                ? rs.getDate("appointment_date").toString() : "");

                a.setAppointmentTime(
                        rs.getTime("appointment_time") != null
                                ? rs.getTime("appointment_time").toString() : "");

                a.setRoomNumber(
                        rs.getString("room_number"));

                a.setStatus(
                        rs.getString("status"));

                appointmentList.add(a);
            }

            con.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return appointmentList;
    }

    // SEARCH
    public Appointment searchAppointment(int appointmentId) {

        Appointment a = null;

        try {

            Connection con =
                    DBConnection.getConnection();

            String query =
                    "SELECT * FROM Appointments WHERE appointment_id=?";

            PreparedStatement pst =
                    con.prepareStatement(query);

            pst.setInt(1, appointmentId);

            ResultSet rs =
                    pst.executeQuery();

            if (rs.next()) {

                a = new Appointment();

                a.setAppointmentId(
                        rs.getInt("appointment_id"));

                a.setPatientId(
                        rs.getInt("patient_id"));

                a.setDoctorId(
                        rs.getInt("doctor_id"));

                a.setAppointmentDate(
                        rs.getDate("appointment_date") != null
                                ? rs.getDate("appointment_date").toString() : "");

                a.setAppointmentTime(
                        rs.getTime("appointment_time") != null
                                ? rs.getTime("appointment_time").toString() : "");

                a.setRoomNumber(
                        rs.getString("room_number"));

                a.setStatus(
                        rs.getString("status"));
            }

            con.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return a;
    }
    

    // UPDATE COMPLETE RECORD
    public boolean updateAppointment(
            int appointmentId,
            int patientId,
            int doctorId,
            String appointmentDate,
            String appointmentTime,
            String roomNumber,
            String status) {

        try {

            Connection con =
                    DBConnection.getConnection();

            String query =
                    "UPDATE Appointments SET " +
                    "patient_id=?, " +
                    "doctor_id=?, " +
                    "appointment_date=?, " +
                    "appointment_time=?, " +
                    "room_number=?, " +
                    "status=? " +
                    "WHERE appointment_id=?";

            PreparedStatement pst =
                    con.prepareStatement(query);

            pst.setInt(1, patientId);
            pst.setInt(2, doctorId);
            pst.setDate(3,
                    java.sql.Date.valueOf(appointmentDate));
            pst.setTime(4,
                    java.sql.Time.valueOf(appointmentTime));
            pst.setString(5, roomNumber);
            pst.setString(6, status);
            pst.setInt(7, appointmentId);

            int rows = pst.executeUpdate();

            con.close();

            return rows == 1;

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }

    // DELETE
    public void deleteAppointment(int appointmentId) {

        try {

            Connection con =
                    DBConnection.getConnection();

            String query =
                    "DELETE FROM Appointments WHERE appointment_id=?";

            PreparedStatement pst =
                    con.prepareStatement(query);

            pst.setInt(1, appointmentId);

            pst.executeUpdate();

            con.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    public int getAppointmentCount() {

    int count = 0;

    try {

        Connection con = DBConnection.getConnection();

        String query = "SELECT COUNT(*) FROM Appointments";

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
