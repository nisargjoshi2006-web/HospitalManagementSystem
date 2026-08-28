package dao;

import java.util.ArrayList;
import model.Appointment;
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

            PreparedStatement pst =
                    con.prepareStatement(query);

            pst.setInt(1, patientId);
            pst.setInt(2, doctorId);
            pst.setDate(3, java.sql.Date.valueOf(appointmentDate));
            pst.setTime(4, java.sql.Time.valueOf(appointmentTime));
            pst.setString(5, roomNumber);
            pst.setString(6, status);

            pst.executeUpdate();

            con.close();

        } catch (Exception e) {

            e.printStackTrace();
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
                        rs.getDate("appointment_date").toString());

                a.setAppointmentTime(
                        rs.getTime("appointment_time").toString());

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
                        rs.getDate("appointment_date").toString());

                a.setAppointmentTime(
                        rs.getTime("appointment_time").toString());

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
    public void updateAppointment(
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

            pst.executeUpdate();

            con.close();

        } catch (Exception e) {

            e.printStackTrace();
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
}