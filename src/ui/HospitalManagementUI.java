package ui;

import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Main application window for CarePoint Hospital Management System.
 * Features a systematic typography architecture, prominent top-bar Logout,
 * a native system MenuBar with keyboard shortcuts, and strict Role-Based Access Control.
 */
public class HospitalManagementUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private final User currentUser;

    public HospitalManagementUI(User user) {
        this.currentUser = user;

        // Apply systematic look & feel, font anti-aliasing, and typography
        UITheme.setupSystemTheme();

        setTitle("CarePoint Hospital Management System — Logged in as: " +
                user.getFullName() + " (" + user.getRole() + ")");

        setSize(1120, 720);
        setMinimumSize(new Dimension(980, 620));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ================= 1. NATIVE SYSTEM MENU BAR =================
        setupMenuBar();

        // ================= 2. TOP APPLICATION BAR =================
        setupTopBar();

        // ================= 3. ROLE-BASED MODULE TABS =================
        setupModuleTabs();

        setVisible(true);
    }

    /**
     * Builds standard desktop menu bar with File, Account, and Help menus.
     * Guarantees Logout is accessible via keyboard shortcuts (Ctrl+L) and standard menus.
     */
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // File Menu
        JMenu menuFile = new JMenu("File");
        menuFile.setFont(UITheme.FONT_LABEL);
        menuFile.setMnemonic(KeyEvent.VK_F);

        JMenuItem itemLogout = new JMenuItem("🚪 Logout", KeyEvent.VK_L);
        itemLogout.setFont(UITheme.FONT_INPUT);
        itemLogout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        itemLogout.addActionListener(e -> performLogout());

        JMenuItem itemExit = new JMenuItem("❌ Exit Application", KeyEvent.VK_X);
        itemExit.setFont(UITheme.FONT_INPUT);
        itemExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        itemExit.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to exit the application?",
                    "Exit Application",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        menuFile.add(itemLogout);
        menuFile.addSeparator();
        menuFile.add(itemExit);
        menuBar.add(menuFile);

        // Account Menu
        JMenu menuAccount = new JMenu("Account");
        menuAccount.setFont(UITheme.FONT_LABEL);
        menuAccount.setMnemonic(KeyEvent.VK_A);

        JMenuItem itemUserInfo = new JMenuItem("👤 Signed in as: " + currentUser.getFullName() + " [" + currentUser.getRole() + "]");
        itemUserInfo.setFont(UITheme.FONT_INPUT);
        itemUserInfo.setEnabled(false); // Display only

        JMenuItem itemSwitchRole = new JMenuItem("🔄 Switch Role / Logout...", KeyEvent.VK_S);
        itemSwitchRole.setFont(UITheme.FONT_INPUT);
        itemSwitchRole.addActionListener(e -> performLogout());

        menuAccount.add(itemUserInfo);
        menuAccount.addSeparator();
        menuAccount.add(itemSwitchRole);
        menuBar.add(menuAccount);

        // Help Menu
        JMenu menuHelp = new JMenu("Help");
        menuHelp.setFont(UITheme.FONT_LABEL);
        menuHelp.setMnemonic(KeyEvent.VK_H);

        JMenuItem itemAbout = new JMenuItem("ℹ️ About CarePoint HMS...", KeyEvent.VK_B);
        itemAbout.setFont(UITheme.FONT_INPUT);
        itemAbout.addActionListener(e -> showAboutDialog());

        menuHelp.add(itemAbout);
        menuBar.add(menuHelp);

        setJMenuBar(menuBar);
    }

    /**
     * Top header panel displaying hospital branding, user profile pill, and red Logout button.
     */
    private void setupTopBar() {
        JPanel topBar = new JPanel(new BorderLayout(15, 0));
        topBar.setBackground(UITheme.COLOR_TOPBAR);
        topBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.COLOR_BORDER),
                new EmptyBorder(10, 18, 10, 18)
        ));

        // Left Section: Logo + User Info Pill
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        leftPanel.setOpaque(false);

        JLabel lblBrand = new JLabel("🏥 CarePoint HMS");
        lblBrand.setFont(UITheme.FONT_TITLE);
        lblBrand.setForeground(new Color(15, 23, 42)); // Slate 900

        JLabel lblDivider = new JLabel("|");
        lblDivider.setFont(UITheme.FONT_TITLE);
        lblDivider.setForeground(new Color(203, 213, 225));

        JLabel lblUserName = new JLabel("👤 " + currentUser.getFullName());
        lblUserName.setFont(UITheme.FONT_LABEL);
        lblUserName.setForeground(UITheme.COLOR_TEXT_MAIN);

        // Role Badge Pill
        JLabel lblRoleBadge = new JLabel(" " + currentUser.getRole().toUpperCase() + " ");
        lblRoleBadge.setFont(UITheme.FONT_BADGE);
        lblRoleBadge.setOpaque(true);
        lblRoleBadge.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(getRoleBorderColor(currentUser.getRole()), 1, true),
                new EmptyBorder(2, 6, 2, 6)
        ));
        lblRoleBadge.setBackground(getRoleBgColor(currentUser.getRole()));
        lblRoleBadge.setForeground(getRoleTextColor(currentUser.getRole()));

        leftPanel.add(lblBrand);
        leftPanel.add(lblDivider);
        leftPanel.add(lblUserName);
        leftPanel.add(lblRoleBadge);

        // Right Section: Logout Button
        JButton btnLogout = new JButton("🚪 Logout");
        btnLogout.setFont(UITheme.FONT_BUTTON);
        btnLogout.setBackground(UITheme.COLOR_DANGER);
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.setToolTipText("Log out and return to role login portal (Ctrl+L)");
        btnLogout.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.COLOR_DANGER_HOVER, 1),
                new EmptyBorder(6, 16, 6, 16)
        ));

        // Hover Effect
        btnLogout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnLogout.setBackground(UITheme.COLOR_DANGER_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnLogout.setBackground(UITheme.COLOR_DANGER);
            }
        });

        btnLogout.addActionListener(e -> performLogout());

        topBar.add(leftPanel, BorderLayout.WEST);
        topBar.add(btnLogout, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);
    }

    /**
     * Initializes role-specific tabs with systematic fonts.
     */
    private void setupModuleTabs() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(UITheme.FONT_BUTTON);
        tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        String role = currentUser.getRole();

        if (role.equalsIgnoreCase("Admin")) {
            // Admin gets all modules
            tabs.add("Dashboard", new DashboardPanel());
            tabs.add("Patients", new PatientPanel());
            tabs.add("Doctors", new DoctorPanel());
            tabs.add("Doctor Schedule", new DoctorSchedulePanel());
            tabs.add("Appointments", new AppointmentPanel());
            tabs.add("Prescriptions", new PrescriptionPanel());
            tabs.add("Feedback", new FeedbackPanel());
            tabs.add("Billing", new BillingPanel());
            tabs.add("Emergency", new EmergencyPanel());
            tabs.add("Lab Tests", new LabTestPanel());
            tabs.add("Bed Allocation", new BedAllocationPanel());
            tabs.add("Reports", new ReportsPanel());

        } else if (role.equalsIgnoreCase("Doctor")) {
            // Doctor gets consultations, timetable, prescriptions, lab tests, and patients
            tabs.add("Doctor Schedule", new DoctorSchedulePanel());
            tabs.add("Appointments", new AppointmentPanel());
            tabs.add("Prescriptions", new PrescriptionPanel());
            tabs.add("Lab Tests", new LabTestPanel());
            tabs.add("Patients", new PatientPanel());
            tabs.add("Emergency", new EmergencyPanel());
            tabs.add("Feedback", new FeedbackPanel());

        } else if (role.equalsIgnoreCase("Patient")) {
            // Patient gets personal medical history, appointments, prescriptions, and feedback
            tabs.add("My Medical History", new PatientPortalPanel(currentUser));
            tabs.add("Appointments", new AppointmentPanel());
            tabs.add("Prescriptions", new PrescriptionPanel());
            tabs.add("Billing", new BillingPanel());
            tabs.add("Submit Feedback", new FeedbackPanel());

        } else {
            // Receptionist gets front-desk management
            tabs.add("Dashboard", new DashboardPanel());
            tabs.add("Patients", new PatientPanel());
            tabs.add("Appointments", new AppointmentPanel());
            tabs.add("Billing", new BillingPanel());
            tabs.add("Bed Allocation", new BedAllocationPanel());
            tabs.add("Emergency", new EmergencyPanel());
            tabs.add("Lab Tests", new LabTestPanel());
            tabs.add("Feedback", new FeedbackPanel());
        }

        add(tabs, BorderLayout.CENTER);
    }

    /**
     * Clean logout handler that prompts for confirmation and returns to LoginUI.
     */
    public void performLogout() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to log out of CarePoint Hospital Management System?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginUI();
        }
    }

    private void showAboutDialog() {
        String msg = "<html><div style='padding:5px; font-family: Segoe UI, sans-serif;'>" +
                "<h2 style='color:#0F766E; margin-bottom:4px;'>🏥 CarePoint Hospital Management System</h2>" +
                "<p><b>Version:</b> 2.0 (Dual-Client Enterprise Edition)</p>" +
                "<p><b>Architecture:</b> Java Swing + JDBC &bull; React 19 + Node.js REST API &bull; MySQL 8.0</p>" +
                "<p><b>Logged in as:</b> " + currentUser.getFullName() + " (" + currentUser.getRole() + ")</p>" +
                "<hr style='border: 0; border-top: 1px solid #CBD5E1; margin: 8px 0;'/>" +
                "<p style='color:#64748B; font-size:11px;'>Developed for Academic & Clinical Evaluation &bull; 2026</p>" +
                "</div></html>";

        JOptionPane.showMessageDialog(this, msg, "About CarePoint HMS", JOptionPane.INFORMATION_MESSAGE);
    }

    private Color getRoleBgColor(String role) {
        if (role.equalsIgnoreCase("Doctor")) return new Color(204, 251, 241);     // Teal-100
        if (role.equalsIgnoreCase("Patient")) return new Color(237, 233, 254);    // Purple-100
        if (role.equalsIgnoreCase("Receptionist")) return new Color(224, 242, 254);// Sky-100
        return new Color(226, 232, 240);                                          // Slate-200 (Admin)
    }

    private Color getRoleBorderColor(String role) {
        if (role.equalsIgnoreCase("Doctor")) return new Color(153, 246, 228);
        if (role.equalsIgnoreCase("Patient")) return new Color(221, 214, 254);
        if (role.equalsIgnoreCase("Receptionist")) return new Color(186, 230, 253);
        return new Color(203, 213, 225);
    }

    private Color getRoleTextColor(String role) {
        if (role.equalsIgnoreCase("Doctor")) return new Color(15, 118, 110);
        if (role.equalsIgnoreCase("Patient")) return new Color(109, 40, 217);
        if (role.equalsIgnoreCase("Receptionist")) return new Color(2, 132, 199);
        return new Color(15, 23, 42);
    }

    public static void main(String[] args) {
        UITheme.setupSystemTheme();
        new LoginUI();
    }
}
