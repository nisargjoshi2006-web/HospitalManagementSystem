package ui;

import dao.PrescriptionDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;

public class PrescriptionPanel extends JPanel {

    JTextField txtAppointmentId;
    JTextField txtDiagnosis;
    JTextField txtMedicine;
    JTextField txtNextVisit;
    JTextField txtRemarks;

    JButton btnAdd;
JButton btnView;
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

        btnAdd =
                new JButton("Add Prescription");

        btnAdd.setFont(buttonFont);


        // VIEW BUTTON

        btnView =
                new JButton("View Prescriptions");

        btnView.setFont(buttonFont);
        btnUpdate = new JButton("Update Prescription");
btnUpdate.setFont(buttonFont);

btnDelete = new JButton("Delete Prescription");
btnDelete.setFont(buttonFont);


        buttonPanel.add(btnAdd);
buttonPanel.add(btnView);
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

            try {

                // Clear old rows

                model.setRowCount(0);


                ResultSet rs =
                        dao.getAllPrescriptions();


                while (rs.next()) {

                    model.addRow(
                            new Object[]{

                                    rs.getInt(
                                            "prescription_id"
                                    ),

                                    rs.getInt(
                                            "appointment_id"
                                    ),

                                    rs.getString(
                                            "diagnosis"
                                    ),

                                    rs.getString(
                                            "medicine"
                                    ),

                                    rs.getDate(
                                            "next_visit_date"
                                    ),

                                    rs.getString(
                                            "remarks"
                                    )
                            }
                    );
                }


            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Unable to load prescriptions",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );

                ex.printStackTrace();
            }
        });
        btnUpdate.addActionListener(e -> {

    int row = table.getSelectedRow();

    if(row == -1) {

        JOptionPane.showMessageDialog(
                this,
                "Select a row first"
        );

        return;
    }

    try {

        int prescriptionId =
                Integer.parseInt(
                        model.getValueAt(row,0).toString()
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

    } catch(Exception ex) {

        ex.printStackTrace();

    }

});
btnDelete.addActionListener(e -> {

    int row = table.getSelectedRow();

    if(row == -1) {

        JOptionPane.showMessageDialog(
                this,
                "Select a row first"
        );

        return;
    }

    try {

        int prescriptionId =
                Integer.parseInt(
                        model.getValueAt(row,0).toString()
                );

        dao.deletePrescription(
                prescriptionId
        );

        JOptionPane.showMessageDialog(
                this,
                "Prescription Deleted"
        );

    } catch(Exception ex) {

        ex.printStackTrace();

    }

});
    }
}