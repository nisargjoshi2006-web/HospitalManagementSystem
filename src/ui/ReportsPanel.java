package ui;

import db.DBConnection;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/** Displays the aggregate, join, and view queries required for the database demonstration with CSV export. */
public class ReportsPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final JComboBox<String> reportSelector;
    private final DefaultTableModel tableModel;
    private final JLabel statusLabel;

    public ReportsPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Database Reports & Analytical Views", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));

        reportSelector = new JComboBox<>(new String[]{
                "Revenue by Payment Method",
                "Doctor Appointment Workload",
                "Pending Bills",
                "Active Appointments (View)",
                "Hospital Revenue Summary (View)"
        });
        reportSelector.setFont(new Font("Arial", Font.PLAIN, 13));

        JButton refreshButton = new JButton("🔄 Refresh Report");
        refreshButton.setFont(new Font("Arial", Font.BOLD, 12));

        JButton exportCsvButton = new JButton("📥 Export to CSV");
        exportCsvButton.setFont(new Font("Arial", Font.BOLD, 12));
        exportCsvButton.setBackground(new Color(40, 167, 69));
        exportCsvButton.setForeground(Color.WHITE);
        exportCsvButton.setFocusPainted(false);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        controls.add(new JLabel("Select Report:"));
        controls.add(reportSelector);
        controls.add(refreshButton);
        controls.add(exportCsvButton);

        JPanel header = new JPanel(new BorderLayout(5, 10));
        header.add(title, BorderLayout.NORTH);
        header.add(controls, BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();
        JTable table = new JTable(tableModel);
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(24);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        add(new JScrollPane(table), BorderLayout.CENTER);

        statusLabel = new JLabel("Select a report and click Refresh Report.", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        add(statusLabel, BorderLayout.SOUTH);

        refreshButton.addActionListener(event -> loadSelectedReport());
        reportSelector.addActionListener(event -> loadSelectedReport());
        exportCsvButton.addActionListener(event -> exportToCsv());

        loadSelectedReport();
    }

    private void loadSelectedReport() {
        String reportName = (String) reportSelector.getSelectedItem();
        String query = queryFor(reportName);

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            ResultSetMetaData metadata = resultSet.getMetaData();
            int columnCount = metadata.getColumnCount();
            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);

            for (int column = 1; column <= columnCount; column++) {
                tableModel.addColumn(metadata.getColumnLabel(column));
            }

            int rows = 0;
            while (resultSet.next()) {
                Object[] values = new Object[columnCount];
                for (int column = 1; column <= columnCount; column++) {
                    values[column - 1] = resultSet.getObject(column);
                }
                tableModel.addRow(values);
                rows++;
            }
            statusLabel.setText("Loaded " + rows + " row(s) successfully for: " + reportName);
        } catch (Exception exception) {
            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);
            statusLabel.setText("Unable to load report: " + exception.getMessage());
        }
    }

    private void exportToCsv() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "No data to export. Please load a report first.",
                    "Export Warning",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Report as CSV");
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files (*.csv)", "csv"));
        fileChooser.setSelectedFile(new File("hospital_report.csv"));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            // Ensure .csv extension
            if (!fileToSave.getName().toLowerCase().endsWith(".csv")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".csv");
            }

            try (PrintWriter writer = new PrintWriter(new FileWriter(fileToSave))) {
                int colCount = tableModel.getColumnCount();
                int rowCount = tableModel.getRowCount();

                // Write Header row
                for (int i = 0; i < colCount; i++) {
                    writer.print(escapeCsv(tableModel.getColumnName(i)));
                    if (i < colCount - 1) writer.print(",");
                }
                writer.println();

                // Write Data rows
                for (int row = 0; row < rowCount; row++) {
                    for (int col = 0; col < colCount; col++) {
                        Object val = tableModel.getValueAt(row, col);
                        writer.print(escapeCsv(val != null ? val.toString() : ""));
                        if (col < colCount - 1) writer.print(",");
                    }
                    writer.println();
                }

                JOptionPane.showMessageDialog(
                        this,
                        "Report successfully exported to:\n" + fileToSave.getAbsolutePath(),
                        "Export Successful",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Error exporting report: " + ex.getMessage(),
                        "Export Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private String escapeCsv(String data) {
        if (data.contains(",") || data.contains("\"") || data.contains("\n")) {
            data = data.replace("\"", "\"\"");
            return "\"" + data + "\"";
        }
        return data;
    }

    private String queryFor(String reportName) {
        if ("Active Appointments (View)".equals(reportName)) {
            return "SELECT * FROM v_ActiveAppointments";
        }
        if ("Hospital Revenue Summary (View)".equals(reportName)) {
            return "SELECT * FROM v_HospitalRevenueSummary";
        }
        if ("Doctor Appointment Workload".equals(reportName)) {
            return "SELECT d.doctor_id AS Doctor_ID, d.doctor_name AS Doctor_Name, " +
                    "COUNT(a.appointment_id) AS Total_Appointments " +
                    "FROM Doctor d LEFT JOIN Appointments a ON d.doctor_id=a.doctor_id " +
                    "GROUP BY d.doctor_id, d.doctor_name ORDER BY Total_Appointments DESC";
        }
        if ("Pending Bills".equals(reportName)) {
            return "SELECT b.bill_id AS Bill_ID, p.patient_name AS Patient_Name, " +
                    "b.amount AS Amount, b.payment_method AS Payment_Method " +
                    "FROM Billing b JOIN Appointments a ON b.appointment_id=a.appointment_id " +
                    "JOIN Patients p ON a.patient_id=p.patient_id " +
                    "WHERE b.payment_status='Pending' ORDER BY b.bill_date DESC";
        }
        return "SELECT payment_method AS Payment_Method, COUNT(*) AS Transactions, " +
                "SUM(amount) AS Total_Revenue, AVG(amount) AS Average_Bill " +
                "FROM Billing WHERE payment_status='Paid' GROUP BY payment_method " +
                "ORDER BY Total_Revenue DESC";
    }
}
