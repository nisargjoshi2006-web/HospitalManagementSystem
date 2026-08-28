package ui;

import dao.AppointmentDAO;
import model.Appointment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.ArrayList;

public class AppointmentPanel extends JPanel {

    JTextField txtAppointmentId;
JTextField txtPatientId;
    JTextField txtDoctorId;
    JTextField txtDate;
    JTextField txtTime;
    JTextField txtRoom;
    JTextField txtStatus;

    JButton btnAdd;
JButton btnView;
JButton btnSearch;
JButton btnUpdate;
JButton btnDelete;

    JTable table;
    DefaultTableModel tableModel;

    AppointmentDAO dao = new AppointmentDAO();

    public AppointmentPanel() {

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
        JLabel lblAppointmentId =
        new JLabel("Appointment ID");

lblAppointmentId.setFont(labelFont);

formPanel.add(lblAppointmentId);

txtAppointmentId =
        new JTextField();

txtAppointmentId.setFont(fieldFont);

formPanel.add(txtAppointmentId);


        // ================= PATIENT ID =================

        JLabel lblPatient =
                new JLabel("Patient ID");

        lblPatient.setFont(labelFont);

        formPanel.add(lblPatient);

        txtPatientId =
                new JTextField();

        txtPatientId.setFont(fieldFont);

        formPanel.add(txtPatientId);


        // ================= DOCTOR ID =================

        JLabel lblDoctor =
                new JLabel("Doctor ID");

        lblDoctor.setFont(labelFont);

        formPanel.add(lblDoctor);

        txtDoctorId =
                new JTextField();

        txtDoctorId.setFont(fieldFont);

        formPanel.add(txtDoctorId);


        // ================= APPOINTMENT DATE =================

        JLabel lblDate =
                new JLabel(
                        "Appointment Date (YYYY-MM-DD)"
                );

        lblDate.setFont(labelFont);

        formPanel.add(lblDate);

        txtDate =
                new JTextField();

        txtDate.setFont(fieldFont);

        formPanel.add(txtDate);


        // ================= APPOINTMENT TIME =================

        JLabel lblTime =
                new JLabel(
                        "Appointment Time (HH:MM:SS)"
                );

        lblTime.setFont(labelFont);

        formPanel.add(lblTime);

        txtTime =
                new JTextField();

        txtTime.setFont(fieldFont);

        formPanel.add(txtTime);


        // ================= ROOM =================

        JLabel lblRoom =
                new JLabel("Room Number");

        lblRoom.setFont(labelFont);

        formPanel.add(lblRoom);

        txtRoom =
                new JTextField();

        txtRoom.setFont(fieldFont);

        formPanel.add(txtRoom);


        // ================= STATUS =================

        JLabel lblStatus =
                new JLabel("Status");

        lblStatus.setFont(labelFont);

        formPanel.add(lblStatus);

        txtStatus =
                new JTextField();

        txtStatus.setFont(fieldFont);

        formPanel.add(txtStatus);


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

        btnAdd = new JButton("Add");
btnView = new JButton("View");
btnSearch = new JButton("Search");
btnUpdate = new JButton("Update");
btnDelete = new JButton("Delete");

btnAdd.setFont(buttonFont);
btnView.setFont(buttonFont);
btnSearch.setFont(buttonFont);
btnUpdate.setFont(buttonFont);
btnDelete.setFont(buttonFont);
        btnView.setFont(buttonFont);


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

        tableModel =
                new DefaultTableModel();

        tableModel.setColumnIdentifiers(
                new String[]{
                        "ID",
                        "Patient ID",
                        "Doctor ID",
                        "Date",
                        "Time",
                        "Room",
                        "Status"
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


        // ================= ADD APPOINTMENT =================

        btnAdd.addActionListener(e -> {

            try {

                int patientId =
                        Integer.parseInt(
                                txtPatientId
                                        .getText()
                                        .trim()
                        );

                int doctorId =
                        Integer.parseInt(
                                txtDoctorId
                                        .getText()
                                        .trim()
                        );

                String date =
                        txtDate
                                .getText()
                                .trim();

                String time =
                        txtTime
                                .getText()
                                .trim();

                String room =
                        txtRoom
                                .getText()
                                .trim();

                String status =
                        txtStatus
                                .getText()
                                .trim();


                dao.addAppointment(
                        patientId,
                        doctorId,
                        date,
                        time,
                        room,
                        status
                );


                JOptionPane.showMessageDialog(
                        this,
                        "Appointment Added Successfully",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );


                txtPatientId.setText("");

                txtDoctorId.setText("");

                txtDate.setText("");

                txtTime.setText("");

                txtRoom.setText("");

                txtStatus.setText("");


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


        // ================= VIEW APPOINTMENTS =================

        btnView.addActionListener(e -> {

            try {

                tableModel.setRowCount(0);


                ArrayList<Appointment> appointments =
                        dao.getAllAppointments();


                for (Appointment a : appointments) {

                    tableModel.addRow(
                            new Object[]{

                                    a.getAppointmentId(),

                                    a.getPatientId(),

                                    a.getDoctorId(),

                                    a.getAppointmentDate(),

                                    a.getAppointmentTime(),

                                    a.getRoomNumber(),

                                    a.getStatus()
                            }
                    );
                }


            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Unable to load appointments",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );

                ex.printStackTrace();
            }
        });
        btnSearch.addActionListener(e -> {

    try {

        int id =
                Integer.parseInt(
                        txtAppointmentId.getText().trim());

        Appointment a =
                dao.searchAppointment(id);

        if(a != null){

            txtPatientId.setText(
                    String.valueOf(a.getPatientId()));

            txtDoctorId.setText(
                    String.valueOf(a.getDoctorId()));

            txtDate.setText(
                    a.getAppointmentDate());

            txtTime.setText(
                    a.getAppointmentTime());

            txtRoom.setText(
                    a.getRoomNumber());

            txtStatus.setText(
                    a.getStatus());

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Appointment Not Found");
        }

    } catch(Exception ex){

        ex.printStackTrace();
    }
});

btnUpdate.addActionListener(e -> {

    try {

        dao.updateAppointment(

                Integer.parseInt(
                        txtAppointmentId.getText().trim()),

                Integer.parseInt(
                        txtPatientId.getText().trim()),

                Integer.parseInt(
                        txtDoctorId.getText().trim()),

                txtDate.getText().trim(),

                txtTime.getText().trim(),

                txtRoom.getText().trim(),

                txtStatus.getText().trim()
        );

        JOptionPane.showMessageDialog(
                this,
                "Appointment Updated Successfully");

        btnView.doClick();

    } catch(Exception ex){

        ex.printStackTrace();
    }
});

btnDelete.addActionListener(e -> {

    try {

        int id =
                Integer.parseInt(
                        txtAppointmentId.getText().trim());

        dao.deleteAppointment(id);

        JOptionPane.showMessageDialog(
                this,
                "Appointment Deleted Successfully");

        btnView.doClick();

    } catch(Exception ex){

        ex.printStackTrace();
    }
});

table.getSelectionModel()
        .addListSelectionListener(e -> {

    if(!e.getValueIsAdjusting()
            && table.getSelectedRow() != -1){

        int row =
                table.getSelectedRow();

        txtAppointmentId.setText(
                tableModel.getValueAt(row,0).toString());

        txtPatientId.setText(
                tableModel.getValueAt(row,1).toString());

        txtDoctorId.setText(
                tableModel.getValueAt(row,2).toString());

        txtDate.setText(
                tableModel.getValueAt(row,3).toString());

        txtTime.setText(
                tableModel.getValueAt(row,4).toString());

        txtRoom.setText(
                tableModel.getValueAt(row,5).toString());

        txtStatus.setText(
                tableModel.getValueAt(row,6).toString());
    }
});
    }
}