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

    JTable table;
    DefaultTableModel tableModel;

    BillingDAO dao = new BillingDAO();

    public BillingPanel() {

        setLayout(new BorderLayout());

        // FORM PANEL
        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));

        form.add(new JLabel("Appointment ID"));
        txtAppointmentId = new JTextField();
        form.add(txtAppointmentId);

        form.add(new JLabel("Bill Date (YYYY-MM-DD)"));
        txtBillDate = new JTextField();
        form.add(txtBillDate);

        form.add(new JLabel("Payment Method"));
        txtPaymentMethod = new JTextField();
        form.add(txtPaymentMethod);

        form.add(new JLabel("Payment Status"));
        txtPaymentStatus = new JTextField();
        form.add(txtPaymentStatus);

        // BUTTON PANEL
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        btnAdd = new JButton("Add Bill");
        btnView = new JButton("View Bills");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnView);

        // TOP PANEL
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(form, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // TABLE
        tableModel = new DefaultTableModel();

        tableModel.setColumnIdentifiers(
                new String[]{
                        "Bill ID",
                        "Appointment ID",
                        "Amount",
                        "Bill Date",
                        "Payment Method",
                        "Payment Status"
                });

        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);

        // ADD BILL
        btnAdd.addActionListener(e -> {

            try {

                dao.addBill(
                        Integer.parseInt(txtAppointmentId.getText()),
                        txtBillDate.getText(),
                        txtPaymentMethod.getText(),
                        txtPaymentStatus.getText()
                );

                JOptionPane.showMessageDialog(
                        null,
                        "Bill Added Successfully"
                );

                txtAppointmentId.setText("");
                txtBillDate.setText("");
                txtPaymentMethod.setText("");
                txtPaymentStatus.setText("");

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        null,
                        "Invalid Input"
                );

                ex.printStackTrace();
            }
        });

        // VIEW BILLS
        btnView.addActionListener(e -> {

            try {

                tableModel.setRowCount(0);

                ResultSet rs = dao.getAllBills();

                while (rs.next()) {

                    tableModel.addRow(
                            new Object[]{
                                    rs.getInt("bill_id"),
                                    rs.getInt("appointment_id"),
                                    rs.getDouble("amount"),
                                    rs.getDate("bill_date"),
                                    rs.getString("payment_method"),
                                    rs.getString("payment_status")
                            }
                    );
                }

            } catch (Exception ex) {
     
                ex.printStackTrace();
            }
        });
    }
}