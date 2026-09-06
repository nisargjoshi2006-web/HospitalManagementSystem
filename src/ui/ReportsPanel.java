package ui;

import db.DBConnection;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/** Displays the aggregate and join queries required for the database demonstration. */
public class ReportsPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final JComboBox<String> reportSelector;
    private final DefaultTableModel tableModel;
    private final JLabel statusLabel;

    public ReportsPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Database Reports", SwingConstants.CENTER);

        reportSelector = new JComboBox<>(new String[]{
                "Revenue by Payment Method",
                "Doctor Appointment Workload",
                "Pending Bills"
        });
        JButton refreshButton = new JButton("Refresh Report");
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER));
        controls.add(reportSelector);
        controls.add(refreshButton);
        JPanel header = new JPanel(new BorderLayout());
        header.add(title, BorderLayout.NORTH);
        header.add(controls, BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();
        JTable table = new JTable(tableModel);
        table.setAutoCreateRowSorter(true);
        add(new JScrollPane(table), BorderLayout.CENTER);

        statusLabel = new JLabel("Select a report and click Refresh Report.", SwingConstants.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        refreshButton.addActionListener(event -> loadSelectedReport());
        reportSelector.addActionListener(event -> loadSelectedReport());
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
            statusLabel.setText(rows + " row(s) loaded.");
        } catch (Exception exception) {
            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);
            statusLabel.setText("Unable to load report: " + exception.getMessage());
        }
    }

    private String queryFor(String reportName) {
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
