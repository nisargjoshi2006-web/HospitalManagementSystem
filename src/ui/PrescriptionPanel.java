package ui;

import dao.PrescriptionDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;

import java.sql.ResultSet;

public class PrescriptionPanel extends JPanel {

    JTextField txtAppointmentId;
    JTextField txtDiagnosis;
    JTextField txtMedicine;
    JTextField txtNextVisit;
    JTextField txtRemarks;

    JButton btnAdd;
    JButton btnView;

    JTable table;
    DefaultTableModel model;

    PrescriptionDAO dao = new PrescriptionDAO();

    public PrescriptionPanel() {

        setLayout(new GridLayout(7,2,10,10));

        add(new JLabel("Appointment ID"));
        txtAppointmentId = new JTextField();
        add(txtAppointmentId);

        add(new JLabel("Diagnosis"));
        txtDiagnosis = new JTextField();
        add(txtDiagnosis);

        add(new JLabel("Medicine"));
        txtMedicine = new JTextField();
        add(txtMedicine);

        add(new JLabel("Next Visit Date (YYYY-MM-DD)"));
        txtNextVisit = new JTextField();
        add(txtNextVisit);

        add(new JLabel("Remarks"));
        txtRemarks = new JTextField();
        add(txtRemarks);

        btnAdd = new JButton("Add Prescription");
        add(btnAdd);

        btnView = new JButton("View Prescriptions");
        add(btnView);

        model = new DefaultTableModel();

        model.addColumn("ID");
        model.addColumn("Appointment ID");
        model.addColumn("Diagnosis");
        model.addColumn("Medicine");
        model.addColumn("Next Visit");
        model.addColumn("Remarks");

        table = new JTable(model);

        JScrollPane scroll =
                new JScrollPane(table);

        add(scroll);

        btnAdd.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                try {

                    dao.addPrescription(

                            Integer.parseInt(
                                    txtAppointmentId.getText()),

                            txtDiagnosis.getText(),

                            txtMedicine.getText(),

                            txtNextVisit.getText(),

                            txtRemarks.getText()
                    );

                    JOptionPane.showMessageDialog(
                            null,
                            "Prescription Added Successfully"
                    );

                } catch (Exception ex) {

                    ex.printStackTrace();
                }
            }
        });

        btnView.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                try {

                    model.setRowCount(0);

                    ResultSet rs =
                            dao.getAllPrescriptions();

                    while(rs.next()) {

                        model.addRow(new Object[]{

                                rs.getInt("prescription_id"),

                                rs.getInt("appointment_id"),

                                rs.getString("diagnosis"),

                                rs.getString("medicine"),

                                rs.getDate("next_visit_date"),

                                rs.getString("remarks")
                        });
                    }

                } catch(Exception ex) {

                    ex.printStackTrace();
                }
            }
        });
    }
}