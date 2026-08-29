package db;

import java.sql.Connection;

public class TestConnection {

    public static void main(String[] args) {

        Connection con = DBConnection.getConnection();

        if(con != null)
            System.out.println("Connected");
        else
            System.out.println("Not Connected");
    }
}