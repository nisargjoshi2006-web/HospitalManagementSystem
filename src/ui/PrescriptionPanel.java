package ui;

import dao.PrescriptionDAO;
import model.Prescription;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class PrescriptionPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    JTextField txtAppointmentId;
    JTextField txtDiagnosis;
    JTextField txtMedicine;
    JTextField txtNextVisit;
    JTextField txtRemarks;

    JButton btnAdd;
    JButton btnView;
    JButton btnSearch;
    JButton btnUpdate;
    JButton btnDelete;

    JTable table;
    DefaultTableModel model;

    PrescriptionDAO dao = new PrescriptionDAO();

    public PrescriptionPanel() {

        // ================= MAIN PANEL =================

        setLayout(new BorderLayout(10, 10));

        // ================= FONTS =================

        Font labelFont =
                new Font("Arial", Font.BOLD, 16);

        Font fieldFont =
                new Font("Arial", Font.PLAIN, 16);

        Font buttonFont =
                new Font("Arial", Font.BOLD, 16);

        Font tableFont =
                new Font("Arial", Font.PLAIN, 15);

        Font tableHeaderFont =
                new Font("Arial", Font.BOLD, 15);


        // ================= FORM PANEL =================

        JPanel formPanel =
                new JPanel(new GridLayout(5, 2, 10, 10));

        formPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 10, 5, 10
                )
        );


        // ================= APPOINTMENT ID =================

        JLabel lblAppointment =
                new JLabel("Appointment ID");

        lblAppointment.setFont(labelFont);

        formPanel.add(lblAppointment);

        txtAppointmentId =
                new JTextField();

        txtAppointmentId.setFont(fieldFont);

        formPanel.add(txtAppointmentId);


        // ================= DIAGNOSIS =================

        JLabel lblDiagnosis =
                new JLabel("Diagnosis");

        lblDiagnosis.setFont(labelFont);

        formPanel.add(lblDiagnosis);

        txtDiagnosis =
                new JTextField();

        txtDiagnosis.setFont(fieldFont);

        formPanel.add(txtDiagnosis);


        // ================= MEDICINE =================

        JLabel lblMedicine =
                new JLabel("Medicine");

        lblMedicine.setFont(labelFont);

        formPanel.add(lblMedicine);

        txtMedicine =
                new JTextField();

        txtMedicine.setFont(fieldFont);

        formPanel.add(txtMedicine);


        // ================= NEXT VISIT =================

        JLabel lblNextVisit =
                new JLabel(
                        "Next Visit Date (YYYY-MM-DD)"
                );

        lblNextVisit.setFont(labelFont);

        formPanel.add(lblNextVisit);

        txtNextVisit =
                new JTextField();

        txtNextVisit.setFont(fieldFont);

        formPanel.add(txtNextVisit);


        // ================= REMARKS =================

        JLabel lblRemarks =
                new JLabel("Remarks");

        lblRemarks.setFont(labelFont);

        formPanel.add(lblRemarks);

        txtRemarks =
                new JTextField();

        txtRemarks.setFont(fieldFont);

        formPanel.add(txtRemarks);


        // ================= BUTTON PANEL =================

        JPanel buttonPanel =
                new JPanel(new GridLayout(1, 5, 10, 10));

        buttonPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        5, 10, 10, 10
                )
        );

        // ADD BUTTON
        btnAdd = new JButton("Add Prescription");
        btnAdd.setFont(buttonFont);

        // VIEW BUTTON
        btnView = new JButton("View Prescriptions");
        btnView.setFont(buttonFont);

        // SEARCH BUTTON
        btnSearch = new JButton("Search");
        btnSearch.setFont(buttonFont);

        // UPDATE BUTTON
        btnUpdate = new JButton("Update Prescription");
        btnUpdate.setFont(buttonFont);

        // DELETE BUTTON
        btnDelete = new JButton("Delete Prescription");
        btnDelete.setFont(buttonFont);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnView);
        buttonPanel.add(btnSearch);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);

        // ================= TOP PANEL =================

        JPanel topPanel =
                new JPanel(new BorderLayout());

        topPanel.add(
                formPanel,
                BorderLayout.CENTER
        );

        topPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        add(
                topPanel,
                BorderLayout.NORTH
        );


        // ================= TABLE =================

        model =
                new DefaultTableModel();

        model.setColumnIdentifiers(
                new String[]{
    "Prescription ID",
    "Appointment ID",
    "Diagnosis",
    "Medicine",
    "Next Visit",
    "Remarks"
}
        );


        table =
                new JTable(model);

        // Table font

        table.setFont(tableFont);

        // Table row height

        table.setRowHeight(25);

        // Table header font

        table.getTableHeader()
                .setFont(tableHeaderFont);


        JScrollPane scrollPane =
                new JScrollPane(table);

        add(
                scrollPane,
                BorderLayout.CENTER
        );
        table.getSelectionModel()
        .addListSelectionListener(e -> {

    int row = table.getSelectedRow();

    if(row >= 0) {

        txtAppointmentId.setText(
                model.getValueAt(row,1).toString()
        );

        txtDiagnosis.setText(
                model.getValueAt(row,2).toString()
        );

        txtMedicine.setText(
                model.getValueAt(row,3).toString()
        );

        txtNextVisit.setText(
                model.getValueAt(row,4).toString()
        );

        txtRemarks.setText(
                model.getValueAt(row,5).toString()
        );
    }

});


        // ================= ADD PRESCRIPTION =================

        btnAdd.addActionListener(e -> {

            try {

                int appointmentId =
                        Integer.parseInt(
                                txtAppointmentId
                                        .getText()
                                        .trim()
                        );

                String diagnosis =
                        txtDiagnosis
                                .getText()
                                .trim();

                String medicine =
                        txtMedicine
                                .getText()
                                .trim();

                String nextVisit =
                        txtNextVisit
                                .getText()
                                .trim();

                String remarks =
                        txtRemarks
                                .getText()
                                .trim();

                java.time.LocalDate today =
        java.time.LocalDate.now();

java.time.LocalDate nextDate =
        java.time.LocalDate.parse(nextVisit);

if(nextDate.isBefore(today))
{
    JOptionPane.showMessageDialog(
            this,
            "Next Visit Date cannot be before today!"
    );
    return;
}        
                dao.addPrescription(
                        appointmentId,
                        diagnosis,
                        medicine,
                        nextVisit,
                        remarks
                );


                JOptionPane.showMessageDialog(
                        this,
                        "Prescription Added Successfully",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );


                // Clear fields

                txtAppointmentId.setText("");

                txtDiagnosis.setText("");

                txtMedicine.setText("");

                txtNextVisit.setText("");

                txtRemarks.setText("");


            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid Input",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );

                ex.printStackTrace();
            }
        });


        // ================= VIEW PRESCRIPTIONS =================

        btnView.addActionListener(e -> {

            model.setRowCount(0);

            ArrayList<Prescription> list = dao.getAllPrescriptions();

            for (Prescription p : list) {

                model.addRow(
                        new Object[]{
                                p.getPrescriptionId(),
                                p.getAppointmentId(),
                                p.getDiagnosis(),
                                p.getMedicine(),
                                p.getNextVisitDate(),
                                p.getRemarks()
                        }
                );
            }
        });


        // ================= SEARCH PRESCRIPTION =================

        btnSearch.addActionListener(e -> {

            String idStr = JOptionPane.showInputDialog(
                    this,
                    "Enter Prescription ID"
            );

            if (idStr == null || idStr.trim().isEmpty()) return;

            try {

                int id = Integer.parseInt(idStr.trim());

                boolean found = false;

                for (int r = 0; r < model.getRowCount(); r++) {

                    if (Integer.parseInt(model.getValueAt(r, 0).toString()) == id) {

                        table.setRowSelectionInterval(r, r);
                        table.scrollRectToVisible(table.getCellRect(r, 0, true));
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Prescription ID " + id + " not found. Try viewing first."
                    );
                }

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter a valid numeric ID."
                );
            }
        });


        // ================= UPDATE PRESCRIPTION =================

        btnUpdate.addActionListener(e -> {

            int row = table.getSelectedRow();

            if (row == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Select a row first"
                );

                return;
            }

            try {

                int prescriptionId =
                        Integer.parseInt(
                                model.getValueAt(row, 0).toString()
                        );

                int appointmentId =
                        Integer.parseInt(
                                txtAppointmentId.getText().trim()
                        );

                dao.updatePrescription(
                        prescriptionId,
                        appointmentId,
                        txtDiagnosis.getText().trim(),
                        txtMedicine.getText().trim(),
                        txtNextVisit.getText().trim(),
                        txtRemarks.getText().trim()
                );

                JOptionPane.showMessageDialog(
                        this,
                        "Prescription Updated"
                );

                btnView.doClick();

            } catch (Exception ex) {

                ex.printStackTrace();
            }
        });


        // ================= DELETE PRESCRIPTION =================

        btnDelete.addActionListener(e -> {

            int row = table.getSelectedRow();

            if (row == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Select a row first"
                );

                return;
            }

            try {

                int prescriptionId =
                        Integer.parseInt(
                                model.getValueAt(row, 0).toString()
                        );

                dao.deletePrescription(
                        prescriptionId
                );

                JOptionPane.showMessageDialog(
                        this,
                        "Prescription Deleted"
                );

                btnView.doClick();

            } catch (Exception ex) {

                ex.printStackTrace();
            }
        });
    }
}