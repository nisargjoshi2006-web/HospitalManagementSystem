package ui;

import javax.swing.*;

public class HospitalManagementUI extends JFrame {

    public HospitalManagementUI() {

        setTitle("Hospital Management System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Dashboard", new DashboardPanel());

        tabs.add("Patients", new PatientPanel());
        tabs.add("Doctors", new DoctorPanel());
        tabs.add("Doctor Schedule", new DoctorSchedulePanel());
        tabs.add("Appointments", new AppointmentPanel());
        tabs.add("Prescriptions", new PrescriptionPanel());
        tabs.add("Feedback", new FeedbackPanel());
        tabs.add("Billing", new BillingPanel());
        tabs.add("Emergency", new EmergencyPanel());

        add(tabs);

        setVisible(true);
    }

    public static void main(String[] args) {
        new HospitalManagementUI();
    }
}