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
    private int selectedTestId = -1;

    private LabTestDAO dao = new LabTestDAO();
    private PatientDAO patientDAO = new PatientDAO();
    private DoctorDAO doctorDAO = new DoctorDAO();

    public LabTestPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);

        // Form Panel (Clean form without redundant ID field)
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 8, 8));

        txtPatientId = new JTextField();
        txtDoctorId = new JTextField();
        txtTestName = new JTextField();
        txtTestDate = new JTextField(LocalDate.now().toString());
        txtCost = new JTextField();
        txtResult = new JTextField("Pending Analysis");
        cmbStatus = new JComboBox<>(new String[]{"Pending", "Sample Collected", "Completed"});

        txtPatientId.setFont(fieldFont);
        txtDoctorId.setFont(fieldFont);
        txtTestName.setFont(fieldFont);
        txtTestDate.setFont(fieldFont);
        txtCost.setFont(fieldFont);
        txtResult.setFont(fieldFont);
        cmbStatus.setFont(fieldFont);

        JLabel lblPid = new JLabel("Patient ID:"); lblPid.setFont(labelFont);
        JLabel lblDid = new JLabel("Doctor ID:"); lblDid.setFont(labelFont);
        JLabel lblName = new JLabel("Test Name (CBC, MRI, etc.):"); lblName.setFont(labelFont);
        JLabel lblDate = new JLabel("Test Date (YYYY-MM-DD):"); lblDate.setFont(labelFont);
        JLabel lblCost = new JLabel("Test Cost (Rs.):"); lblCost.setFont(labelFont);
        JLabel lblRes = new JLabel("Findings / Result:"); lblRes.setFont(labelFont);
        JLabel lblStat = new JLabel("Status:"); lblStat.setFont(labelFont);

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

        // Select row from table to populate form for update/delete
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                Object val0 = tableModel.getValueAt(row, 0);
                selectedTestId = Integer.parseInt(val0 != null ? val0.toString() : "-1");
                
                Object val1 = tableModel.getValueAt(row, 1);
                txtPatientId.setText(val1 != null ? val1.toString() : "");
                
                Object val2 = tableModel.getValueAt(row, 2);
                txtDoctorId.setText(val2 != null ? val2.toString() : "");
                
                Object val3 = tableModel.getValueAt(row, 3);
                txtTestName.setText(val3 != null ? val3.toString() : "");
                
                Object val4 = tableModel.getValueAt(row, 4);
                txtTestDate.setText(val4 != null ? val4.toString() : "");
                
                Object val5 = tableModel.getValueAt(row, 5);
                txtCost.setText(val5 != null ? val5.toString() : "");
                
                Object val6 = tableModel.getValueAt(row, 6);
                cmbStatus.setSelectedItem(val6 != null ? val6.toString() : "");
                
                Object val7 = tableModel.getValueAt(row, 7);
                txtResult.setText(val7 != null ? val7.toString() : "");
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
                JOptionPane.showMessageDialog(this, "Lab Test Ordered Successfully! (ID auto-assigned)");
                clearForm();
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnUpdate.addActionListener(e -> {
            if (selectedTestId == -1) {
                JOptionPane.showMessageDialog(this, "Please select a test from the table below to update.", "Select Test", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                String res = txtResult.getText().trim();
                String stat = (String) cmbStatus.getSelectedItem();
                dao.updateTestResult(selectedTestId, res, stat);
                JOptionPane.showMessageDialog(this, "Test Result Updated Successfully!");
                clearForm();
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnDelete.addActionListener(e -> {
            if (selectedTestId == -1) {
                JOptionPane.showMessageDialog(this, "Please select a test from the table below to delete.", "Select Test", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete Test #" + selectedTestId + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dao.deleteLabTest(selectedTestId);
                JOptionPane.showMessageDialog(this, "Lab Test Record Deleted!");
                clearForm();
                refreshTable();
            }
        });

        refreshTable();
    }

    private void clearForm() {
        selectedTestId = -1;
        txtPatientId.setText("");
        txtDoctorId.setText("");
        txtTestName.setText("");
        txtTestDate.setText(LocalDate.now().toString());
        txtCost.setText("");
        txtResult.setText("Pending Analysis");
        cmbStatus.setSelectedIndex(0);
        table.clearSelection();
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
