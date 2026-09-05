package ui;

import dao.DoctorScheduleDAO;
import model.DoctorSchedule;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class DoctorSchedulePanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JTextField txtScheduleId;
    private JTextField txtDoctorId;
    private JComboBox<String> dayBox;
    private JTextField txtStartTime;
    private JTextField txtEndTime;

    private JButton btnAdd;
    private JButton btnView;
    private JButton btnSearch;
    private JButton btnUpdate;
    private JButton btnDelete;

    private JTable table;
    private DefaultTableModel tableModel;

    private DoctorScheduleDAO dao = new DoctorScheduleDAO();

    public DoctorSchedulePanel() {

        setLayout(new BorderLayout(10,10));

        JPanel formPanel = new JPanel(new GridLayout(5,2,10,10));

        formPanel.add(new JLabel("Schedule ID"));
        txtScheduleId = new JTextField();
        formPanel.add(txtScheduleId);

        formPanel.add(new JLabel("Doctor ID"));
        txtDoctorId = new JTextField();
        formPanel.add(txtDoctorId);

        formPanel.add(new JLabel("Day Of Week"));

        dayBox = new JComboBox<>(new String[]{
                "Monday",
                "Tuesday",
                "Wednesday",
                "Thursday",
                "Friday",
                "Saturday",
                "Sunday"
        });

        formPanel.add(dayBox);

        formPanel.add(new JLabel("Start Time"));
        txtStartTime = new JTextField();
        formPanel.add(txtStartTime);

        formPanel.add(new JLabel("End Time"));
        txtEndTime = new JTextField();
        formPanel.add(txtEndTime);

        JPanel buttonPanel = new JPanel(new GridLayout(1,5,10,10));

        btnAdd = new JButton("Add");
        btnView = new JButton("View");
        btnSearch = new JButton("Search");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnView);
        buttonPanel.add(btnSearch);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);

        JPanel topPanel = new JPanel(new BorderLayout());

        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();

        tableModel.setColumnIdentifiers(new String[]{
                "Schedule ID",
                "Doctor ID",
                "Day",
                "Start Time",
                "End Time"
        });

        table = new JTable(tableModel);

        JScrollPane sp = new JScrollPane(table);

        add(sp, BorderLayout.CENTER);

        // ADD

        btnAdd.addActionListener(e -> {

            try {

                int doctorId =
                        Integer.parseInt(txtDoctorId.getText());

                dao.addSchedule(
                        doctorId,
                        dayBox.getSelectedItem().toString(),
                        txtStartTime.getText(),
                        txtEndTime.getText()
                );

                JOptionPane.showMessageDialog(
                        this,
                        "Schedule Added Successfully"
                );

                loadTable();

            } catch(Exception ex) {

                ex.printStackTrace();

                JOptionPane.showMessageDialog(
                        this,
                        "Error Adding Schedule"
                );
            }
        });

        // VIEW

        btnView.addActionListener(e -> loadTable());

        // SEARCH

        btnSearch.addActionListener(e -> {
                System.out.println("Search button clicked");

            try {

                int id =
                        Integer.parseInt(
                                txtScheduleId.getText()
                        );

                DoctorSchedule ds =
                        dao.searchSchedule(id);
                        System.out.println("Searching for ID = " + id);

                if(ds != null) {

                    System.out.println("Found: " + ds.getScheduleId() + " " + ds.getDoctorId());
                    txtDoctorId.setText(
        String.valueOf(ds.getDoctorId())
);    
                    dayBox.setSelectedItem(
                            ds.getDayOfWeek()
                    );

                    txtStartTime.setText(
                            ds.getStartTime()
                    );

                    txtEndTime.setText(
                            ds.getEndTime()
                    );

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "Schedule Not Found"
                    );
                }

            } catch(Exception ex) {

                ex.printStackTrace();
            }
        });

        // UPDATE

        btnUpdate.addActionListener(e -> {

            try {

                int scheduleId =
                        Integer.parseInt(
                                txtScheduleId.getText()
                        );

                int doctorId =
                        Integer.parseInt(
                                txtDoctorId.getText()
                        );

                dao.updateSchedule(
                        scheduleId,
                        doctorId,
                        dayBox.getSelectedItem().toString(),
                        txtStartTime.getText(),
                        txtEndTime.getText()
                );

                JOptionPane.showMessageDialog(
                        this,
                        "Schedule Updated Successfully"
                );

                loadTable();

            } catch(Exception ex) {

                ex.printStackTrace();
            }
        });

        // DELETE

        btnDelete.addActionListener(e -> {

            try {

                int scheduleId =
                        Integer.parseInt(
                                txtScheduleId.getText()
                        );

                dao.deleteSchedule(scheduleId);

                JOptionPane.showMessageDialog(
                        this,
                        "Schedule Deleted Successfully"
                );

                txtScheduleId.setText("");
                txtDoctorId.setText("");
                txtStartTime.setText("");
                txtEndTime.setText("");

                loadTable();

            } catch(Exception ex) {

                ex.printStackTrace();
            }
        });

        // TABLE CLICK

        table.getSelectionModel().addListSelectionListener(e -> {

            if(!e.getValueIsAdjusting()
                    && table.getSelectedRow() != -1) {

                int row = table.getSelectedRow();

                txtScheduleId.setText(
                        tableModel.getValueAt(row,0).toString());

                txtDoctorId.setText(
                        tableModel.getValueAt(row,1).toString());

                dayBox.setSelectedItem(
                        tableModel.getValueAt(row,2).toString());

                txtStartTime.setText(
                        tableModel.getValueAt(row,3).toString());

                txtEndTime.setText(
                        tableModel.getValueAt(row,4).toString());
            }
        });
    }

    private void loadTable() {
         System.out.println("Loading Doctor Schedule Table...");

        tableModel.setRowCount(0);

        ArrayList<DoctorSchedule> list =
                dao.getAllSchedules();

        for(DoctorSchedule ds : list) {

            tableModel.addRow(new Object[]{
                    ds.getScheduleId(),
                    ds.getDoctorId(),
                    ds.getDayOfWeek(),
                    ds.getStartTime(),
                    ds.getEndTime()
            });
        }
    }
}