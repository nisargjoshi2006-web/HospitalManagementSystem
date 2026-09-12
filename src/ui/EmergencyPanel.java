package ui;

import dao.EmergencyDAO;
import dao.DoctorDAO;
import dao.PatientDAO;
import model.Emergency;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class EmergencyPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JTextField txtEmergencyId;
    private JTextField txtPatientId;
    private JTextField txtEmergencyType;
    private JTextField txtPriority;
    private JTextField txtArrivalDate;
    private JTextField txtArrivalTime;
    private JTextField txtStatus;
    private JTextField txtAssignedDoctor;

    private JButton btnAdd;
    private JButton btnView;
    private JButton btnSearch;
    private JButton btnUpdate;
    private JButton btnDelete;

    private JTable table;
    private DefaultTableModel tableModel;

    private EmergencyDAO dao = new EmergencyDAO();
    private DoctorDAO doctorDAO = new DoctorDAO();
    private PatientDAO patientDAO = new PatientDAO();

    public EmergencyPanel() {

        setLayout(new BorderLayout(10, 10));

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

        JPanel formPanel =
                new JPanel(
                        new GridLayout(8, 2, 10, 10)
                );

        formPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 10, 5, 10
                )
        );

        JLabel lblEmergencyId =
                new JLabel("Emergency ID");
        lblEmergencyId.setFont(labelFont);
        formPanel.add(lblEmergencyId);

        txtEmergencyId = new JTextField();
        txtEmergencyId.setFont(fieldFont);
        formPanel.add(txtEmergencyId);

        JLabel lblPatientId =
                new JLabel("Patient ID");
        lblPatientId.setFont(labelFont);
        formPanel.add(lblPatientId);

        txtPatientId = new JTextField();
        txtPatientId.setFont(fieldFont);
        formPanel.add(txtPatientId);

        JLabel lblEmergencyType =
                new JLabel("Emergency Type");
        lblEmergencyType.setFont(labelFont);
        formPanel.add(lblEmergencyType);

        txtEmergencyType = new JTextField();
        txtEmergencyType.setFont(fieldFont);
        formPanel.add(txtEmergencyType);

        JLabel lblPriority =
                new JLabel("Priority Level");
        lblPriority.setFont(labelFont);
        formPanel.add(lblPriority);

        txtPriority = new JTextField();
        txtPriority.setFont(fieldFont);
        formPanel.add(txtPriority);

        JLabel lblArrivalDate =
                new JLabel("Arrival Date (YYYY-MM-DD)");

        lblArrivalDate.setFont(labelFont);

        formPanel.add(lblArrivalDate);

        txtArrivalDate = new JTextField();

        txtArrivalDate.setFont(fieldFont);

        formPanel.add(txtArrivalDate);

        JLabel lblArrivalTime =
                new JLabel("Arrival Time (HH:MM:SS)");
        lblArrivalTime.setFont(labelFont);
        formPanel.add(lblArrivalTime);

        txtArrivalTime = new JTextField();
        txtArrivalTime.setFont(fieldFont);
        formPanel.add(txtArrivalTime);

        JLabel lblStatus =
                new JLabel("Status");
        lblStatus.setFont(labelFont);
        formPanel.add(lblStatus);

        txtStatus = new JTextField();
        txtStatus.setFont(fieldFont);
        formPanel.add(txtStatus);

        JLabel lblAssignedDoctor =
                new JLabel("Assigned Doctor");
        lblAssignedDoctor.setFont(labelFont);
        formPanel.add(lblAssignedDoctor);

        txtAssignedDoctor = new JTextField();
        txtAssignedDoctor.setFont(fieldFont);
        formPanel.add(txtAssignedDoctor);

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
        btnAdd.setFont(buttonFont);

        btnView = new JButton("View");
        btnView.setFont(buttonFont);

        btnSearch = new JButton("Search");
        btnSearch.setFont(buttonFont);

        btnUpdate = new JButton("Update");
        btnUpdate.setFont(buttonFont);

        btnDelete = new JButton("Delete");
        btnDelete.setFont(buttonFont);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnView);
        buttonPanel.add(btnSearch);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);

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

        tableModel =
                new DefaultTableModel();

        tableModel.setColumnIdentifiers(
                new String[]{
                        "Emergency ID",
                        "Patient ID",
                        "Emergency Type",
                        "Priority",
                        "Arrival Date",
                        "Arrival Time",
                        "Status",
                        "Assigned Doctor"
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

        btnAdd.addActionListener(e -> {

            String patientIdText = txtPatientId.getText().trim();
            String assignedDoctorText = txtAssignedDoctor.getText().trim();
            
           

            int patientId;
            int assignedDoctor;

            try {
                patientId = Integer.parseInt(patientIdText);
                assignedDoctor = Integer.parseInt(assignedDoctorText);
            } catch (NumberFormatException ex) {
                showValidationError("Patient ID and Assigned Doctor must be valid numbers.");
                return;
            }
            if (!patientDAO.patientExists(patientId)) {

    JOptionPane.showMessageDialog(
            this,
            "Patient ID does not exist!"
    );

    return;
}


if (!doctorDAO.doctorExists(assignedDoctor)) {

    JOptionPane.showMessageDialog(
            this,
            "Doctor ID does not exist!"
    );

    return;
}
 String arrivalDate = txtArrivalDate.getText().trim();
            String arrivalTime = txtArrivalTime.getText().trim();

            if (!isValidDate(arrivalDate)) {
                showValidationError("Arrival Date must be a valid date in YYYY-MM-DD format.");
                return;
            }

            if (!isValidTime(arrivalTime)) {
                showValidationError("Arrival Time must be in HH:MM:SS format (00-23 : 00-59 : 00-59).");
                return;
            }

            dao.addEmergency(
                    patientId,
                    txtEmergencyType.getText(),
                    txtPriority.getText(),
                    txtStatus.getText(),
                    assignedDoctor,
                    arrivalDate,
                    arrivalTime
            );

            loadTable();

            JOptionPane.showMessageDialog(
                    this,
                    "Emergency Added Successfully"
            );
        });

        btnView.addActionListener(
                e -> loadTable()
        );

        btnSearch.addActionListener(e -> {

            try {

                int id =
                        Integer.parseInt(
                                txtEmergencyId.getText().trim()
                        );

                Emergency em =
                        dao.searchEmergency(id);

                if (em != null) {

                    txtPatientId.setText(
                            String.valueOf(
                                    em.getPatientId()
                            )
                    );

                    txtEmergencyType.setText(
                            em.getEmergencyType()
                    );

                    txtPriority.setText(
                            em.getPriorityLevel()
                    );

                    txtArrivalDate.setText(
                            em.getArrivalDate()
                    );

                    txtArrivalTime.setText(
                            em.getArrivalTime()
                    );

                    txtStatus.setText(
                            em.getStatus()
                    );

                    txtAssignedDoctor.setText(
                            String.valueOf(
                                    em.getAssignedDoctor()
                            )
                    );

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "Emergency Not Found"
                    );
                }

            } catch (NumberFormatException ex) {

                showValidationError("Emergency ID must be a valid number.");
            }
        });

        btnUpdate.addActionListener(e -> {

            String arrivalDate = txtArrivalDate.getText().trim();
            String arrivalTime = txtArrivalTime.getText().trim();

            if (!isValidDate(arrivalDate)) {
                showValidationError("Arrival Date must be a valid date in YYYY-MM-DD format.");
                return;
            }

            if (!isValidTime(arrivalTime)) {
                showValidationError("Arrival Time must be in HH:MM:SS format (00-23 : 00-59 : 00-59).");
                return;
            }

            int emergencyId;
            int patientId;
            int assignedDoctor;

            try {
                emergencyId = Integer.parseInt(txtEmergencyId.getText().trim());
                patientId = Integer.parseInt(txtPatientId.getText().trim());
                assignedDoctor = Integer.parseInt(txtAssignedDoctor.getText().trim());
            } catch (NumberFormatException ex) {
                showValidationError("Emergency ID, Patient ID, and Assigned Doctor must be valid numbers.");
                return;
            }
            if (!doctorDAO.doctorExists(assignedDoctor)) {

    JOptionPane.showMessageDialog(
            this,
            "Doctor ID does not exist!"
    );

    return;
}

            dao.updateEmergency(
                    emergencyId,
                    patientId,
                    txtEmergencyType.getText(),
                    txtPriority.getText(),
                    txtStatus.getText(),
                    assignedDoctor,
                    arrivalDate,
                    arrivalTime
            );

            loadTable();

            JOptionPane.showMessageDialog(
                    this,
                    "Emergency Updated Successfully"
            );
        });

        btnDelete.addActionListener(e -> {

            try {

                int id = Integer.parseInt(
                        txtEmergencyId.getText().trim()
                );

                dao.deleteEmergency(id);

                loadTable();

                JOptionPane.showMessageDialog(
                        this,
                        "Emergency Deleted Successfully"
                );

            } catch (NumberFormatException ex) {

                showValidationError("Emergency ID must be a valid number.");
            }
        });

        table.getSelectionModel()
                .addListSelectionListener(e -> {

            if(!e.getValueIsAdjusting()
                    && table.getSelectedRow() != -1) {

                int row =
                        table.getSelectedRow();

                Object val0 = tableModel.getValueAt(row, 0);
                txtEmergencyId.setText(val0 != null ? val0.toString() : "");

                Object val1 = tableModel.getValueAt(row, 1);
                txtPatientId.setText(val1 != null ? val1.toString() : "");

                Object val2 = tableModel.getValueAt(row, 2);
                txtEmergencyType.setText(val2 != null ? val2.toString() : "");

                Object val3 = tableModel.getValueAt(row, 3);
                txtPriority.setText(val3 != null ? val3.toString() : "");

                Object val4 = tableModel.getValueAt(row, 4);
                txtArrivalDate.setText(val4 != null ? val4.toString() : "");

                Object val5 = tableModel.getValueAt(row, 5);
                txtArrivalTime.setText(val5 != null ? val5.toString() : "");

                Object val6 = tableModel.getValueAt(row, 6);
                txtStatus.setText(val6 != null ? val6.toString() : "");

                Object val7 = tableModel.getValueAt(row, 7);
                txtAssignedDoctor.setText(val7 != null ? val7.toString() : "");
            }
        });
    }

    private void showValidationError(String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private boolean isValidDate(String dateStr) {

        if (!dateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return false;
        }

        try {
            java.time.LocalDate.parse(dateStr);
            return true;
        } catch (java.time.format.DateTimeParseException ex) {
            return false;
        }
    }

    private boolean isValidTime(String timeStr) {

        if (!timeStr.matches("\\d{2}:\\d{2}:\\d{2}")) {
            return false;
        }

        try {
            java.time.LocalTime.parse(timeStr);
            return true;
        } catch (java.time.format.DateTimeParseException ex) {
            return false;
        }
    }

    private void loadTable() {

        tableModel.setRowCount(0);

        ArrayList<Emergency> list =
                dao.getAllEmergencies();

        for(Emergency em : list) {

            tableModel.addRow(new Object[]{
                    em.getEmergencyId(),
                    em.getPatientId(),
                    em.getEmergencyType(),
                    em.getPriorityLevel(),
                    em.getArrivalDate(),
                    em.getArrivalTime(),
                    em.getStatus(),
                    em.getAssignedDoctor()
            });
        }
    }
}