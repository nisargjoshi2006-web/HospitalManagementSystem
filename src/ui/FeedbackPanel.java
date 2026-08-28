package ui;

import dao.FeedbackDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;

public class FeedbackPanel extends JPanel {

    JTextField txtPatientId;
    JTextField txtRating;
    JTextField txtDate;
    JTextField txtComments;

    JButton btnAdd;
    JButton btnView;

    JTable table;
    DefaultTableModel tableModel;

    FeedbackDAO dao = new FeedbackDAO();

    public FeedbackPanel() {

        setLayout(new BorderLayout(10, 10));

        Font labelFont = new Font("Arial", Font.BOLD, 16);
        Font fieldFont = new Font("Arial", Font.PLAIN, 16);
        Font buttonFont = new Font("Arial", Font.BOLD, 16);
        Font tableFont = new Font("Arial", Font.PLAIN, 15);
        Font tableHeaderFont = new Font("Arial", Font.BOLD, 15);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 5, 10)
        );

        JLabel lblPatient = new JLabel("Patient ID");
        lblPatient.setFont(labelFont);
        formPanel.add(lblPatient);

        txtPatientId = new JTextField();
        txtPatientId.setFont(fieldFont);
        formPanel.add(txtPatientId);

        JLabel lblRating = new JLabel("Rating");
        lblRating.setFont(labelFont);
        formPanel.add(lblRating);

        txtRating = new JTextField();
        txtRating.setFont(fieldFont);
        formPanel.add(txtRating);

        JLabel lblDate =
                new JLabel("Feedback Date (DD-MM-YYYY)");
        lblDate.setFont(labelFont);
        formPanel.add(lblDate);

        txtDate = new JTextField();
        txtDate.setFont(fieldFont);
        formPanel.add(txtDate);

        JLabel lblComments = new JLabel("Comments");
        lblComments.setFont(labelFont);
        formPanel.add(lblComments);

        txtComments = new JTextField();
        txtComments.setFont(fieldFont);
        formPanel.add(txtComments);

        JPanel buttonPanel =
                new JPanel(new GridLayout(1, 2, 10, 10));

        buttonPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        5, 10, 10, 10
                )
        );

        btnAdd = new JButton("Add Feedback");
        btnAdd.setFont(buttonFont);

        btnView = new JButton("View Feedback");
        btnView.setFont(buttonFont);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnView);

        JPanel topPanel = new JPanel(new BorderLayout());

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

        tableModel = new DefaultTableModel();

        tableModel.setColumnIdentifiers(
                new String[]{
                        "ID",
                        "Patient ID",
                        "Rating",
                        "Date",
                        "Comments"
                }
        );

        table = new JTable(tableModel);

        table.setFont(tableFont);
        table.setRowHeight(25);
        table.getTableHeader().setFont(tableHeaderFont);

        JScrollPane scrollPane =
                new JScrollPane(table);

        add(
                scrollPane,
                BorderLayout.CENTER
        );

        // ADD FEEDBACK

        btnAdd.addActionListener(e -> {

            try {

                int patientId =
                        Integer.parseInt(
                                txtPatientId
                                        .getText()
                                        .trim()
                        );

                int rating =
                        Integer.parseInt(
                                txtRating
                                        .getText()
                                        .trim()
                        );

                if (rating < 1 || rating > 5) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Rating must be between 1 and 5"
                    );

                    return;
                }

                String date =
                        txtDate
                                .getText()
                                .trim();

                String comments =
                        txtComments
                                .getText()
                                .trim();

                dao.addFeedback(
                        patientId,
                        rating,
                        date,
                        comments
                );

                JOptionPane.showMessageDialog(
                        this,
                        "Feedback Added Successfully",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                txtPatientId.setText("");
                txtRating.setText("");
                txtDate.setText("");
                txtComments.setText("");

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

        // VIEW FEEDBACK

        btnView.addActionListener(e -> {

            try {

                tableModel.setRowCount(0);

                ResultSet rs =
                        dao.getAllFeedback();

                while (rs.next()) {

                    tableModel.addRow(
                            new Object[]{
                                    rs.getInt("feedback_id"),
                                    rs.getInt("patient_id"),
                                    rs.getInt("rating"),
                                    rs.getDate("feedback_date"),
                                    rs.getString("comments")
                            }
                    );
                }

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Unable to load feedback",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );

                ex.printStackTrace();
            }
        });

        // TABLE ROW CLICK

        table.getSelectionModel()
                .addListSelectionListener(e -> {

                    int row =
                            table.getSelectedRow();

                    if (row >= 0) {

                        txtPatientId.setText(
                                tableModel.getValueAt(row, 1)
                                        .toString()
                        );

                        txtRating.setText(
                                tableModel.getValueAt(row, 2)
                                        .toString()
                        );

                        txtDate.setText(
                                tableModel.getValueAt(row, 3)
                                        .toString()
                        );

                        txtComments.setText(
                                tableModel.getValueAt(row, 4)
                                        .toString()
                        );
                    }
                });
    }
}