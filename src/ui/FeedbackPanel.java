package ui;

import dao.FeedbackDAO;
import model.Feedback;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class FeedbackPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    JTextField txtPatientId;
    JTextField txtRating;
    JTextField txtDate;
    JTextField txtComments;

    JButton btnAdd;
    JButton btnView;
    JButton btnUpdate;
    JButton btnDelete;
    JButton btnClear;

    JTable table;
    DefaultTableModel tableModel;

    int selectedFeedbackId = -1;

    FeedbackDAO dao = new FeedbackDAO();

    public FeedbackPanel() {

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
                new JPanel(new GridLayout(4, 2, 10, 10));

        formPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 10, 5, 10
                )
        );

        // ================= PATIENT ID =================

        JLabel lblPatient =
                new JLabel("Patient ID");

        lblPatient.setFont(labelFont);

        formPanel.add(lblPatient);

        txtPatientId =
                new JTextField();

        txtPatientId.setFont(fieldFont);

        formPanel.add(txtPatientId);

        // ================= RATING =================

        JLabel lblRating =
                new JLabel("Rating (1-5)");

        lblRating.setFont(labelFont);

        formPanel.add(lblRating);

        txtRating =
                new JTextField();

        txtRating.setFont(fieldFont);

        formPanel.add(txtRating);

        // ================= FEEDBACK DATE =================

        JLabel lblDate =
                new JLabel("Feedback Date (YYYY-MM-DD)");

        lblDate.setFont(labelFont);

        formPanel.add(lblDate);

        txtDate =
                new JTextField();

        txtDate.setFont(fieldFont);

        formPanel.add(txtDate);

        // ================= COMMENTS =================

        JLabel lblComments =
                new JLabel("Comments");

        lblComments.setFont(labelFont);

        formPanel.add(lblComments);

        txtComments =
                new JTextField();

        txtComments.setFont(fieldFont);

        formPanel.add(txtComments);

        // ================= BUTTON PANEL =================

        JPanel buttonPanel =
                new JPanel(new GridLayout(1, 5, 10, 10));

        buttonPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        5, 10, 10, 10
                )
        );

        // ADD BUTTON

        btnAdd =
                new JButton("Add Feedback");

        btnAdd.setFont(buttonFont);

        // VIEW BUTTON

        btnView =
                new JButton("View Feedback");

        btnView.setFont(buttonFont);

        // UPDATE BUTTON

        btnUpdate =
                new JButton("Update Feedback");

        btnUpdate.setFont(buttonFont);

        // DELETE BUTTON

        btnDelete =
                new JButton("Delete Feedback");

        btnDelete.setFont(buttonFont);

        // CLEAR BUTTON

        btnClear =
                new JButton("Clear Form");

        btnClear.setFont(buttonFont);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnView);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

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
                        "Rating",
                        "Date",
                        "Comments"
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

        // ================= ADD FEEDBACK =================

        btnAdd.addActionListener(e -> {

            try {

                int patientId =
                        Integer.parseInt(
                                txtPatientId
                                        .getText()
                                        .trim()
                        );

                String ratingText =
                        txtRating
                                .getText()
                                .trim();

                if (ratingText.isEmpty()) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Please enter rating.",
                            "Validation Error",
                            JOptionPane.WARNING_MESSAGE
                    );

                    return;
                }

                int rating =
                        Integer.parseInt(ratingText);

                if (rating < 1 || rating > 5) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Rating must be between 1 and 5.",
                            "Validation Error",
                            JOptionPane.WARNING_MESSAGE
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

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Patient ID and Rating must be valid numbers.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Unable to add feedback.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );

                ex.printStackTrace();
            }
        });

        // ================= VIEW FEEDBACK =================

        btnView.addActionListener(e -> {

            tableModel.setRowCount(0);

            ArrayList<Feedback> feedbackList = dao.getAllFeedback();

            for (Feedback f : feedbackList) {

                tableModel.addRow(
                        new Object[]{
                                f.getFeedbackId(),
                                f.getPatientId(),
                                f.getRating(),
                                f.getFeedbackDate(),
                                f.getComments()
                        }
                );
            }
        });

        // ================= SELECT ROW -> FILL FORM =================

        table.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()
                            && table.getSelectedRow() != -1) {

                        int row =
                                table.getSelectedRow();

                        selectedFeedbackId =
                                Integer.parseInt(
                                        tableModel
                                                .getValueAt(row, 0)
                                                .toString()
                                );

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

        // ================= UPDATE FEEDBACK =================

        btnUpdate.addActionListener(e -> {

            if (selectedFeedbackId == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please select a feedback entry from the table first.",
                        "No Selection",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            try {

                String ratingText =
                        txtRating.getText().trim();

                int rating =
                        Integer.parseInt(ratingText);

                if (rating < 1 || rating > 5) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Rating must be between 1 and 5.",
                            "Validation Error",
                            JOptionPane.WARNING_MESSAGE
                    );

                    return;
                }

                String comments =
                        txtComments.getText().trim();

                dao.updateFeedback(
                        selectedFeedbackId,
                        rating,
                        comments
                );

                JOptionPane.showMessageDialog(
                        this,
                        "Feedback Updated Successfully",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                btnView.doClick();
                selectedFeedbackId = -1;

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Rating must be a valid number.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Unable to update feedback.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );

                ex.printStackTrace();
            }
        });

        // ================= DELETE FEEDBACK =================

        btnDelete.addActionListener(e -> {

            if (selectedFeedbackId == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please select a feedback entry from the table first.",
                        "No Selection",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            int confirm =
                    JOptionPane.showConfirmDialog(
                            this,
                            "Are you sure you want to delete this feedback?\nThis cannot be undone.",
                            "Confirm Delete",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );

            if (confirm == JOptionPane.YES_OPTION) {

                dao.deleteFeedback(selectedFeedbackId);

                JOptionPane.showMessageDialog(
                        this,
                        "Feedback Deleted Successfully",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                btnView.doClick();
                selectedFeedbackId = -1;
            }
        });

        // ================= CLEAR FORM =================

        btnClear.addActionListener(e -> {

            txtPatientId.setText("");
            txtRating.setText("");
            txtDate.setText("");
            txtComments.setText("");
            selectedFeedbackId = -1;
            table.clearSelection();
        });
    }
}