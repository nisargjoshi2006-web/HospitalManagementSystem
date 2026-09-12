package ui;

import model.User;

import javax.swing.*;
import java.awt.*;

public class HospitalManagementUI extends JFrame {

    private static final long serialVersionUID = 1L;

    public HospitalManagementUI(User user) {

        setTitle(
                "Hospital Management System — Logged in as: " +
                user.getFullName() + " (" + user.getRole() + ")"
        );

        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ================= TOP APP BAR =================
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(240, 244, 250));
        topBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(210, 215, 225)),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));

        JLabel lblUserInfo = new JLabel(
                "👤 Logged in as: " + user.getFullName() + "  |  Role: " + user.getRole()
        );
        lblUserInfo.setFont(new Font("Arial", Font.BOLD, 13));
        lblUserInfo.setForeground(new Color(40, 50, 70));

        JButton btnLogout = new JButton("🚪 Logout");
        btnLogout.setFont(new Font("Arial", Font.BOLD, 12));
        btnLogout.setBackground(new Color(220, 53, 69));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to log out?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginUI();
            }
        });

        topBar.add(lblUserInfo, BorderLayout.WEST);
        topBar.add(btnLogout, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        // ================= MODULE TABS =================
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
            tabs.add("Reports", new ReportsPanel());

        } else {

            // Receptionist gets limited tabs
            tabs.add("Patients", new PatientPanel());
            tabs.add("Appointments", new AppointmentPanel());
            tabs.add("Billing", new BillingPanel());
        }

        add(tabs, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginUI();
    }
}
