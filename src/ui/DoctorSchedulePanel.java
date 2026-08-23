package ui;

import dao.DoctorScheduleDAO;
import model.DoctorSchedule;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class DoctorSchedulePanel extends JPanel {

    JTextField txtDoctorId;

    JComboBox<String> dayBox;

    JTextField txtStartTime;
    JTextField txtEndTime;

    JButton btnAdd;
    JButton btnView;

    JTable table;
    DefaultTableModel tableModel;

    DoctorScheduleDAO dao =
            new DoctorScheduleDAO();

    public DoctorSchedulePanel() {

        // ================= MAIN PANEL =================

        setLayout(new BorderLayout(10,10));

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
                        new GridLayout(4,2,10,10)
                );

        formPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10,10,5,10
                )
        );

        // ================= DOCTOR ID =================

        JLabel lblDoctor =
                new JLabel("Doctor ID");

        lblDoctor.setFont(labelFont);

        formPanel.add(lblDoctor);

        txtDoctorId =
                new JTextField();

        txtDoctorId.setFont(fieldFont);

        formPanel.add(txtDoctorId);

        // ================= DAY =================

        JLabel lblDay =
                new JLabel("Day Of Week");

        lblDay.setFont(labelFont);

        formPanel.add(lblDay);

        dayBox =
                new JComboBox<>(
                        new String[]{
                                "Monday",
                                "Tuesday",
                                "Wednesday",
                                "Thursday",
                                "Friday",
                                "Saturday",
                                "Sunday"
                        }
                );

        dayBox.setFont(fieldFont);

        formPanel.add(dayBox);

        // ================= START TIME =================

        JLabel lblStart =
                new JLabel("Start Time");

        lblStart.setFont(labelFont);

        formPanel.add(lblStart);

        txtStartTime =
                new JTextField();

        txtStartTime.setFont(fieldFont);

        formPanel.add(txtStartTime);

        // ================= END TIME =================

        JLabel lblEnd =
                new JLabel("End Time");

        lblEnd.setFont(labelFont);

        formPanel.add(lblEnd);

        txtEndTime =
                new JTextField();

        txtEndTime.setFont(fieldFont);

        formPanel.add(txtEndTime);

        // ================= BUTTON PANEL =================

        JPanel buttonPanel =
                new JPanel(
                        new GridLayout(1,2,10,10)
                );

        buttonPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        5,10,10,10
                )
        );

        btnAdd =
                new JButton("Add Schedule");

        btnAdd.setFont(buttonFont);

        btnView =
                new JButton("View Schedules");

        btnView.setFont(buttonFont);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnView);

        // ================= TOP PANEL =================

        JPanel topPanel =
                new JPanel(
                        new BorderLayout()
                );

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
                        "Schedule ID",
                        "Doctor ID",
                        "Day",
                        "Start Time",
                        "End Time"
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

        // ================= ADD SCHEDULE =================

        btnAdd.addActionListener(e -> {

            try {

                int doctorId =
                        Integer.parseInt(
                                txtDoctorId.getText().trim()
                        );

                String day =
                        dayBox.getSelectedItem().toString();

                String startTime =
                        txtStartTime.getText().trim();

                String endTime =
                        txtEndTime.getText().trim();

                dao.addSchedule(
                        doctorId,
                        day,
                        startTime,
                        endTime
                );

                JOptionPane.showMessageDialog(
                        this,
                        "Schedule Added Successfully",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                txtDoctorId.setText("");
                txtStartTime.setText("");
                txtEndTime.setText("");

            }
            catch(Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid Input",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );

                ex.printStackTrace();
            }

        });

        // ================= VIEW SCHEDULES =================

        btnView.addActionListener(e -> {

            try {

                tableModel.setRowCount(0);

                ArrayList<DoctorSchedule> list =
                        dao.getAllSchedules();

                for(DoctorSchedule ds : list) {

                    tableModel.addRow(
                            new Object[]{
                                    ds.getScheduleId(),
                                    ds.getDoctorId(),
                                    ds.getDayOfWeek(),
                                    ds.getStartTime(),
                                    ds.getEndTime()
                            }
                    );
                }

            }
            catch(Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Unable to Load Schedules",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );

                ex.printStackTrace();
            }

        });
    }
}