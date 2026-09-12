package ui;

import dao.LabTestDAO;
import dao.PatientDAO;
import dao.DoctorDAO;
import model.LabTest;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

public class LabTestPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JTextField txtTestId;
    private JTextField txtPatientId;
    private JTextField txtDoctorId;
    private JTextField txtTestName;
    private JTextField txtTestDate;
    private JTextField txtCost;
    private JTextField txtResult;
    private JComboBox<String> cmbStatus;

    private JButton btnAdd;
    private JButton btnView;
    private JButton btnUpdate;
    private JButton btnDelete;

    private JTable table;
    private DefaultTableModel tableModel;

    private LabTestDAO dao = new LabTestDAO();
    private PatientDAO patientDAO = new PatientDAO();
    private DoctorDAO doctorDAO = new DoctorDAO();

    public LabTestPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(8, 2, 8, 8));

        txtTestId = new JTextField();
        txtTestId.setEditable(false);
        txtTestId.setBackground(new Color(240, 240, 240));

        txtPatientId = new JTextField();
        txtDoctorId = new JTextField();
        txtTestName = new JTextField();
        txtTestDate = new JTextField(LocalDate.now().toString());
        txtCost = new JTextField();
        txtResult = new JTextField("Pending Analysis");

        cmbStatus = new JComboBox<>(new String[]{"Pending", "Sample Collected", "Completed"});

        txtTestId.setFont(fieldFont);
        txtPatientId.setFont(fieldFont);
        txtDoctorId.setFont(fieldFont);
        txtTestName.setFont(fieldFont);
        txtTestDate.setFont(fieldFont);
        txtCost.setFont(fieldFont);
        txtResult.setFont(fieldFont);
        cmbStatus.setFont(fieldFont);

        JLabel lblId = new JLabel("Test ID (Auto):"); lblId.setFont(labelFont);
        JLabel lblPid = new JLabel("Patient ID:"); lblPid.setFont(labelFont);
        JLabel lblDid = new JLabel("Doctor ID:"); lblDid.setFont(labelFont);
        JLabel lblName = new JLabel("Test Name (CBC, MRI, etc.):"); lblName.setFont(labelFont);
        JLabel lblDate = new JLabel("Date (YYYY-MM-DD):"); lblDate.setFont(labelFont);
        JLabel lblCost = new JLabel("Cost (Rs.):"); lblCost.setFont(labelFont);
        JLabel lblRes = new JLabel("Findings / Result:"); lblRes.setFont(labelFont);
        JLabel lblStat = new JLabel("Status:"); lblStat.setFont(labelFont);

        formPanel.add(lblId); formPanel.add(txtTestId);
        formPanel.add(lblPid); formPanel.add(txtPatientId);
        formPanel.add(lblDid); formPanel.add(txtDoctorId);
        formPanel.add(lblName); formPanel.add(txtTestName);
        formPanel.add(lblDate); formPanel.add(txtTestDate);
        formPanel.add(lblCost); formPanel.add(txtCost);
        formPanel.add(lblRes); formPanel.add(txtResult);
        formPanel.add(lblStat); formPanel.add(cmbStatus);

        // Buttons
        btnAdd = new JButton("Order Test");
        btnView = new JButton("View All");
        btnUpdate = new JButton("Update Result");
        btnDelete = new JButton("Delete Test");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 8));
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnView);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);

        JPanel topContainer = new JPanel(new BorderLayout(5, 5));
        topContainer.add(formPanel, BorderLayout.CENTER);
        topContainer.add(buttonPanel, BorderLayout.SOUTH);
        add(topContainer, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(new String[]{
                "Test ID", "Patient ID", "Doctor ID", "Test Name", "Date", "Cost (Rs.)", "Status", "Result"
        }, 0);
        table = new JTable(tableModel);
        table.setRowHeight(22);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Populate table on select
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                txtTestId.setText(tableModel.getValueAt(row, 0).toString());
                txtPatientId.setText(tableModel.getValueAt(row, 1).toString());
                txtDoctorId.setText(tableModel.getValueAt(row, 2).toString());
                txtTestName.setText(tableModel.getValueAt(row, 3).toString());
                txtTestDate.setText(tableModel.getValueAt(row, 4).toString());
                txtCost.setText(tableModel.getValueAt(row, 5).toString());
                cmbStatus.setSelectedItem(tableModel.getValueAt(row, 6).toString());
                txtResult.setText(tableModel.getValueAt(row, 7) != null ? tableModel.getValueAt(row, 7).toString() : "");
            }
        });

        // Button Actions
        btnView.addActionListener(e -> refreshTable());

        btnAdd.addActionListener(e -> {
            try {
                int pid = Integer.parseInt(txtPatientId.getText().trim());
                int did = Integer.parseInt(txtDoctorId.getText().trim());
                String name = txtTestName.getText().trim();
                String date = txtTestDate.getText().trim();
                BigDecimal cost = new BigDecimal(txtCost.getText().trim());
                String res = txtResult.getText().trim();
                String stat = (String) cmbStatus.getSelectedItem();

                if (!patientDAO.patientExists(pid)) {
                    JOptionPane.showMessageDialog(this, "Patient ID does not exist!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!doctorDAO.doctorExists(did)) {
                    JOptionPane.showMessageDialog(this, "Doctor ID does not exist!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                dao.addLabTest(pid, did, name, date, cost, res, stat);
                JOptionPane.showMessageDialog(this, "Lab Test Ordered Successfully!");
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnUpdate.addActionListener(e -> {
            try {
                int tid = Integer.parseInt(txtTestId.getText().trim());
                String res = txtResult.getText().trim();
                String stat = (String) cmbStatus.getSelectedItem();
                dao.updateTestResult(tid, res, stat);
                JOptionPane.showMessageDialog(this, "Test Result Updated Successfully!");
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Select a test from the table first.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnDelete.addActionListener(e -> {
            try {
                int tid = Integer.parseInt(txtTestId.getText().trim());
                dao.deleteLabTest(tid);
                JOptionPane.showMessageDialog(this, "Lab Test Deleted!");
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Select a test from the table to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        ArrayList<LabTest> list = dao.getAllLabTests();
        for (LabTest t : list) {
            tableModel.addRow(new Object[]{
                    t.getTestId(), t.getPatientId(), t.getDoctorId(), t.getTestName(),
                    t.getTestDate(), t.getCost(), t.getStatus(), t.getResult()
            });
        }
    }
}
