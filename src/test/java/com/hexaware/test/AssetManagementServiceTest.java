package com.hexaware.test;

import com.hexaware.dao.AssetManagementService;
import com.hexaware.dao.AssetManagementServiceImpl;
import com.hexaware.entity.Asset;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class AssetManagementServiceTest {
    private Connection connection;
    private AssetManagementService service;
    private int reservationId;

    @BeforeEach
    public void setUp() throws SQLException {
        // Update these values to your actual database name, username, and password
        String dbName = "DigitalAssetManagement"; // Replace with your actual database name
        String user = "root"; // Replace with your actual username
        String password = "aaaa"; // Replace with your actual password

        // Initialize the database connection
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName, user, password);

        // Pass the connection to the service constructor
        service = new AssetManagementServiceImpl(connection);

        // Set auto-commit to false for the transaction
        connection.setAutoCommit(false); // Start transaction

        // Insert a test reservation to ensure it exists for the withdrawal test
        String insertQuery = "INSERT INTO reservations (asset_id, employee_id, reservation_date, start_date, end_date, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement insertPstmt = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            insertPstmt.setInt(1, 1); // Assuming asset_id = 1
            insertPstmt.setInt(2, 1); // Assuming employee_id = 1
            insertPstmt.setDate(3, java.sql.Date.valueOf("2024-10-01")); // Example reservation date
            insertPstmt.setDate(4, java.sql.Date.valueOf("2024-10-05")); // Example start date
            insertPstmt.setDate(5, java.sql.Date.valueOf("2024-10-10")); // Example end date
            insertPstmt.setString(6, "Active");

            int rowsInserted = insertPstmt.executeUpdate();
            System.out.println("Rows inserted: " + rowsInserted); // Log the number of rows inserted

            // Retrieve the generated key for the inserted reservation
            ResultSet generatedKeys = insertPstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                reservationId = generatedKeys.getInt(1); // Store the generated reservation ID
            }
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to insert test reservation");
        }
    }

    @Test
    public void testAddAsset() throws SQLException {
        // Create an Asset object with test values
        Asset asset = new Asset(0, "Laptop", "Electronics", "SN12345", LocalDate.now(), "Office", "Available", 1);

        // Add the asset to the database
        service.addAsset(asset);

        // After adding, fetch the asset by its ID
        Asset addedAsset = service.getAssetById(asset.getAssetId());

        // Verify that the asset was added and fetched successfully
        assertNotNull(addedAsset, "The added asset should not be null");
        assertEquals("Laptop", addedAsset.getName(), "Asset name should match the added asset");
        assertEquals("SN12345", addedAsset.getSerialNumber(), "Serial number should match the added asset");
        assertEquals("Available", addedAsset.getStatus(), "Asset status should be 'Available'");
        assertEquals(1, addedAsset.getOwnerId(), "Owner ID should match");
    }

    @Test
    public void testUpdateAsset() throws SQLException {
        // First, create and add a new asset
        Asset newAsset = new Asset(0, "Monitor", "Electronics", "SN67890", LocalDate.now(), "Office", "Available", 2);
        service.addAsset(newAsset);

        // Fetch the asset to get the assigned asset ID
        Asset addedAsset = service.getAssetById(newAsset.getAssetId());
        assertNotNull(addedAsset, "The asset should exist before the update");

        // Now, update the asset details
        addedAsset.setName("Updated Monitor");
        addedAsset.setType("Updated Electronics");
        addedAsset.setSerialNumber("SN67890U");
        addedAsset.setPurchaseDate(LocalDate.of(2022, 1, 10)); // updating purchase date
        addedAsset.setLocation("Updated Office");
        addedAsset.setStatus("In Use");
        addedAsset.setOwnerId(3); // updating owner id

        // Perform the update operation
        boolean isUpdated = service.updateAsset(addedAsset);

        // Fetch the updated asset to verify
        Asset updatedAsset = service.getAssetById(addedAsset.getAssetId());

        // Verify that the asset was updated correctly
        assertNotNull(updatedAsset, "The updated asset should not be null");
        assertEquals("Updated Monitor", updatedAsset.getName(), "Asset name should be updated");
        assertEquals("Updated Electronics", updatedAsset.getType(), "Asset type should be updated");
        assertEquals("SN67890U", updatedAsset.getSerialNumber(), "Asset serial number should be updated");
        assertEquals(LocalDate.of(2022, 1, 10), updatedAsset.getPurchaseDate(), "Purchase date should be updated");
        assertEquals("Updated Office", updatedAsset.getLocation(), "Asset location should be updated");
        assertEquals("In Use", updatedAsset.getStatus(), "Asset status should be updated");
        assertEquals(3, updatedAsset.getOwnerId(), "Owner ID should be updated");
    }

    @Test
    public void testAllocateAsset() throws SQLException {
        // Assume asset and employee exist
        int assetId = 1; // existing asset ID
        int employeeId = 2; // existing employee ID

        // Allocate the asset to the employee
        boolean isAllocated = service.allocateAsset(assetId, employeeId, String.valueOf(LocalDate.now()));

        // Fetch the asset and verify allocation
        Asset allocatedAsset = service.getAssetById(assetId);
        assertEquals("Allocated", allocatedAsset.getStatus(), "Asset status should be 'Allocated'");
    }

    @Test
    public void testDeallocateAsset() throws SQLException {
        // Assuming an asset was previously allocated to employeeId 1
        int assetId = 1;
        int employeeId = 1;
        String allocationDate = "2024-10-01";
        String returnDate = "2024-10-10";

        // First, allocate the asset (assuming the allocation method works as expected)
        service.allocateAsset(assetId, employeeId, allocationDate);

        // Deallocate the asset by setting a return date
        boolean isDeallocated = service.deallocateAsset(assetId, employeeId, returnDate);

        // Check if the return date is correctly updated in the database
        String query = "SELECT return_date FROM asset_allocations WHERE asset_id = ? AND employee_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, assetId);
            pstmt.setInt(2, employeeId);
            ResultSet resultSet = pstmt.executeQuery();

            // Check if the record exists
            if (resultSet.next()) {
                java.sql.Date actualReturnDate = resultSet.getDate("return_date");
                assertEquals(java.sql.Date.valueOf(returnDate), actualReturnDate, "Return date should match the provided value");
            } else {
                fail("Asset allocation record not found for the provided assetId and employeeId.");
            }
        }
    }

    @Test
    public void testPerformMaintenance() throws SQLException {
        // Assume asset exists
        int assetId = 1; // existing asset ID

        // Perform maintenance on the asset
        boolean isMaintained = service.performMaintenance(assetId, String.valueOf(LocalDate.now()), "Replaced power supply", 150.00);

        // Verify that the maintenance record was added
        assertTrue(isMaintained, "The maintenance should be recorded successfully");
    }

    @Test
    public void testReserveAsset() throws SQLException {
        // Assume asset and employee exist
        int assetId = 1; // existing asset ID
        int employeeId = 2; // existing employee ID

        // Reserve the asset for the employee
        boolean isReserved = service.reserveAsset(assetId, employeeId, String.valueOf(LocalDate.now()), String.valueOf(LocalDate.now().plusDays(3)), String.valueOf(LocalDate.now().plusDays(7)));

        // Verify the reservation
        assertTrue(isReserved, "The asset should be reserved successfully");
    }

    @Test
    public void testWithdrawReservation() throws SQLException {
        // Verify the reservation exists before withdrawal
        String checkQuery = "SELECT * FROM reservations WHERE reservation_id = ?";
        try (PreparedStatement checkPstmt = connection.prepareStatement(checkQuery)) {
            checkPstmt.setInt(1, reservationId);
            ResultSet checkResultSet = checkPstmt.executeQuery();
            assertTrue(checkResultSet.next(), "Reservation should exist before withdrawal");
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Database query failed while checking reservation existence");
        }

        // Now attempt to withdraw the reservation
        boolean result = service.withdrawReservation(reservationId);
        System.out.println("Withdraw result: " + result); // Log the result of withdrawal
        assertTrue(result, "Reservation should be withdrawn successfully");

        // Verify that the reservation no longer exists in the database
        String query = "SELECT * FROM reservations WHERE reservation_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, reservationId);
            ResultSet resultSet = pstmt.executeQuery();
            assertFalse(resultSet.next(), "Reservation should not exist after withdrawal");
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Database query failed while checking reservation absence");
        }
    }

    @AfterEach
    public void tearDown() {
        try {
            if (connection != null) {
                connection.rollback(); // Rollback transaction to ensure a clean state for next test
                connection.close(); // Close the connection after the tests
            }
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to rollback the transaction or close the connection");
        }
    }
}
