package hms;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/hostel_management";

    private static final String USER = "root";

    private static final String PASSWORD = "YOUR_PASSWORD";

    public static Connection getConnection() {

        Connection con = null;

        try {
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database Connected Successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return con;
    }

    public static void main(String[] args) {

        Connection con = DBConnection.getConnection();

        if (con != null) {
            System.out.println("Connection Test Successful!");
        }
    }
}