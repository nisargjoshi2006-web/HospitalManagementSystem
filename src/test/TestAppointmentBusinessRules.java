package test;

import dao.AppointmentDAO;
import db.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

/** Read-only integration checks for appointment business rules. */
public class TestAppointmentBusinessRules {

    public static void main(String[] args) throws Exception {
        AppointmentDAO appointmentDAO = new AppointmentDAO();

        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement()) {

            try (ResultSet patient = statement.executeQuery(
                    "SELECT patient_id, registration_date FROM Patients " +
                    "WHERE registration_date IS NOT NULL LIMIT 1")) {
                require(patient.next(), "At least one registered patient is required.");
                int patientId = patient.getInt("patient_id");
                LocalDate registrationDate = patient.getDate("registration_date").toLocalDate();

                require(appointmentDAO.isAppointmentDateValid(patientId, registrationDate.toString()),
                        "An appointment on registration day must be valid.");
                require(!appointmentDAO.isAppointmentDateValid(patientId,
                                registrationDate.minusDays(1).toString()),
                        "An appointment before registration must be rejected.");
            }

            try (ResultSet appointment = statement.executeQuery(
                    "SELECT doctor_id, appointment_date, appointment_time FROM Appointments LIMIT 1")) {
                require(appointment.next(), "At least one appointment is required.");
                require(!appointmentDAO.isDoctorAvailable(
                                appointment.getInt("doctor_id"),
                                appointment.getDate("appointment_date").toString(),
                                appointment.getTime("appointment_time").toString(),
                                null),
                        "A duplicate doctor appointment slot must be unavailable.");
            }
        }

        System.out.println("Appointment business-rule tests passed.");
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
