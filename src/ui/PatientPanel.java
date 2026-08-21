package ui;

import dao.PatientDAO;
import model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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


        // ================= PATIENT NAME =================

        JLabel lblName =
                new JLabel("Patient Name");

        lblName.setFont(labelFont);

        formPanel.add(lblName);

        txtName =
                new JTextField();

        txtName.setFont(fieldFont);

        formPanel.add(txtName);


        // ================= GENDER =================

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


        // ================= AGE =================

        JLabel lblAge =
                new JLabel("Age");

        lblAge.setFont(labelFont);

        formPanel.add(lblAge);

        txtAge =
                new JTextField();

        txtAge.setFont(fieldFont);

        formPanel.add(txtAge);


        // ================= BLOOD GROUP =================

        JLabel lblBlood =
                new JLabel("Blood Group");

        lblBlood.setFont(labelFont);

        formPanel.add(lblBlood);

        txtBlood =
                new JTextField();

        txtBlood.setFont(fieldFont);

        formPanel.add(txtBlood);


        // ================= CONTACT =================

        JLabel lblContact =
                new JLabel("Contact");

        lblContact.setFont(labelFont);

        formPanel.add(lblContact);

        txtContact =
                new JTextField();

        txtContact.setFont(fieldFont);

        formPanel.add(txtContact);


        // ================= ADDRESS =================

        JLabel lblAddress =
                new JLabel("Address");

        lblAddress.setFont(labelFont);

        formPanel.add(lblAddress);

        txtAddress =
                new JTextField();

        txtAddress.setFont(fieldFont);

        formPanel.add(txtAddress);


        // ================= REGISTRATION DATE =================

        JLabel lblDate =
                new JLabel(
                        "Registration Date (YYYY-MM-DD)"
                );

        lblDate.setFont(labelFont);

        formPanel.add(lblDate);

        txtDate =
                new JTextField();

        txtDate.setFont(fieldFont);

        formPanel.add(txtDate);


        // ================= BUTTON PANEL =================

        JPanel buttonPanel =
                new JPanel(
                        new GridLayout(1, 2, 10, 10)
                );

        buttonPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        5, 10, 10, 10
                )
        );


        btnAdd =
                new JButton("Add Patient");

        btnAdd.setFont(buttonFont);


        btnView =
                new JButton("View Patients");

        btnView.setFont(buttonFont);


        buttonPanel.add(btnAdd);
        buttonPanel.add(btnView);


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


        // ================= ADD PATIENT =================

        btnAdd.addActionListener(e -> {

            try {

                // ================= GET INPUT =================

                String name =
                        txtName
                                .getText()
                                .trim();

                String gender =
                        genderBox
                                .getSelectedItem()
                                .toString();

                String ageText =
                        txtAge
                                .getText()
                                .trim();

                String blood =
                        txtBlood
                                .getText()
                                .trim()
                                .toUpperCase();

                String contact =
                        txtContact
                                .getText()
                                .trim();

                String address =
                        txtAddress
                                .getText()
                                .trim();

                String date =
                        txtDate
                                .getText()
                                .trim();


                // ================= NAME VALIDATION =================

                if (name.isEmpty()) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Please enter patient name.",
                            "Validation Error",
                            JOptionPane.WARNING_MESSAGE
                    );

                    txtName.requestFocus();
                    return;
                }


                if (!name.matches("[a-zA-Z ]+")) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Patient name should contain only letters.",
                            "Validation Error",
                            JOptionPane.WARNING_MESSAGE
                    );

                    txtName.requestFocus();
                    return;
                }


                // ================= AGE VALIDATION =================

                if (ageText.isEmpty()) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Please enter patient age.",
                            "Validation Error",
                            JOptionPane.WARNING_MESSAGE
                    );

                    txtAge.requestFocus();
                    return;
                }


                int age;

                try {

                    age = Integer.parseInt(ageText);

                } catch (NumberFormatException ex) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Age must be a valid number.",
                            "Validation Error",
                            JOptionPane.WARNING_MESSAGE
                    );

                    txtAge.requestFocus();
                    return;
                }


                if (age < 1 || age > 120) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Age must be between 1 and 120.",
                            "Validation Error",
                            JOptionPane.WARNING_MESSAGE
                    );

                    txtAge.requestFocus();
                    return;
                }


                // ================= BLOOD GROUP VALIDATION =================

                String[] validBloodGroups = {
                        "A+",
                        "A-",
                        "B+",
                        "B-",
                        "AB+",
                        "AB-",
                        "O+",
                        "O-"
                };

                boolean validBlood = false;

                for (String group : validBloodGroups) {

                    if (blood.equals(group)) {

                        validBlood = true;
                        break;
                    }
                }


                if (!validBlood) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Enter a valid blood group:\n"
                                    + "A+, A-, B+, B-, AB+, AB-, O+, O-",
                            "Validation Error",
                            JOptionPane.WARNING_MESSAGE
                    );

                    txtBlood.requestFocus();
                    return;
                }


                // ================= CONTACT VALIDATION =================

                if (contact.isEmpty()) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Please enter contact number.",
                            "Validation Error",
                            JOptionPane.WARNING_MESSAGE
                    );

                    txtContact.requestFocus();
                    return;
                }


                if (!contact.matches("\\d{10}")) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Contact number must contain exactly 10 digits.",
                            "Validation Error",
                            JOptionPane.WARNING_MESSAGE
                    );

                    txtContact.requestFocus();
                    return;
                }


                // ================= ADDRESS VALIDATION =================

                if (address.isEmpty()) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Please enter patient address.",
                            "Validation Error",
                            JOptionPane.WARNING_MESSAGE
                    );

                    txtAddress.requestFocus();
                    return;
                }


                // ================= DATE VALIDATION =================

                if (date.isEmpty()) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Please enter registration date.",
                            "Validation Error",
                            JOptionPane.WARNING_MESSAGE
                    );

                    txtDate.requestFocus();
                    return;
                }


                if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Date must be in YYYY-MM-DD format.",
                            "Validation Error",
                            JOptionPane.WARNING_MESSAGE
                    );

                    txtDate.requestFocus();
                    return;
                }


                // Check whether date is actually valid

                SimpleDateFormat dateFormat =
                        new SimpleDateFormat("yyyy-MM-dd");

                dateFormat.setLenient(false);

                try {

                    dateFormat.parse(date);

                } catch (ParseException ex) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Please enter a valid calendar date.",
                            "Validation Error",
                            JOptionPane.WARNING_MESSAGE
                    );

                    txtDate.requestFocus();
                    return;
                }


                // ================= ADD TO DATABASE =================

                dao.addPatient(
                        name,
                        gender,
                        age,
                        blood,
                        contact,
                        address,
                        date
                );


                // ================= SUCCESS MESSAGE =================

                JOptionPane.showMessageDialog(
                        this,
                        "Patient Added Successfully",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );


                // ================= CLEAR FIELDS =================

                txtName.setText("");

                genderBox.setSelectedIndex(0);

                txtAge.setText("");

                txtBlood.setText("");

                txtContact.setText("");

                txtAddress.setText("");

                txtDate.setText("");


                txtName.requestFocus();


            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Unable to add patient.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );

                ex.printStackTrace();
            }
        });


        // ================= VIEW PATIENTS =================

        btnView.addActionListener(e -> {

            try {

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


            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Unable to load patients",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );

                ex.printStackTrace();
            }
        });
    }
}