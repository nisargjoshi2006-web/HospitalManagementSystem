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

        setLayout(new BorderLayout());

        JPanel form = new JPanel();

        form.setLayout(new GridLayout(5,2,10,10));

        form.add(new JLabel("Patient ID"));

        txtPatientId = new JTextField();
        form.add(txtPatientId);

        form.add(new JLabel("Rating"));

        txtRating = new JTextField();
        form.add(txtRating);

        form.add(new JLabel("Feedback Date (YYYY-MM-DD)"));

        txtDate = new JTextField();
        form.add(txtDate);

        form.add(new JLabel("Comments"));

        txtComments = new JTextField();
        form.add(txtComments);

        btnAdd = new JButton("Add Feedback");
        form.add(btnAdd);

        btnView = new JButton("View Feedback");
        form.add(btnView);

        add(form, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();

        tableModel.setColumnIdentifiers(
                new String[]{
                        "ID",
                        "Patient ID",
                        "Rating",
                        "Date",
                        "Comments"
                });

        table = new JTable(tableModel);

        JScrollPane scroll =
                new JScrollPane(table);

        add(scroll, BorderLayout.CENTER);

        btnAdd.addActionListener(e -> {

            try {

                dao.addFeedback(
                        Integer.parseInt(
                                txtPatientId.getText()),
                        Integer.parseInt(
                                txtRating.getText()),
                        txtDate.getText(),
                        txtComments.getText()
                );

                JOptionPane.showMessageDialog(
                        null,
                        "Feedback Added Successfully"
                );

                txtPatientId.setText("");
                txtRating.setText("");
                txtDate.setText("");
                txtComments.setText("");

            } catch(Exception ex) {

                JOptionPane.showMessageDialog(
                        null,
                        "Invalid Input"
                );

                ex.printStackTrace();
            }
        });

        btnView.addActionListener(e -> {

            try {

                tableModel.setRowCount(0);

                ResultSet rs =
                        dao.getAllFeedback();

                while(rs.next()) {

                    tableModel.addRow(
                            new Object[]{

                                    rs.getInt("feedback_id"),

                                    rs.getInt("patient_id"),

                                    rs.getInt("rating"),

                                    rs.getDate("feedback_date"),

                                    rs.getString("comments")
                            });
                }

            } catch(Exception ex) {

                ex.printStackTrace();
            }
        });
    }
}