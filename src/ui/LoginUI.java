package ui;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private int failedAttempts = 0;

    public LoginUI() {

        setTitle("CarePoint Hospital Management System — Unified Healthcare Portal");
        setSize(920, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // ================= 1. TOP HEADER BANNER (VTOP-Style) =================
        JPanel topHeader = new JPanel();
        topHeader.setLayout(new BoxLayout(topHeader, BoxLayout.Y_AXIS));
        topHeader.setBackground(new Color(15, 23, 42)); // Dark Slate / Navy
        topHeader.setBorder(new EmptyBorder(22, 30, 20, 30));

        JLabel lblHospitalTitle = new JLabel("🏥 CarePoint Hospital Management Portal");
        lblHospitalTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblHospitalTitle.setForeground(Color.WHITE);
        lblHospitalTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitle = new JLabel(
                "A digital clinical initiative facilitating Doctors, Receptionists, Patients, and Administrators on one common platform."
        );
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitle.setForeground(new Color(148, 163, 184));
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        topHeader.add(lblHospitalTitle);
        topHeader.add(Box.createVerticalStrut(6));
        topHeader.add(lblSubtitle);
        add(topHeader, BorderLayout.NORTH);

        // ================= 2. CENTER CONTENT (4 ROLE CARDS + FORM) =================
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(248, 250, 252));
        centerPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        // Label above cards
        JLabel lblChooseRole = new JLabel("Select Your Role Portal to Continue");
        lblChooseRole.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblChooseRole.setForeground(new Color(30, 41, 59));
        lblChooseRole.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(lblChooseRole);
        centerPanel.add(Box.createVerticalStrut(15));

        // 4 Role Cards in a Row (GridLayout)
        JPanel cardsGrid = new JPanel(new GridLayout(1, 4, 15, 0));
        cardsGrid.setBackground(new Color(248, 250, 252));
        cardsGrid.setMaximumSize(new Dimension(870, 180));

        // Card 1: Doctor
        cardsGrid.add(createRoleCard(
                "👨‍⚕️",
                "Doctor",
                "Consultations, Timetable & Prescriptions",
                new Color(15, 118, 110), // Teal
                new Color(204, 251, 241),
                () -> fastLogin("doctor", "doc123")
        ));

        // Card 2: Receptionist
        cardsGrid.add(createRoleCard(
                "👩‍💼",
                "Receptionist",
                "Admissions, Appointments & Invoicing",
                new Color(2, 132, 199), // Blue
                new Color(224, 242, 254),
                () -> fastLogin("receptionist", "recep123")
        ));

        // Card 3: Patient
        cardsGrid.add(createRoleCard(
                "🧑‍🤝‍🧑",
                "Patient",
                "Personal Timeline, Reports & Bills",
                new Color(124, 58, 237), // Purple
                new Color(237, 233, 254),
                () -> fastLogin("patient", "patient123")
        ));

        // Card 4: Administrator
        cardsGrid.add(createRoleCard(
                "🛡️",
                "Admin",
                "Full System Access & Analytics",
                new Color(15, 23, 42), // Slate / Navy
                new Color(226, 232, 240),
                () -> fastLogin("admin", "admin123")
        ));

        centerPanel.add(cardsGrid);
        centerPanel.add(Box.createVerticalStrut(25));

        // ================= 3. MANUAL CREDENTIALS LOGIN BOX =================
        JPanel authCard = new JPanel();
        authCard.setLayout(new BoxLayout(authCard, BoxLayout.Y_AXIS));
        authCard.setBackground(Color.WHITE);
        authCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                new EmptyBorder(15, 25, 15, 25)
        ));
        authCard.setMaximumSize(new Dimension(600, 170));

        JLabel lblAuthTitle = new JLabel("Or Sign In with User Credentials");
        lblAuthTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblAuthTitle.setForeground(new Color(71, 85, 105));
        lblAuthTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel formGrid = new JPanel(new GridLayout(2, 2, 10, 10));
        formGrid.setBackground(Color.WHITE);
        formGrid.setMaximumSize(new Dimension(500, 70));

        JLabel lblUsername = new JLabel("Username :");
        lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 13));
        txtUsername = new JTextField("admin");
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel lblPassword = new JLabel("Password :");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 13));
        txtPassword = new JPasswordField("admin123");
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        formGrid.add(lblUsername);
        formGrid.add(txtUsername);
        formGrid.add(lblPassword);
        formGrid.add(txtPassword);

        btnLogin = new JButton("Sign In ➜");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBackground(new Color(15, 118, 110));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(200, 35));

        authCard.add(lblAuthTitle);
        authCard.add(Box.createVerticalStrut(10));
        authCard.add(formGrid);
        authCard.add(Box.createVerticalStrut(12));
        authCard.add(btnLogin);

        centerPanel.add(authCard);
        add(centerPanel, BorderLayout.CENTER);

        // ================= ACTIONS =================
        btnLogin.addActionListener(e -> handleLogin());
        txtPassword.addActionListener(e -> handleLogin());

        setVisible(true);
    }

    /** Helper to build each VTOP-style role card */
    private JPanel createRoleCard(String emoji, String title, String description,
                                  Color themeColor, Color bgColor, Runnable onLogin) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(5, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                new EmptyBorder(12, 12, 12, 12)
        ));

        // Top icon + title
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JLabel lblIcon = new JLabel(emoji);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setForeground(themeColor);

        top.add(lblIcon, BorderLayout.WEST);
        top.add(lblTitle, BorderLayout.CENTER);
        card.add(top, BorderLayout.NORTH);

        // Subtitle / description
        JLabel lblDesc = new JLabel("<html><p style='color:#64748B; font-size:10px;'>" + description + "</p></html>");
        card.add(lblDesc, BorderLayout.CENTER);

        // Action button (like the VTOP arrow button)
        JButton btnRoleLogin = new JButton("Login ➜");
        btnRoleLogin.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRoleLogin.setBackground(themeColor);
        btnRoleLogin.setForeground(Color.WHITE);
        btnRoleLogin.setFocusPainted(false);
        btnRoleLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRoleLogin.addActionListener(e -> onLogin.run());
        card.add(btnRoleLogin, BorderLayout.SOUTH);

        return card;
    }

    /** Fast 1-click login for the selected role */
    private void fastLogin(String username, String password) {
        txtUsername.setText(username);
        txtPassword.setText(password);
        handleLogin();
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
                        "Multiple failed login attempts!\nPlease verify credentials in MySQL Users table.",
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
        // Set System Look & Feel for crisp rendering
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        new LoginUI();
    }
}
