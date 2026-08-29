package ui;

import dao.BillingDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.sql.ResultSet;

public class BillingPanel extends JPanel {

    JTextField txtAppointmentId;
    JTextField txtBillDate;
    JTextField txtPaymentMethod;
    JTextField txtPaymentStatus;

    JButton btnAdd;
JButton btnView;
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
                        new GridLayout(1, 3, 10, 10)
                );

        buttonPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        5, 10, 10, 10
                )
        );


        // ADD BILL

        btnAdd =
                new JButton("Add Bill");

        btnAdd.setFont(buttonFont);


        // VIEW BILLS

        btnView =
                new JButton("View Bills");

        btnView.setFont(buttonFont);
        btnReceipt = new JButton("Generate Receipt");
btnReceipt.setFont(buttonFont);

        buttonPanel.add(btnAdd);
buttonPanel.add(btnView);
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

            try {

                // Clear previous rows

                tableModel.setRowCount(0);


                ResultSet rs =
                        dao.getAllBills();


                while (rs.next()) {

                    tableModel.addRow(
                            new Object[]{

                                    rs.getInt(
                                            "bill_id"
                                    ),

                                    rs.getInt(
                                            "appointment_id"
                                    ),

                                    rs.getDouble(
                                            "amount"
                                    ),

                                    rs.getDate(
                                            "bill_date"
                                    ),

                                    rs.getString(
                                            "payment_method"
                                    ),

                                    rs.getString(
                                            "payment_status"
                                    )
                            }
                    );
                }


            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Unable to load bills",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );

                ex.printStackTrace();
            }
        });
        table.getSelectionModel()
     .addListSelectionListener(e -> {

    int row = table.getSelectedRow();

    if(row >= 0) {

        txtAppointmentId.setText(
                tableModel.getValueAt(row,1).toString()
        );

        

        txtBillDate.setText(
                tableModel.getValueAt(row,3).toString()
        );

        txtPaymentMethod.setText(
                tableModel.getValueAt(row,4).toString()
        );

        txtPaymentStatus.setText(
                tableModel.getValueAt(row,5).toString()
        );
    }
});
btnReceipt.addActionListener(e -> {

    int row = table.getSelectedRow();

    if(row < 0) {

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
            tableModel.getValueAt(row,0) + "\n" +
            "Appointment ID: " +
            tableModel.getValueAt(row,1) + "\n" +
            "Amount: " +
            tableModel.getValueAt(row,2) + "\n" +
            "Bill Date: " +
            tableModel.getValueAt(row,3) + "\n" +
            "Payment Method: " +
            tableModel.getValueAt(row,4) + "\n" +
            "Payment Status: " +
            tableModel.getValueAt(row,5) + "\n\n" +
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