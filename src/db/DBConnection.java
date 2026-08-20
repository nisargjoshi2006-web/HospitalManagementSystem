package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL =
        "jdbc:mysql://localhost:3306/hospital";

    private static final String USER = "root";

    private static final String PASSWORD = "Kalpana1979*";

    public static Connection getConnection() {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                    URL,
                    USER,
                    PASSWORD
            );

            System.out.println("Database Connected Successfully");

            return con;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}