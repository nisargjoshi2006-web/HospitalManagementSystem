package ui;

import dao.PatientDAO;
import dao.DoctorDAO;
import dao.AppointmentDAO;
import dao.FeedbackDAO;
import dao.EmergencyDAO;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class DashboardPanel extends JPanel {

    public DashboardPanel() {

        setLayout(new GridLayout(7, 1, 20, 20));

        PatientDAO patientDAO = new PatientDAO();
        DoctorDAO doctorDAO = new DoctorDAO();
        AppointmentDAO appointmentDAO = new AppointmentDAO();
        FeedbackDAO feedbackDAO = new FeedbackDAO();
        EmergencyDAO emergencyDAO = new EmergencyDAO();

        JLabel lblTitle = new JLabel(
                "Hospital Management System Dashboard",
                SwingConstants.CENTER
        );

        JLabel lblDate = new JLabel(
                "Date : " + LocalDate.now(),
                SwingConstants.CENTER
        );

        JLabel lblPatients = new JLabel(
                "Total Patients : "
                        + patientDAO.getPatientCount(),
                SwingConstants.CENTER
        );

        JLabel lblDoctors = new JLabel(
                "Total Doctors : "
                        + doctorDAO.getDoctorCount(),
                SwingConstants.CENTER
        );

        JLabel lblAppointments = new JLabel(
                "Total Appointments : "
                        + appointmentDAO.getAppointmentCount(),
                SwingConstants.CENTER
        );

        JLabel lblFeedback = new JLabel(
                "Total Feedback : "
                        + feedbackDAO.getFeedbackCount(),
                SwingConstants.CENTER
        );

        JLabel lblEmergency = new JLabel(
                "Total Emergencies : "
                        + emergencyDAO.getEmergencyCount(),
                SwingConstants.CENTER
        );

        Font titleFont =
                new Font("Arial", Font.BOLD, 30);

        Font normalFont =
                new Font("Arial", Font.BOLD, 22);

        lblTitle.setFont(titleFont);

        lblDate.setFont(
                new Font("Arial", Font.BOLD, 18)
        );

        lblPatients.setFont(normalFont);
        lblDoctors.setFont(normalFont);
        lblAppointments.setFont(normalFont);
        lblFeedback.setFont(normalFont);
        lblEmergency.setFont(normalFont);

        add(lblTitle);
        add(lblDate);
        add(lblPatients);
        add(lblDoctors);
        add(lblAppointments);
        add(lblFeedback);
        add(lblEmergency);
    }
}