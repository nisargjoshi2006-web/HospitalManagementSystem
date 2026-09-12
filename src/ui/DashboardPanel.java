package ui;

import dao.PatientDAO;
import dao.DoctorDAO;
import dao.AppointmentDAO;
import dao.FeedbackDAO;
import dao.EmergencyDAO;
import dao.BillingDAO;
import dao.PrescriptionDAO;
import dao.LabTestDAO;
import dao.BedAllocationDAO;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class DashboardPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JPanel cardsGrid;
    private JLabel lblDate;

    public DashboardPanel() {

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // Header Panel (Title + Date)
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        JLabel lblTitle = new JLabel("Hospital Management System Dashboard", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 26));

        lblDate = new JLabel("Today's Date: " + LocalDate.now(), SwingConstants.CENTER);
        lblDate.setFont(new Font("Arial", Font.BOLD, 16));
        lblDate.setForeground(Color.DARK_GRAY);

        headerPanel.add(lblTitle);
        headerPanel.add(lblDate);
        add(headerPanel, BorderLayout.NORTH);

        cardsGrid = new JPanel(new GridLayout(5, 2, 12, 12));
        add(new JScrollPane(cardsGrid), BorderLayout.CENTER);

        JButton btnRefresh = new JButton("Refresh Dashboard");
        btnRefresh.setFont(new Font("Arial", Font.BOLD, 14));
        btnRefresh.addActionListener(e -> refreshDashboard());
        JPanel footerPanel = new JPanel();
        footerPanel.add(btnRefresh);
        add(footerPanel, BorderLayout.SOUTH);

        refreshDashboard();
    }

    private void refreshDashboard() {
        cardsGrid.removeAll();

        lblDate.setText("Today's Date: " + LocalDate.now());

        // Fetch counts from DAOs
        PatientDAO patientDAO = new PatientDAO();
        DoctorDAO doctorDAO = new DoctorDAO();
        AppointmentDAO appointmentDAO = new AppointmentDAO();
        EmergencyDAO emergencyDAO = new EmergencyDAO();
        PrescriptionDAO prescriptionDAO = new PrescriptionDAO();
        BillingDAO billingDAO = new BillingDAO();
        FeedbackDAO feedbackDAO = new FeedbackDAO();
        LabTestDAO labTestDAO = new LabTestDAO();
        BedAllocationDAO bedDAO = new BedAllocationDAO();

        int patients = patientDAO.getPatientCount();
        int doctors = doctorDAO.getDoctorCount();
        int appointments = appointmentDAO.getAppointmentCount();
        int emergencies = emergencyDAO.getEmergencyCount();
        int bedsOccupied = bedDAO.getOccupiedBedCount();
        int prescriptions = prescriptionDAO.getPrescriptionCount();
        int pendingLabs = labTestDAO.getPendingTestCount();
        int bills = billingDAO.getBillCount();
        double revenue = billingDAO.getTotalRevenue();
        int feedback = feedbackDAO.getFeedbackCount();

        cardsGrid.add(createMetricCard("👥 Total Patients", String.valueOf(patients), new Color(230, 242, 255)));
        cardsGrid.add(createMetricCard("🩺 Total Doctors", String.valueOf(doctors), new Color(235, 250, 235)));
        cardsGrid.add(createMetricCard("📅 Appointments Scheduled", String.valueOf(appointments), new Color(255, 245, 230)));
        cardsGrid.add(createMetricCard("🚨 Emergency Cases", String.valueOf(emergencies), new Color(255, 235, 235)));
        cardsGrid.add(createMetricCard("🛏️ Inpatient Beds Occupied", String.valueOf(bedsOccupied), new Color(240, 248, 255)));
        cardsGrid.add(createMetricCard("💊 Prescriptions Issued", String.valueOf(prescriptions), new Color(245, 235, 255)));
        cardsGrid.add(createMetricCard("🧪 Pending Lab Tests", String.valueOf(pendingLabs), new Color(255, 240, 245)));
        cardsGrid.add(createMetricCard("🧾 Invoices & Bills", String.valueOf(bills), new Color(240, 255, 240)));
        cardsGrid.add(createMetricCard("💰 Total Revenue Collected", String.format("₹ %.2f", revenue), new Color(255, 255, 225)));
        cardsGrid.add(createMetricCard("⭐ Patient Reviews", String.valueOf(feedback), new Color(240, 240, 255)));

        cardsGrid.revalidate();
        cardsGrid.repaint();
    }

    private JPanel createMetricCard(String title, String value, Color bgColor) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitle.setForeground(new Color(60, 60, 60));

        JLabel lblValue = new JLabel(value, SwingConstants.RIGHT);
        lblValue.setFont(new Font("Arial", Font.BOLD, 20));
        lblValue.setForeground(new Color(20, 20, 20));

        card.add(lblTitle, BorderLayout.WEST);
        card.add(lblValue, BorderLayout.EAST);
        return card;
    }
}