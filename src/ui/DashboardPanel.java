package ui;

import dao.PatientDAO;
import dao.DoctorDAO;
import dao.AppointmentDAO;
import dao.FeedbackDAO;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {

    public DashboardPanel() {

        setLayout(new GridLayout(4, 1, 20, 20));

        PatientDAO patientDAO = new PatientDAO();
        DoctorDAO doctorDAO = new DoctorDAO();
        AppointmentDAO appointmentDAO = new AppointmentDAO();
        FeedbackDAO feedbackDAO = new FeedbackDAO();

        JLabel lblPatients = new JLabel(
                "Total Patients : " +
                        patientDAO.getPatientCount(),
                SwingConstants.CENTER
        );

        JLabel lblDoctors = new JLabel(
                "Total Doctors : " +
                        doctorDAO.getDoctorCount(),
                SwingConstants.CENTER
        );

        JLabel lblAppointments = new JLabel(
                "Total Appointments : " +
                        appointmentDAO.getAppointmentCount(),
                SwingConstants.CENTER
        );

        JLabel lblFeedback = new JLabel(
                "Total Feedback : " +
                        feedbackDAO.getFeedbackCount(),
                SwingConstants.CENTER
        );

        Font f = new Font("Arial", Font.BOLD, 22);

        lblPatients.setFont(f);
        lblDoctors.setFont(f);
        lblAppointments.setFont(f);
        lblFeedback.setFont(f);

        add(lblPatients);
        add(lblDoctors);
        add(lblAppointments);
        add(lblFeedback);
    }
}