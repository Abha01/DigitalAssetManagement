package com.hexaware.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class DBConnUtil {

    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (connection == null) {
                // Load database properties from a properties file
                Properties props = new Properties();
                InputStream input = DBConnUtil.class.getClassLoader().getResourceAsStream("db.properties");

                if (input == null) {
                    throw new IOException("db.properties file not found");
                }

                props.load(input);
                System.out.println("Database properties loaded successfully.");

                // Ensure URL is retrieved correctly
                String url = props.getProperty("db.url");
                if (url == null) {
                    throw new SQLException("The url cannot be null", "08001");
                }

                String user = props.getProperty("db.user");
                String password = props.getProperty("db.password");

                // Load the MySQL JDBC driver
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Establish the connection
                connection = DriverManager.getConnection(url, user, password);
                System.out.println("Database connection established successfully."); // Confirmation message
            }
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver class not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("SQL exception occurred: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("I/O exception occurred: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }
}
