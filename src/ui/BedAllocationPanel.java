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

    private JTextField txtAllocationId;
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

    private BedAllocationDAO dao = new BedAllocationDAO();
    private PatientDAO patientDAO = new PatientDAO();

    public BedAllocationPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 8, 8));

        txtAllocationId = new JTextField();
        txtAllocationId.setEditable(false);
        txtAllocationId.setBackground(new Color(240, 240, 240));

        txtPatientId = new JTextField();
        cmbWardType = new JComboBox<>(new String[]{"General Ward", "ICU", "Private AC Room", "Emergency Ward", "Semi-Private"});
        txtBedNumber = new JTextField();
        txtAdmitDate = new JTextField(LocalDate.now().toString());
        txtDischargeDate = new JTextField(LocalDate.now().toString());
        txtDailyCharge = new JTextField("1500.00");

        txtAllocationId.setFont(fieldFont);
        txtPatientId.setFont(fieldFont);
        cmbWardType.setFont(fieldFont);
        txtBedNumber.setFont(fieldFont);
        txtAdmitDate.setFont(fieldFont);
        txtDischargeDate.setFont(fieldFont);
        txtDailyCharge.setFont(fieldFont);

        JLabel lblId = new JLabel("Allocation ID (Auto):"); lblId.setFont(labelFont);
        JLabel lblPid = new JLabel("Patient ID:"); lblPid.setFont(labelFont);
        JLabel lblWard = new JLabel("Ward Type:"); lblWard.setFont(labelFont);
        JLabel lblBed = new JLabel("Bed Number (e.g., GW-101):"); lblBed.setFont(labelFont);
        JLabel lblAdmit = new JLabel("Admit Date (YYYY-MM-DD):"); lblAdmit.setFont(labelFont);
        JLabel lblDischarge = new JLabel("Discharge Date:"); lblDischarge.setFont(labelFont);
        JLabel lblCharge = new JLabel("Daily Charge (Rs.):"); lblCharge.setFont(labelFont);

        formPanel.add(lblId); formPanel.add(txtAllocationId);
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
                txtAllocationId.setText(tableModel.getValueAt(row, 0).toString());
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
                JOptionPane.showMessageDialog(this, "Patient Admitted & Bed Allocated Successfully!");
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnDischarge.addActionListener(e -> {
            try {
                int aid = Integer.parseInt(txtAllocationId.getText().trim());
                String disDate = txtDischargeDate.getText().trim();
                dao.dischargePatient(aid, disDate);
                JOptionPane.showMessageDialog(this, "Patient Discharged Successfully!");
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Please select an active bed from the table first.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        refreshTable();
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
