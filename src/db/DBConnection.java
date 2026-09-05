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
                PASSWORD = props.getProperty("db.password").trim();
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

        System.out.println("DBConnection class loaded");

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.println("Database Connected Successfully");

            return con;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;
    }
}