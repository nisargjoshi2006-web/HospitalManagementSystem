package ui;

import dao.PatientDAO;
import model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class PatientPanel extends JPanel {

    JTextField txtName;
    JComboBox<String> genderBox;
    JTextField txtAge;
    JTextField txtBlood;
    JTextField txtContact;
    JTextField txtAddress;
    JTextField txtDate;

    JButton btnAdd;
    JButton btnView;

    JTable table;
    DefaultTableModel tableModel;

    PatientDAO dao = new PatientDAO();

    public PatientPanel() {

        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(8, 2, 10, 10));

        formPanel.add(new JLabel("Patient Name"));
        txtName = new JTextField();
        formPanel.add(txtName);

        formPanel.add(new JLabel("Gender"));
        genderBox = new JComboBox<>(new String[]{
                "Male",
                "Female",
                "Other"
        });
        formPanel.add(genderBox);

        formPanel.add(new JLabel("Age"));
        txtAge = new JTextField();
        formPanel.add(txtAge);

        formPanel.add(new JLabel("Blood Group"));
        txtBlood = new JTextField();
        formPanel.add(txtBlood);

        formPanel.add(new JLabel("Contact"));
        txtContact = new JTextField();
        formPanel.add(txtContact);

        formPanel.add(new JLabel("Address"));
        txtAddress = new JTextField();
        formPanel.add(txtAddress);

        formPanel.add(new JLabel("Registration Date (YYYY-MM-DD)"));
        txtDate = new JTextField();
        formPanel.add(txtDate);

        btnAdd = new JButton("Add Patient");
        formPanel.add(btnAdd);

        btnView = new JButton("View Patients");
        formPanel.add(btnView);

        add(formPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();

        tableModel.addColumn("ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Gender");
        tableModel.addColumn("Age");
        tableModel.addColumn("Blood Group");
        tableModel.addColumn("Contact");
        tableModel.addColumn("Address");

        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);

        btnAdd.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                try {

                    String name = txtName.getText();
                    String gender = genderBox.getSelectedItem().toString();
                    int age = Integer.parseInt(txtAge.getText());
                    String blood = txtBlood.getText();
                    String contact = txtContact.getText();
                    String address = txtAddress.getText();
                    String date = txtDate.getText();

                    dao.addPatient(
                            name,
                            gender,
                            age,
                            blood,
                            contact,
                            address,
                            date
                    );

                    JOptionPane.showMessageDialog(
                            null,
                            "Patient Added Successfully"
                    );

                    txtName.setText("");
                    genderBox.setSelectedIndex(0);
                    txtAge.setText("");
                    txtBlood.setText("");
                    txtContact.setText("");
                    txtAddress.setText("");
                    txtDate.setText("");

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

                ArrayList<Patient> patients =
                        dao.getAllPatients();

                for (Patient p : patients) {

                    tableModel.addRow(new Object[]{
                            p.getPatientId(),
                            p.getPatientName(),
                            p.getGender(),
                            p.getAge(),
                            p.getBloodGroup(),
                            p.getContact(),
                            p.getAddress()
                    });
                }
            }
        });
    } 
}