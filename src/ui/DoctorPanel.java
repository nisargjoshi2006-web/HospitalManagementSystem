package ui;

import dao.DoctorDAO;
import model.Doctor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
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
                        new GridLayout(5, 2, 10, 10)
                );

        formPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 10, 5, 10
                )
        );


        // ================= DOCTOR NAME =================

        JLabel lblName =
                new JLabel("Doctor Name");

        lblName.setFont(labelFont);

        formPanel.add(lblName);

        txtName =
                new JTextField();

        txtName.setFont(fieldFont);

        formPanel.add(txtName);


        // ================= SPECIALIZATION =================

        JLabel lblSpecialization =
                new JLabel("Specialization ID");

        lblSpecialization.setFont(labelFont);

        formPanel.add(lblSpecialization);

        txtSpecializationId =
                new JTextField();

        txtSpecializationId.setFont(fieldFont);

        formPanel.add(txtSpecializationId);


        // ================= QUALIFICATION =================

        JLabel lblQualification =
                new JLabel("Qualification");

        lblQualification.setFont(labelFont);

        formPanel.add(lblQualification);

        txtQualification =
                new JTextField();

        txtQualification.setFont(fieldFont);

        formPanel.add(txtQualification);


        // ================= CONSULTATION FEE =================

        JLabel lblFee =
                new JLabel("Consultation Fee");

        lblFee.setFont(labelFont);

        formPanel.add(lblFee);

        txtFee =
                new JTextField();

        txtFee.setFont(fieldFont);

        formPanel.add(txtFee);


        // ================= CONTACT =================

        JLabel lblContact =
                new JLabel("Contact");

        lblContact.setFont(labelFont);

        formPanel.add(lblContact);

        txtContact =
                new JTextField();

        txtContact.setFont(fieldFont);

        formPanel.add(txtContact);


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


        // ================= ADD BUTTON =================

        btnAdd =
                new JButton("Add Doctor");

        btnAdd.setFont(buttonFont);


        // ================= VIEW BUTTON =================

        btnView =
                new JButton("View Doctors");

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
                        "Specialization",
                        "Qualification",
                        "Fee",
                        "Contact"
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


        // =====================================================
        // ================= ADD DOCTOR ========================
        // =====================================================

        btnAdd.addActionListener(e -> {

            try {

                // ================= GET INPUT =================

                String name =
                        txtName
                                .getText()
                                .trim();

                String specializationText =
                        txtSpecializationId
                                .getText()
                                .trim();

                String qualification =
                        txtQualification
                                .getText()
                                .trim();

                String feeText =
                        txtFee
                                .getText()
                                .trim();

                String contact =
                        txtContact
                                .getText()
                                .trim();


                // ================= NAME VALIDATION =================

                if (name.isEmpty()) {

                    showValidationError(
                            "Please enter doctor name."
                    );

                    txtName.requestFocus();

                    return;
                }

                if (!name.matches("[a-zA-Z ]+")) {

                    showValidationError(
                            "Doctor name should contain only letters and spaces."
                    );

                    txtName.requestFocus();

                    return;
                }


                // ================= SPECIALIZATION ID =================

                if (specializationText.isEmpty()) {

                    showValidationError(
                            "Please enter specialization ID."
                    );

                    txtSpecializationId.requestFocus();

                    return;
                }

                int specializationId;

                try {

                    specializationId =
                            Integer.parseInt(
                                    specializationText
                            );

                } catch (NumberFormatException ex) {

                    showValidationError(
                            "Specialization ID must be a valid number."
                    );

                    txtSpecializationId.requestFocus();

                    return;
                }

                if (specializationId <= 0) {

                    showValidationError(
                            "Specialization ID must be greater than 0."
                    );

                    txtSpecializationId.requestFocus();

                    return;
                }


                // ================= QUALIFICATION =================

                if (qualification.isEmpty()) {

                    showValidationError(
                            "Please enter doctor qualification."
                    );

                    txtQualification.requestFocus();

                    return;
                }


                // ================= FEE VALIDATION =================

                if (feeText.isEmpty()) {

                    showValidationError(
                            "Please enter consultation fee."
                    );

                    txtFee.requestFocus();

                    return;
                }

                double fee;

                try {

                    fee =
                            Double.parseDouble(
                                    feeText
                            );

                } catch (NumberFormatException ex) {

                    showValidationError(
                            "Consultation fee must be a valid number."
                    );

                    txtFee.requestFocus();

                    return;
                }

                if (fee <= 0) {

                    showValidationError(
                            "Consultation fee must be greater than 0."
                    );

                    txtFee.requestFocus();

                    return;
                }


                // ================= CONTACT VALIDATION =================

                if (contact.isEmpty()) {

                    showValidationError(
                            "Please enter contact number."
                    );

                    txtContact.requestFocus();

                    return;
                }

                if (!contact.matches("\\d{10}")) {

                    showValidationError(
                            "Contact number must contain exactly 10 digits."
                    );

                    txtContact.requestFocus();

                    return;
                }


                // ================= ADD TO DATABASE =================

                dao.addDoctor(
                        name,
                        specializationId,
                        qualification,
                        fee,
                        contact
                );


                // ================= SUCCESS MESSAGE =================

                JOptionPane.showMessageDialog(
                        this,
                        "Doctor Added Successfully",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );


                // ================= CLEAR FIELDS =================

                txtName.setText("");

                txtSpecializationId.setText("");

                txtQualification.setText("");

                txtFee.setText("");

                txtContact.setText("");


            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Unable to add doctor.",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );

                ex.printStackTrace();
            }
        });


        // =====================================================
        // ================= VIEW DOCTORS ======================
        // =====================================================

        btnView.addActionListener(e -> {

            try {

                tableModel.setRowCount(0);

                ArrayList<Doctor> doctors =
                        dao.getAllDoctors();

                for (Doctor d : doctors) {

                    tableModel.addRow(
                            new Object[]{
                                    d.getDoctorId(),
                                    d.getDoctorName(),
                                    d.getSpecializationId(),
                                    d.getQualification(),
                                    d.getConsultationFee(),
                                    d.getContact()
                            }
                    );
                }

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Unable to load doctors",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );

                ex.printStackTrace();
            }
        });
    }


    // =========================================================
    // ================= VALIDATION MESSAGE ===================
    // =========================================================

    private void showValidationError(String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
        );
    }
}