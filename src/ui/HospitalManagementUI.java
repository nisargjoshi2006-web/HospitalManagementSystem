package ui;

import model.User;

import javax.swing.*;

public class HospitalManagementUI extends JFrame {

    private static final long serialVersionUID = 1L;

    public HospitalManagementUI(User user) {

        setTitle(
                "Hospital Management System — Logged in as: " +
                user.getFullName() + " (" + user.getRole() + ")"
        );

        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        tabs.add("Dashboard", new DashboardPanel());

        if (user.getRole().equalsIgnoreCase("Admin")) {

            // Admin gets all tabs
            tabs.add("Patients", new PatientPanel());
            tabs.add("Doctors", new DoctorPanel());
            tabs.add("Doctor Schedule", new DoctorSchedulePanel());
            tabs.add("Appointments", new AppointmentPanel());
            tabs.add("Prescriptions", new PrescriptionPanel());
            tabs.add("Feedback", new FeedbackPanel());
            tabs.add("Billing", new BillingPanel());
            tabs.add("Emergency", new EmergencyPanel());

        } else {

            // Receptionist gets limited tabs
            tabs.add("Patients", new PatientPanel());
            tabs.add("Appointments", new AppointmentPanel());
            tabs.add("Billing", new BillingPanel());
        }

        add(tabs);

        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginUI();
    }
}