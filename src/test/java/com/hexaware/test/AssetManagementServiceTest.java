package com.hexaware.test;
import com.hexaware.dao.AssetManagementService;
import com.hexaware.dao.AssetManagementServiceImpl;
import com.hexaware.entity.Asset;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;



public class AssetManagementServiceTest<AssetManagementServiceImpl> {
    private Connection connection;
    private AssetManagementServiceImpl service;

    @BeforeEach
    public void setUp() throws SQLException {
        // Update these values to your actual database name, username, and password
        String dbName = "DigitalAssetManagement"; // Replace with your actual database name
        String user = "root"; // Replace with your actual username
        String password = "aaaa"; // Replace with your actual password

        // Initialize the database connection
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName, user, password);

        // Pass the connection to the service constructor

    }
    @Test
    public void testSomeFunctionality() {
        // Your test cases go here
        // For example, assertEquals(expected, actual);
    }





    @AfterEach
    public void tearDown() throws SQLException {
        if (connection != null) {
            connection.close(); // Close the connection after the tests
        }
    }
}
