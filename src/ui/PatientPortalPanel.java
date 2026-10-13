package ui;

import db.DBConnection;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

public class PatientPortalPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblPatientInfo;
    private int patientId = 1; // Default patient ID (Amit Verma)

    public PatientPortalPanel(User user) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(248, 250, 252));

        // Header Banner
        JPanel headerPanel = new JPanel(new BorderLayout(10, 5));
        headerPanel.setBackground(new Color(238, 242, 255));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(199, 210, 254), 1),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));

        lblPatientInfo = new JLabel("🧑 Patient Portal — Welcome, " + (user != null ? user.getFullName() : "Patient"));
        lblPatientInfo.setFont(UITheme.FONT_TITLE);
        lblPatientInfo.setForeground(new Color(49, 46, 129));

        JLabel lblSub = new JLabel("View your personal clinical timeline, diagnoses, prescribed medicines, and bill statuses (Powered by MySQL Stored Procedure)");
        lblSub.setFont(UITheme.FONT_SUBTITLE);
        lblSub.setForeground(new Color(79, 70, 229));

        JPanel titleBox = new JPanel(new GridLayout(2, 1, 0, 4));
        titleBox.setOpaque(false);
        titleBox.add(lblPatientInfo);
        titleBox.add(lblSub);

        JButton btnRefresh = new JButton("🔄 Refresh History");
        btnRefresh.setFont(UITheme.FONT_BUTTON);
        btnRefresh.setBackground(new Color(79, 70, 229));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.addActionListener(e -> loadPatientHistory());

        headerPanel.add(titleBox, BorderLayout.WEST);
        headerPanel.add(btnRefresh, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Medical History Table
        String[] cols = {
                "Patient ID", "Patient Name", "Age", "Blood Group",
                "Appointment Date", "Doctor Name", "Diagnosis",
                "Prescribed Medicine", "Bill Amount (Rs.)", "Payment Status"
        };
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        table = new JTable(tableModel);
        UITheme.styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        add(scrollPane, BorderLayout.CENTER);

        // Load data on startup
        loadPatientHistory();
    }

    private void loadPatientHistory() {
        tableModel.setRowCount(0);
        try (Connection con = DBConnection.getConnection();
             CallableStatement cs = con.prepareCall("{CALL sp_GetPatientHistory(?)}")) {

            cs.setInt(1, patientId);
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                            rs.getInt("patient_id"),
                            rs.getString("patient_name"),
                            rs.getInt("age"),
                            rs.getString("blood_group"),
                            rs.getDate("appointment_date"),
                            rs.getString("doctor_name"),
                            rs.getString("diagnosis"),
                            rs.getString("medicine"),
                            rs.getBigDecimal("amount"),
                            rs.getString("payment_status")
                    });
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error executing stored procedure sp_GetPatientHistory: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
