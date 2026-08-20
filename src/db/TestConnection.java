package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestConnection {

    public static void main(String[] args) {

        try {

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/hospital_db",
                    "root",
                    "Kalpana1979*"
            );

            System.out.println("Database Connected Successfully");

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}