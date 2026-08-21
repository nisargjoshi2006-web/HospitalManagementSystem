package ui;

import dao.AppointmentDAO;
import model.Appointment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class AppointmentPanel extends JPanel {

    JTextField txtPatientId;
    JTextField txtDoctorId;
    JTextField txtDate;
    JTextField txtTime;
    JTextField txtRoom;
    JTextField txtStatus;

    JButton btnAdd;
    JButton btnView;

    JTable table;
    DefaultTableModel tableModel;

    AppointmentDAO dao = new AppointmentDAO();

    public AppointmentPanel() {

        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();

        formPanel.setLayout(new GridLayout(7, 2, 10, 10));

        formPanel.add(new JLabel("Patient ID"));
        txtPatientId = new JTextField();
        formPanel.add(txtPatientId);

        formPanel.add(new JLabel("Doctor ID"));
        txtDoctorId = new JTextField();
        formPanel.add(txtDoctorId);

        formPanel.add(new JLabel("Appointment Date (YYYY-MM-DD)"));
        txtDate = new JTextField();
        formPanel.add(txtDate);

        formPanel.add(new JLabel("Appointment Time (HH:MM:SS)"));
        txtTime = new JTextField();
        formPanel.add(txtTime);

        formPanel.add(new JLabel("Room Number"));
        txtRoom = new JTextField();
        formPanel.add(txtRoom);

        formPanel.add(new JLabel("Status"));
        txtStatus = new JTextField();
        formPanel.add(txtStatus);

        btnAdd = new JButton("Add Appointment");
        formPanel.add(btnAdd);

        btnView = new JButton("View Appointments");
        formPanel.add(btnView);

        add(formPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();

        tableModel.addColumn("ID");
        tableModel.addColumn("Patient ID");
        tableModel.addColumn("Doctor ID");
        tableModel.addColumn("Date");
        tableModel.addColumn("Time");
        tableModel.addColumn("Room");
        tableModel.addColumn("Status");

        table = new JTable(tableModel);

        JScrollPane scrollPane =
                new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);

        btnAdd.addActionListener(e -> {

            try {

                int patientId =
                        Integer.parseInt(txtPatientId.getText());

                int doctorId =
                        Integer.parseInt(txtDoctorId.getText());

                String date =
                        txtDate.getText();

                String time =
                        txtTime.getText();

                String room =
                        txtRoom.getText();

                String status =
                        txtStatus.getText();

                dao.addAppointment(
                        patientId,
                        doctorId,
                        date,
                        time,
                        room,
                        status
                );

                JOptionPane.showMessageDialog(
                        null,
                        "Appointment Added Successfully"
                );

                txtPatientId.setText("");
                txtDoctorId.setText("");
                txtDate.setText("");
                txtTime.setText("");
                txtRoom.setText("");
                txtStatus.setText("");

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        null,
                        "Invalid Input"
                );

                ex.printStackTrace();
            }

        });

        btnView.addActionListener(e -> {

            tableModel.setRowCount(0);

            ArrayList<Appointment> appointments =
                    dao.getAllAppointments();

            for (Appointment a : appointments) {

                tableModel.addRow(new Object[]{

                        a.getAppointmentId(),
                        a.getPatientId(),
                        a.getDoctorId(),
                        a.getAppointmentDate(),
                        a.getAppointmentTime(),
                        a.getRoomNumber(),
                        a.getStatus()

                });
            }
        });
    }
}