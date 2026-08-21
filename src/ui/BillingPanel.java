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

        JPanel form = new JPanel();

        form.setLayout(new GridLayout(4,2,10,10));

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

        btnAdd = new JButton("Add Bill");
        form.add(btnAdd);

        btnView = new JButton("View Bills");
        form.add(btnView);

        add(form, BorderLayout.NORTH);

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

        JScrollPane scroll =
                new JScrollPane(table);

        add(scroll, BorderLayout.CENTER);

        btnAdd.addActionListener(e -> {

            try {

                dao.addBill(
                        Integer.parseInt(
                                txtAppointmentId.getText()),
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
                        dao.getAllBills();

                while(rs.next()) {

                    tableModel.addRow(
                            new Object[]{

                                    rs.getInt("bill_id"),

                                    rs.getInt("appointment_id"),

                                    rs.getDouble("amount"),

                                    rs.getDate("bill_date"),

                                    rs.getString("payment_method"),

                                    rs.getString("payment_status")
                            });
                }

            } catch(Exception ex) {

                ex.printStackTrace();
            }
        });
    }
}