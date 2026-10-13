package ui;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.Enumeration;

/**
 * UITheme provides a systematic, modern typography and visual design system
 * for the CarePoint Hospital Management System Java Swing client.
 *
 * It configures subpixel text anti-aliasing, native Look & Feel, a coherent
 * typographic scale based on Segoe UI (Windows native) / SansSerif, and standard
 * clinical color palettes.
 */
public final class UITheme {

    // Preferred systematic font family (Segoe UI on Windows, with cross-platform fallback)
    public static final String FONT_FAMILY = detectSystemFontFamily();

    // Standardized Typographic Hierarchy
    public static final Font FONT_TITLE_LARGE  = new Font(FONT_FAMILY, Font.BOLD, 22);
    public static final Font FONT_TITLE        = new Font(FONT_FAMILY, Font.BOLD, 17);
    public static final Font FONT_SUBTITLE     = new Font(FONT_FAMILY, Font.PLAIN, 13);
    public static final Font FONT_SECTION      = new Font(FONT_FAMILY, Font.BOLD, 14);
    public static final Font FONT_LABEL        = new Font(FONT_FAMILY, Font.BOLD, 13);
    public static final Font FONT_INPUT        = new Font(FONT_FAMILY, Font.PLAIN, 13);
    public static final Font FONT_BUTTON       = new Font(FONT_FAMILY, Font.BOLD, 13);
    public static final Font FONT_TABLE_HEADER = new Font(FONT_FAMILY, Font.BOLD, 13);
    public static final Font FONT_TABLE_CELL   = new Font(FONT_FAMILY, Font.PLAIN, 13);
    public static final Font FONT_STATUS       = new Font(FONT_FAMILY, Font.ITALIC, 12);
    public static final Font FONT_BADGE        = new Font(FONT_FAMILY, Font.BOLD, 11);

    // Color Palette
    public static final Color COLOR_PRIMARY       = new Color(15, 118, 110); // Teal 700
    public static final Color COLOR_PRIMARY_HOVER = new Color(17, 94, 89);
    public static final Color COLOR_BG_LIGHT      = new Color(248, 250, 252); // Slate 50
    public static final Color COLOR_TOPBAR        = new Color(241, 245, 249); // Slate 100
    public static final Color COLOR_BORDER        = new Color(226, 232, 240); // Slate 200
    public static final Color COLOR_TEXT_MAIN     = new Color(30, 41, 59);   // Slate 800
    public static final Color COLOR_TEXT_MUTED    = new Color(100, 116, 139); // Slate 500
    public static final Color COLOR_DANGER        = new Color(220, 38, 38);   // Red 600
    public static final Color COLOR_DANGER_HOVER  = new Color(185, 28, 28);   // Red 700
    public static final Color COLOR_SUCCESS       = new Color(22, 163, 74);   // Green 600
    public static final Color COLOR_INFO          = new Color(2, 132, 199);   // Blue 600

    private UITheme() {}

    private static String detectSystemFontFamily() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] families = ge.getAvailableFontFamilyNames();
        for (String f : families) {
            if ("Segoe UI".equalsIgnoreCase(f)) return "Segoe UI";
        }
        for (String f : families) {
            if ("SF Pro Text".equalsIgnoreCase(f) || "Helvetica Neue".equalsIgnoreCase(f)) return f;
        }
        return Font.SANS_SERIF;
    }

    /**
     * Initializes global Look & Feel, subpixel anti-aliasing, and systematic typography.
     */
    public static void setupSystemTheme() {
        // Enable subpixel font antialiasing in AWT/Swing
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Apply systematic default font to all standard Swing component keys in UIManager
        Font defaultBaseFont = new Font(FONT_FAMILY, Font.PLAIN, 13);
        Font boldBaseFont = new Font(FONT_FAMILY, Font.BOLD, 13);
        FontUIResource defaultResource = new FontUIResource(defaultBaseFont);
        FontUIResource boldResource = new FontUIResource(boldBaseFont);

        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource || value instanceof Font) {
                String strKey = key.toString().toLowerCase();
                if (strKey.contains("title") || strKey.contains("bold") || strKey.contains("header") || strKey.contains("button")) {
                    UIManager.put(key, boldResource);
                } else {
                    UIManager.put(key, defaultResource);
                }
            }
        }

        // Specific overrides for crisp, systematic rendering
        UIManager.put("TabbedPane.font", new Font(FONT_FAMILY, Font.BOLD, 13));
        UIManager.put("TableHeader.font", new Font(FONT_FAMILY, Font.BOLD, 13));
        UIManager.put("Table.font", new Font(FONT_FAMILY, Font.PLAIN, 13));
        UIManager.put("Table.rowHeight", 26);
        UIManager.put("Label.font", new Font(FONT_FAMILY, Font.PLAIN, 13));
        UIManager.put("Button.font", new Font(FONT_FAMILY, Font.BOLD, 13));
        UIManager.put("TextField.font", new Font(FONT_FAMILY, Font.PLAIN, 13));
        UIManager.put("PasswordField.font", new Font(FONT_FAMILY, Font.PLAIN, 13));
        UIManager.put("ComboBox.font", new Font(FONT_FAMILY, Font.PLAIN, 13));
        UIManager.put("Menu.font", new Font(FONT_FAMILY, Font.PLAIN, 13));
        UIManager.put("MenuItem.font", new Font(FONT_FAMILY, Font.PLAIN, 13));
        UIManager.put("MenuBar.font", new Font(FONT_FAMILY, Font.PLAIN, 13));
        UIManager.put("OptionPane.messageFont", new Font(FONT_FAMILY, Font.PLAIN, 13));
        UIManager.put("OptionPane.buttonFont", new Font(FONT_FAMILY, Font.BOLD, 13));
    }

    /**
     * Styles a JTable with standard row height, header styling, and subtle grid.
     */
    public static void styleTable(JTable table) {
        table.setFont(FONT_TABLE_CELL);
        table.setRowHeight(28);
        table.setSelectionBackground(new Color(224, 242, 254)); // Light Sky
        table.setSelectionForeground(COLOR_TEXT_MAIN);
        table.setShowGrid(true);
        table.setGridColor(new Color(241, 245, 249));

        JTableHeader header = table.getTableHeader();
        if (header != null) {
            header.setFont(FONT_TABLE_HEADER);
            header.setPreferredSize(new Dimension(header.getWidth(), 32));
            header.setBackground(new Color(241, 245, 249));
            header.setForeground(COLOR_TEXT_MAIN);
        }
    }
}
