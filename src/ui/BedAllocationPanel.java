package ui;

import dao.BedAllocationDAO;
import dao.PatientDAO;
import model.BedAllocation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

public class BedAllocationPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JTextField txtPatientId;
    private JComboBox<String> cmbWardType;
    private JTextField txtBedNumber;
    private JTextField txtAdmitDate;
    private JTextField txtDischargeDate;
    private JTextField txtDailyCharge;

    private JButton btnAdmit;
    private JButton btnView;
    private JButton btnDischarge;

    private JTable table;
    private DefaultTableModel tableModel;
    private int selectedAllocationId = -1;

    private BedAllocationDAO dao = new BedAllocationDAO();
    private PatientDAO patientDAO = new PatientDAO();

    public BedAllocationPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);

        // Form Panel (Clean form without redundant ID box)
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 8, 8));

        txtPatientId = new JTextField();
        cmbWardType = new JComboBox<>(new String[]{"General Ward", "ICU", "Private AC Room", "Emergency Ward", "Semi-Private"});
        txtBedNumber = new JTextField();
        txtAdmitDate = new JTextField(LocalDate.now().toString());
        txtDischargeDate = new JTextField(LocalDate.now().toString());
        txtDailyCharge = new JTextField("1500.00");

        txtPatientId.setFont(fieldFont);
        cmbWardType.setFont(fieldFont);
        txtBedNumber.setFont(fieldFont);
        txtAdmitDate.setFont(fieldFont);
        txtDischargeDate.setFont(fieldFont);
        txtDailyCharge.setFont(fieldFont);

        JLabel lblPid = new JLabel("Patient ID:"); lblPid.setFont(labelFont);
        JLabel lblWard = new JLabel("Ward Type:"); lblWard.setFont(labelFont);
        JLabel lblBed = new JLabel("Bed Number (e.g., GW-101):"); lblBed.setFont(labelFont);
        JLabel lblAdmit = new JLabel("Admit Date (YYYY-MM-DD):"); lblAdmit.setFont(labelFont);
        JLabel lblDischarge = new JLabel("Discharge Date (YYYY-MM-DD):"); lblDischarge.setFont(labelFont);
        JLabel lblCharge = new JLabel("Daily Charge (Rs.):"); lblCharge.setFont(labelFont);

        formPanel.add(lblPid); formPanel.add(txtPatientId);
        formPanel.add(lblWard); formPanel.add(cmbWardType);
        formPanel.add(lblBed); formPanel.add(txtBedNumber);
        formPanel.add(lblAdmit); formPanel.add(txtAdmitDate);
        formPanel.add(lblDischarge); formPanel.add(txtDischargeDate);
        formPanel.add(lblCharge); formPanel.add(txtDailyCharge);

        // Buttons
        btnAdmit = new JButton("Admit Patient");
        btnView = new JButton("View Active Beds");
        btnDischarge = new JButton("Discharge Patient");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 8));
        buttonPanel.add(btnAdmit);
        buttonPanel.add(btnView);
        buttonPanel.add(btnDischarge);

        JPanel topContainer = new JPanel(new BorderLayout(5, 5));
        topContainer.add(formPanel, BorderLayout.CENTER);
        topContainer.add(buttonPanel, BorderLayout.SOUTH);
        add(topContainer, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(new String[]{
                "Allocation ID", "Patient ID", "Ward Type", "Bed Number", "Admit Date", "Daily Charge (Rs.)", "Status"
        }, 0);
        table = new JTable(tableModel);
        table.setRowHeight(22);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Table selection listener
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                selectedAllocationId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                txtPatientId.setText(tableModel.getValueAt(row, 1).toString());
                cmbWardType.setSelectedItem(tableModel.getValueAt(row, 2).toString());
                txtBedNumber.setText(tableModel.getValueAt(row, 3).toString());
                txtAdmitDate.setText(tableModel.getValueAt(row, 4).toString());
                txtDailyCharge.setText(tableModel.getValueAt(row, 5).toString());
            }
        });

        // Actions
        btnView.addActionListener(e -> refreshTable());

        btnAdmit.addActionListener(e -> {
            try {
                int pid = Integer.parseInt(txtPatientId.getText().trim());
                String ward = (String) cmbWardType.getSelectedItem();
                String bed = txtBedNumber.getText().trim();
                String admit = txtAdmitDate.getText().trim();
                BigDecimal charge = new BigDecimal(txtDailyCharge.getText().trim());

                if (!patientDAO.patientExists(pid)) {
                    JOptionPane.showMessageDialog(this, "Patient ID does not exist!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                dao.allocateBed(pid, ward, bed, admit, charge, "Occupied");
                JOptionPane.showMessageDialog(this, "Patient Admitted & Bed Allocated! (ID auto-assigned)");
                clearForm();
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnDischarge.addActionListener(e -> {
            if (selectedAllocationId == -1) {
                JOptionPane.showMessageDialog(this, "Please select an active bed from the table below to discharge.", "Select Bed", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                String disDate = txtDischargeDate.getText().trim();
                dao.dischargePatient(selectedAllocationId, disDate);
                JOptionPane.showMessageDialog(this, "Patient Discharged Successfully!");
                clearForm();
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        refreshTable();
    }

    private void clearForm() {
        selectedAllocationId = -1;
        txtPatientId.setText("");
        txtBedNumber.setText("");
        txtAdmitDate.setText(LocalDate.now().toString());
        txtDischargeDate.setText(LocalDate.now().toString());
        txtDailyCharge.setText("1500.00");
        cmbWardType.setSelectedIndex(0);
        table.clearSelection();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        ArrayList<BedAllocation> list = dao.getActiveAllocations();
        for (BedAllocation b : list) {
            tableModel.addRow(new Object[]{
                    b.getAllocationId(), b.getPatientId(), b.getWardType(),
                    b.getBedNumber(), b.getAdmitDate(), b.getDailyCharge(), b.getStatus()
            });
        }
    }
}
