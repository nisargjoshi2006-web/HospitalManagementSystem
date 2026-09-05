package ui;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import java.awt.*;

public class LoginUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private int failedAttempts = 0;

    public LoginUI() {

        setTitle("Hospital Management System — Login");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // ================= MAIN PANEL =================

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(30, 50, 30, 50)
        );
        mainPanel.setBackground(new Color(245, 245, 245));

        // ================= TITLE =================

        JLabel lblTitle = new JLabel("Hospital Management System");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitle = new JLabel("Please login to continue");
        lblSubtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        lblSubtitle.setForeground(Color.GRAY);
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ================= FORM =================

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 15));
        formPanel.setBackground(new Color(245, 245, 245));
        formPanel.setMaximumSize(new Dimension(350, 80));

        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);

        JLabel lblUsername = new JLabel("Username :");
        lblUsername.setFont(labelFont);

        txtUsername = new JTextField();
        txtUsername.setFont(fieldFont);

        JLabel lblPassword = new JLabel("Password :");
        lblPassword.setFont(labelFont);

        txtPassword = new JPasswordField();
        txtPassword.setFont(fieldFont);

        formPanel.add(lblUsername);
        formPanel.add(txtUsername);
        formPanel.add(lblPassword);
        formPanel.add(txtPassword);

        // ================= LOGIN BUTTON =================

        btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 16));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(200, 40));
        btnLogin.setBackground(new Color(0, 120, 215));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);

        // ================= LAYOUT =================

        mainPanel.add(lblTitle);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(lblSubtitle);
        mainPanel.add(Box.createVerticalStrut(25));
        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(btnLogin);

        add(mainPanel);

        // ================= LOGIN ACTION =================

        btnLogin.addActionListener(e -> handleLogin());

        // Enter key triggers login
        txtPassword.addActionListener(e -> handleLogin());

        setVisible(true);
    }

    private void handleLogin() {

        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter both username and password",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        UserDAO dao = new UserDAO();
        User user = dao.authenticate(username, password);

        if (user != null) {

            // Login successful
            dispose();
            new HospitalManagementUI(user);

        } else {

            failedAttempts++;

            if (failedAttempts >= 3) {

                JOptionPane.showMessageDialog(
                        this,
                        "Multiple failed login attempts!\n" +
                        "Please verify your credentials.",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE
                );
            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid username or password",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            }

            txtPassword.setText("");
            txtPassword.requestFocus();
        }
    }

    public static void main(String[] args) {
        new LoginUI();
    }
}
