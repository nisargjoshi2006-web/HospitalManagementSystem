package ui;

import dao.PatientDAO;
import model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
    JButton btnUpdate;
    JButton btnDelete;
    JButton btnSearch;

    JTable table;
    DefaultTableModel tableModel;

    int selectedPatientId = -1;

    PatientDAO dao = new PatientDAO();

    public PatientPanel() {

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
                new JPanel(
                        new GridLayout(7, 2, 10, 10)
                );

        formPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 10, 5, 10
                )
        );

        JLabel lblName =
                new JLabel("Patient Name");
        lblName.setFont(labelFont);
        formPanel.add(lblName);

        txtName = new JTextField();
        txtName.setFont(fieldFont);
        formPanel.add(txtName);

        JLabel lblGender =
                new JLabel("Gender");
        lblGender.setFont(labelFont);
        formPanel.add(lblGender);

        genderBox =
                new JComboBox<>(
                        new String[]{
                                "Male",
                                "Female",
                                "Other"
                        }
                );
        genderBox.setFont(fieldFont);
        formPanel.add(genderBox);

        JLabel lblAge =
                new JLabel("Age");
        lblAge.setFont(labelFont);
        formPanel.add(lblAge);

        txtAge = new JTextField();
        txtAge.setFont(fieldFont);
        formPanel.add(txtAge);

        JLabel lblBlood =
                new JLabel("Blood Group");
        lblBlood.setFont(labelFont);
        formPanel.add(lblBlood);

        txtBlood = new JTextField();
        txtBlood.setFont(fieldFont);
        formPanel.add(txtBlood);

        JLabel lblContact =
                new JLabel("Contact");
        lblContact.setFont(labelFont);
        formPanel.add(lblContact);

        txtContact = new JTextField();
        txtContact.setFont(fieldFont);
        formPanel.add(txtContact);

        JLabel lblAddress =
                new JLabel("Address");
        lblAddress.setFont(labelFont);
        formPanel.add(lblAddress);

        txtAddress = new JTextField();
        txtAddress.setFont(fieldFont);
        formPanel.add(txtAddress);

        JLabel lblDate =
                new JLabel(
                        "Registration Date (DD-MM-YYYY)"
                );
        lblDate.setFont(labelFont);
        formPanel.add(lblDate);

        txtDate = new JTextField();
        txtDate.setFont(fieldFont);
        formPanel.add(txtDate);

        // ================= BUTTON PANEL =================

        JPanel buttonPanel =
                new JPanel(
                        new GridLayout(1, 5, 10, 10)
                );

        buttonPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        5, 10, 10, 10
                )
        );

        btnAdd =
                new JButton("Add Patient");

        btnView =
                new JButton("View Patients");

        btnUpdate =
                new JButton("Update Patient");

        btnDelete =
                new JButton("Delete Patient");

        btnSearch = new JButton("Search Patient");

        btnAdd.setFont(buttonFont);
        btnView.setFont(buttonFont);
        btnUpdate.setFont(buttonFont);
        btnDelete.setFont(buttonFont);
        btnSearch.setFont(buttonFont);
        

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnView);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnSearch);
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

        tableModel =
                new DefaultTableModel();

        tableModel.setColumnIdentifiers(
                new String[]{
                        "ID",
                        "Name",
                        "Gender",
                        "Age",
                        "Blood Group",
                        "Contact",
                        "Address"
                }
        );

        table =
                new JTable(tableModel);

        table.setFont(tableFont);
        table.setRowHeight(25);

        table.getTableHeader()
                .setFont(tableHeaderFont);

        JScrollPane scrollPane =
                new JScrollPane(table);

        add(
                scrollPane,
                BorderLayout.CENTER
        );

        // ================= TABLE SELECTION =================

        table.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()
                            && table.getSelectedRow() != -1) {

                        int row =
                                table.getSelectedRow();

                        selectedPatientId =
                                Integer.parseInt(
                                        tableModel
                                                .getValueAt(row, 0)
                                                .toString()
                                );

                        txtName.setText(
                                tableModel.getValueAt(row, 1)
                                        .toString()
                        );

                        genderBox.setSelectedItem(
                                tableModel.getValueAt(row, 2)
                                        .toString()
                        );

                        txtAge.setText(
                                tableModel.getValueAt(row, 3)
                                        .toString()
                        );

                        txtBlood.setText(
                                tableModel.getValueAt(row, 4)
                                        .toString()
                        );

                        txtContact.setText(
                                tableModel.getValueAt(row, 5)
                                        .toString()
                        );

                        txtAddress.setText(
                                tableModel.getValueAt(row, 6)
                                        .toString()
                        );
                    }
                });

        // ================= ADD =================

        btnAdd.addActionListener(e -> {

            try {

                dao.addPatient(
                        txtName.getText(),
                        genderBox.getSelectedItem().toString(),
                        Integer.parseInt(txtAge.getText()),
                        txtBlood.getText(),
                        txtContact.getText(),
                        txtAddress.getText(),
                        txtDate.getText()
                );

                JOptionPane.showMessageDialog(
                        this,
                        "Patient Added Successfully"
                );

                btnView.doClick();
                

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid Input"
                );

                ex.printStackTrace();
            }
        });

        // ================= VIEW =================

        btnView.addActionListener(e -> {

            tableModel.setRowCount(0);

            ArrayList<Patient> patients =
                    dao.getAllPatients();

            for (Patient p : patients) {

                tableModel.addRow(
                        new Object[]{
                                p.getPatientId(),
                                p.getPatientName(),
                                p.getGender(),
                                p.getAge(),
                                p.getBloodGroup(),
                                p.getContact(),
                                p.getAddress()
                        }
                );
            }
        });

        // ================= UPDATE =================

        btnUpdate.addActionListener(e -> {

            if (selectedPatientId == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Select a patient first"
                );

                return;
            }

            try {

                dao.updatePatient(
                        selectedPatientId,
                        txtName.getText(),
                        genderBox.getSelectedItem().toString(),
                        Integer.parseInt(txtAge.getText()),
                        txtBlood.getText(),
                        txtContact.getText(),
                        txtAddress.getText()
                );

                JOptionPane.showMessageDialog(
                        this,
                        "Patient Updated Successfully"
                );

                btnView.doClick();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid Input"
                );

                ex.printStackTrace();
            }
        });

        // ================= DELETE =================

        btnDelete.addActionListener(e -> {

            if (selectedPatientId == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Select a patient first"
                );

                return;
            }

            int confirm =
                    JOptionPane.showConfirmDialog(
                            this,
                            "Are you sure you want to delete this patient?",
                            "Confirm Delete",
                            JOptionPane.YES_NO_OPTION
                    );

            if (confirm == JOptionPane.YES_OPTION) {

                dao.deletePatient(selectedPatientId);

                JOptionPane.showMessageDialog(
                        this,
                        "Patient Deleted Successfully"
                );

                btnView.doClick();
                
            }
        });
        btnSearch.addActionListener(e -> {

    String idStr =
            JOptionPane.showInputDialog(
                    this,
                    "Enter Patient ID");

    if(idStr == null)
        return;

    try {

        int id =
                Integer.parseInt(idStr);

        Patient p =
                dao.searchPatient(id);

        if(p != null) {

            txtName.setText(
                    p.getPatientName());

            genderBox.setSelectedItem(
                    p.getGender());

            txtAge.setText(
                    String.valueOf(
                            p.getAge()));

            txtBlood.setText(
                    p.getBloodGroup());

            txtContact.setText(
                    p.getContact());

            txtAddress.setText(
                    p.getAddress());

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Patient Not Found");
        }

    } catch(Exception ex) {

        JOptionPane.showMessageDialog(
                this,
                "Invalid ID");
    }
});

        

        // ================= AUTO LOAD =================

        btnView.doClick();
    }
}