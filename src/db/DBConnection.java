package db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBConnection {

    private static String URL;
    private static String USER;
    private static String PASSWORD;

    // Load credentials from config.properties on the classpath
    static {
        try {
            Properties props = new Properties();
            InputStream is = DBConnection.class
                    .getClassLoader()
                    .getResourceAsStream("config.properties");

            if (is != null) {
                props.load(is);
                URL      = props.getProperty("db.url");
                USER     = props.getProperty("db.user");
                PASSWORD = props.getProperty("db.password", "").trim();
            } else {
                System.err.println("WARNING: config.properties not found on classpath. Using defaults.");
                URL      = "jdbc:mysql://localhost:3306/hospital";
                USER     = "root";
                PASSWORD = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // DATABASE HEALTH CHECK & DIAGNOSTICS
    public static String checkDatabaseHealth() {
        long start = System.currentTimeMillis();
        try (Connection con = getConnection()) {
            if (con != null && !con.isClosed()) {
                java.sql.DatabaseMetaData meta = con.getMetaData();
                long latency = System.currentTimeMillis() - start;
                return "ONLINE | DBMS: " + meta.getDatabaseProductName() + " " + meta.getDatabaseProductVersion() + " | Latency: " + latency + "ms";
            }
        } catch (Exception e) {
            return "OFFLINE | Error: " + e.getMessage();
        }
        return "OFFLINE | Unable to establish connection";
    }
}
