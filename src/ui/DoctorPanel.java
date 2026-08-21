package ui;

import dao.DoctorDAO;
import model.Doctor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DoctorPanel extends JPanel {

    JTextField txtName;
    JTextField txtSpecializationId;
    JTextField txtQualification;
    JTextField txtFee;
    JTextField txtContact;

    JButton btnAdd;
    JButton btnView;

    JTable table;
    DefaultTableModel tableModel;

    DoctorDAO dao = new DoctorDAO();

    public DoctorPanel() {

        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(6, 2, 10, 10));

        formPanel.add(new JLabel("Doctor Name"));
        txtName = new JTextField();
        formPanel.add(txtName);

        formPanel.add(new JLabel("Specialization ID"));
        txtSpecializationId = new JTextField();
        formPanel.add(txtSpecializationId);

        formPanel.add(new JLabel("Qualification"));
        txtQualification = new JTextField();
        formPanel.add(txtQualification);

        formPanel.add(new JLabel("Consultation Fee"));
        txtFee = new JTextField();
        formPanel.add(txtFee);

        formPanel.add(new JLabel("Contact"));
        txtContact = new JTextField();
        formPanel.add(txtContact);

        btnAdd = new JButton("Add Doctor");
        formPanel.add(btnAdd);

        btnView = new JButton("View Doctors");
        formPanel.add(btnView);

        add(formPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();

        tableModel.addColumn("ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Specialization");
        tableModel.addColumn("Qualification");
        tableModel.addColumn("Fee");
        tableModel.addColumn("Contact");

        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);

        btnAdd.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                try {

                    String name = txtName.getText();
                    int specializationId =
                            Integer.parseInt(txtSpecializationId.getText());

                    String qualification =
                            txtQualification.getText();

                    double fee =
                            Double.parseDouble(txtFee.getText());

                    String contact =
                            txtContact.getText();

                    dao.addDoctor(
                            name,
                            specializationId,
                            qualification,
                            fee,
                            contact
                    );

                    JOptionPane.showMessageDialog(
                            null,
                            "Doctor Added Successfully"
                    );

                    txtName.setText("");
                    txtSpecializationId.setText("");
                    txtQualification.setText("");
                    txtFee.setText("");
                    txtContact.setText("");

                } catch (Exception ex) {

                    JOptionPane.showMessageDialog(
                            null,
                            "Invalid Input"
                    );

                    ex.printStackTrace();
                }
            }
        });

        btnView.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                tableModel.setRowCount(0);

                ArrayList<Doctor> doctors =
                        dao.getAllDoctors();

                for (Doctor d : doctors) {

                    tableModel.addRow(new Object[] {

                            d.getDoctorId(),
                            d.getDoctorName(),
                            d.getSpecializationId(),
                            d.getQualification(),
                            d.getConsultationFee(),
                            d.getContact()

                    });
                }
            }
        });
    }
}