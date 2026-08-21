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

    JTable table;
    DefaultTableModel model;

    PrescriptionDAO dao = new PrescriptionDAO();

    public PrescriptionPanel() {

        setLayout(new BorderLayout());

        // FORM PANEL
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));

        formPanel.add(new JLabel("Appointment ID"));
        txtAppointmentId = new JTextField();
        formPanel.add(txtAppointmentId);

        formPanel.add(new JLabel("Diagnosis"));
        txtDiagnosis = new JTextField();
        formPanel.add(txtDiagnosis);

        formPanel.add(new JLabel("Medicine"));
        txtMedicine = new JTextField();
        formPanel.add(txtMedicine);

        formPanel.add(new JLabel("Next Visit Date (YYYY-MM-DD)"));
        txtNextVisit = new JTextField();
        formPanel.add(txtNextVisit);

        formPanel.add(new JLabel("Remarks"));
        txtRemarks = new JTextField();
        formPanel.add(txtRemarks);

        btnAdd = new JButton("Add Prescription");
        formPanel.add(btnAdd);

        btnView = new JButton("View Prescriptions");
        formPanel.add(btnView);

        add(formPanel, BorderLayout.NORTH);

        // TABLE
        model = new DefaultTableModel();

        model.setColumnIdentifiers(
                new String[]{
                        "ID",
                        "Appointment ID",
                        "Diagnosis",
                        "Medicine",
                        "Next Visit",
                        "Remarks"
                }
        );

        table = new JTable(model);
        table.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);

        // ADD BUTTON
        btnAdd.addActionListener(e -> {

            try {

                dao.addPrescription(
                        Integer.parseInt(txtAppointmentId.getText()),
                        txtDiagnosis.getText(),
                        txtMedicine.getText(),
                        txtNextVisit.getText(),
                        txtRemarks.getText()
                );

                JOptionPane.showMessageDialog(
                        null,
                        "Prescription Added Successfully"
                );

                txtAppointmentId.setText("");
                txtDiagnosis.setText("");
                txtMedicine.setText("");
                txtNextVisit.setText("");
                txtRemarks.setText("");

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        null,
                        "Invalid Input"
                );

                ex.printStackTrace();
            }
        });

        // VIEW BUTTON
        btnView.addActionListener(e -> {

            try {

                model.setRowCount(0);

                ResultSet rs = dao.getAllPrescriptions();

                while (rs.next()) {

                    model.addRow(
                            new Object[]{
                                    rs.getInt("prescription_id"),
                                    rs.getInt("appointment_id"),
                                    rs.getString("diagnosis"),
                                    rs.getString("medicine"),
                                    rs.getDate("next_visit_date"),
                                    rs.getString("remarks")
                            }
                    );
                }

            } catch (Exception ex) {

                ex.printStackTrace();
            }
        });
    }
}