package ui;

import dao.BillingDAO;
import model.Billing;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.ArrayList;

public class BillingPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    JTextField txtAppointmentId;
    JTextField txtBillDate;
    JTextField txtPaymentMethod;
    JTextField txtPaymentStatus;

    JButton btnAdd;
    JButton btnView;
    JButton btnUpdate;
    JButton btnDelete;
    JButton btnReceipt;

    JTable table;
    DefaultTableModel tableModel;

    BillingDAO dao = new BillingDAO();

    public BillingPanel() {

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
                        new GridLayout(4, 2, 10, 10)
                );

        formPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 10, 5, 10
                )
        );


        // ================= APPOINTMENT ID =================

        JLabel lblAppointment =
                new JLabel("Appointment ID");

        lblAppointment.setFont(labelFont);

        formPanel.add(lblAppointment);

        txtAppointmentId =
                new JTextField();

        txtAppointmentId.setFont(fieldFont);

        formPanel.add(txtAppointmentId);


        // ================= BILL DATE =================

        JLabel lblBillDate =
                new JLabel(
                        "Bill Date (YYYY-MM-DD)"
                );

        lblBillDate.setFont(labelFont);

        formPanel.add(lblBillDate);

        txtBillDate =
                new JTextField();

        txtBillDate.setFont(fieldFont);

        formPanel.add(txtBillDate);


        // ================= PAYMENT METHOD =================

        JLabel lblPaymentMethod =
                new JLabel("Payment Method");

        lblPaymentMethod.setFont(labelFont);

        formPanel.add(lblPaymentMethod);

        txtPaymentMethod =
                new JTextField();

        txtPaymentMethod.setFont(fieldFont);

        formPanel.add(txtPaymentMethod);


        // ================= PAYMENT STATUS =================

        JLabel lblPaymentStatus =
                new JLabel("Payment Status");

        lblPaymentStatus.setFont(labelFont);

        formPanel.add(lblPaymentStatus);

        txtPaymentStatus =
                new JTextField();

        txtPaymentStatus.setFont(fieldFont);

        formPanel.add(txtPaymentStatus);


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

        // ADD BILL
        btnAdd = new JButton("Add Bill");
        btnAdd.setFont(buttonFont);

        // VIEW BILLS
        btnView = new JButton("View Bills");
        btnView.setFont(buttonFont);

        // UPDATE BILL
        btnUpdate = new JButton("Update Bill");
        btnUpdate.setFont(buttonFont);

        // DELETE BILL
        btnDelete = new JButton("Delete Bill");
        btnDelete.setFont(buttonFont);

        // RECEIPT
        btnReceipt = new JButton("Generate Receipt");
        btnReceipt.setFont(buttonFont);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnView);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnReceipt);


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
                        "Bill ID",
                        "Appointment ID",
                        "Amount",
                        "Bill Date",
                        "Payment Method",
                        "Payment Status"
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


        // ================= ADD BILL =================

        btnAdd.addActionListener(e -> {

            try {

                int appointmentId =
                        Integer.parseInt(
                                txtAppointmentId
                                        .getText()
                                        .trim()
                        );

                String billDate =
                        txtBillDate
                                .getText()
                                .trim();

                String paymentMethod =
                        txtPaymentMethod
                                .getText()
                                .trim();

                String paymentStatus =
                        txtPaymentStatus
                                .getText()
                                .trim();

                if (!paymentStatus.equalsIgnoreCase("Paid")
                        && !paymentStatus.equalsIgnoreCase("Pending")) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Payment Status must be Paid or Pending"
                    );
                    return;
                }

                dao.addBill(
                        appointmentId,
                        billDate,
                        paymentMethod,
                        paymentStatus
                );

                JOptionPane.showMessageDialog(
                        this,
                        "Bill Added Successfully",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                // Clear fields
                txtAppointmentId.setText("");
                txtBillDate.setText("");
                txtPaymentMethod.setText("");
                txtPaymentStatus.setText("");

                btnView.doClick();

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


        // ================= VIEW BILLS =================

        btnView.addActionListener(e -> {

            tableModel.setRowCount(0);

            ArrayList<Billing> bills = dao.getAllBills();

            for (Billing b : bills) {

                tableModel.addRow(
                        new Object[]{
                                b.getBillId(),
                                b.getAppointmentId(),
                                b.getAmount(),
                                b.getBillDate(),
                                b.getPaymentMethod(),
                                b.getPaymentStatus()
                        }
                );
            }
        });


        // ================= UPDATE BILL =================

        btnUpdate.addActionListener(e -> {

            int row = table.getSelectedRow();

            if (row < 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Select a bill from the table first",
                        "No Selection",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            try {

                int billId = Integer.parseInt(
                        tableModel.getValueAt(row, 0).toString()
                );

                String newMethod = txtPaymentMethod.getText().trim();
                String newStatus = txtPaymentStatus.getText().trim();

                if (!newStatus.equalsIgnoreCase("Paid")
                        && !newStatus.equalsIgnoreCase("Pending")) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Payment Status must be Paid or Pending"
                    );
                    return;
                }

                dao.updateBill(billId, newMethod, newStatus);

                JOptionPane.showMessageDialog(
                        this,
                        "Bill Updated Successfully"
                );

                btnView.doClick();

            } catch (Exception ex) {

                ex.printStackTrace();

                JOptionPane.showMessageDialog(
                        this,
                        "Error updating bill",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });


        // ================= DELETE BILL =================

        btnDelete.addActionListener(e -> {

            int row = table.getSelectedRow();

            if (row < 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Select a bill from the table first",
                        "No Selection",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this bill?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {

                int billId = Integer.parseInt(
                        tableModel.getValueAt(row, 0).toString()
                );

                dao.deleteBill(billId);

                JOptionPane.showMessageDialog(
                        this,
                        "Bill Deleted Successfully"
                );

                btnView.doClick();
            }
        });


        // ================= TABLE SELECTION =================

        table.getSelectionModel()
                .addListSelectionListener(e -> {

            int row = table.getSelectedRow();

            if (row >= 0) {

                txtAppointmentId.setText(
                        tableModel.getValueAt(row, 1).toString()
                );

                txtBillDate.setText(
                        tableModel.getValueAt(row, 3).toString()
                );

                txtPaymentMethod.setText(
                        tableModel.getValueAt(row, 4).toString()
                );

                txtPaymentStatus.setText(
                        tableModel.getValueAt(row, 5).toString()
                );
            }
        });


        // ================= GENERATE RECEIPT =================

        btnReceipt.addActionListener(e -> {

            int row = table.getSelectedRow();

            if (row < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Select a bill first"
                );
                return;
            }

            String receipt =
                    "=========================\n" +
                    "      HOSPITAL RECEIPT\n" +
                    "=========================\n\n" +
                    "Bill ID: " +
                    tableModel.getValueAt(row, 0) + "\n" +
                    "Appointment ID: " +
                    tableModel.getValueAt(row, 1) + "\n" +
                    "Amount: " +
                    tableModel.getValueAt(row, 2) + "\n" +
                    "Bill Date: " +
                    tableModel.getValueAt(row, 3) + "\n" +
                    "Payment Method: " +
                    tableModel.getValueAt(row, 4) + "\n" +
                    "Payment Status: " +
                    tableModel.getValueAt(row, 5) + "\n\n" +
                    "Thank You!\n" +
                    "=========================";

            JOptionPane.showMessageDialog(
                    this,
                    receipt,
                    "Receipt",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });
    }
}