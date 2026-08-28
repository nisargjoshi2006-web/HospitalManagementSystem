package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.io.FileInputStream;

public class DBConnection {

    public static Connection getConnection() {

        try {

            Properties props = new Properties();

            props.load(
                new FileInputStream("config.properties")
            );

            String url =
                    props.getProperty("db.url");

            String user =
                    props.getProperty("db.user");

            String password =
                    props.getProperty("db.password");

            Class.forName(
                    "com.mysql.cj.jdbc.Driver"
            );

            Connection con =
                    DriverManager.getConnection(
                            url,
                            user,
                            password
                    );

            System.out.println(
                    "Database Connected Successfully"
            );

            return con;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;
    }
}