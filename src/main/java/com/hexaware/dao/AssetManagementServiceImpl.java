package com.hexaware.dao;

import com.hexaware.entity.Asset;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AssetManagementServiceImpl implements AssetManagementService {
    private final Connection connection;

    public AssetManagementServiceImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addAsset(Asset asset) {
        String query = "INSERT INTO assets (name, type, serial_number, purchase_date, location, status, owner_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, asset.getName());
            pstmt.setString(2, asset.getType());
            pstmt.setString(3, asset.getSerialNumber());
            pstmt.setDate(4, java.sql.Date.valueOf(asset.getPurchaseDate()));
            pstmt.setString(5, asset.getLocation());
            pstmt.setString(6, asset.getStatus());
            pstmt.setInt(7, asset.getOwnerId());
            pstmt.executeUpdate();

            // Retrieve the generated asset ID
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                asset.setAssetId(generatedKeys.getInt(1)); // Set the generated ID back to the asset
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    @Override
    public boolean updateAsset(Asset asset) {
        String query = "UPDATE assets SET name = ?, type = ?, serial_number = ?, purchase_date = ?, location = ?, status = ?, owner_id = ? WHERE asset_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, asset.getName());
            pstmt.setString(2, asset.getType());
            pstmt.setString(3, asset.getSerialNumber());
            pstmt.setDate(4, java.sql.Date.valueOf(asset.getPurchaseDate()));
            pstmt.setString(5, asset.getLocation());
            pstmt.setString(6, asset.getStatus());
            pstmt.setInt(7, asset.getOwnerId());
            pstmt.setInt(8, asset.getAssetId());

            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;  // Returns true if the asset was updated
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteAsset(int assetId) {
        String query = "DELETE FROM assets WHERE asset_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, assetId);
            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean allocateAsset(int assetId, int employeeId, String allocationDate) {
        String query = "INSERT INTO asset_allocations (asset_id, employee_id, allocation_date) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, assetId);
            pstmt.setInt(2, employeeId);
            pstmt.setDate(3, java.sql.Date.valueOf(allocationDate));
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deallocateAsset(int assetId, int employeeId, String returnDate) {
        String query = "UPDATE asset_allocations SET return_date = ? WHERE asset_id = ? AND employee_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setDate(1, java.sql.Date.valueOf(returnDate));
            pstmt.setInt(2, assetId);
            pstmt.setInt(3, employeeId);
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean performMaintenance(int assetId, String maintenanceDate, String description, double cost) {
        String query = "INSERT INTO maintenance_records (asset_id, maintenance_date, description, cost) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, assetId);
            pstmt.setDate(2, java.sql.Date.valueOf(maintenanceDate));
            pstmt.setString(3, description);
            pstmt.setDouble(4, cost);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean reserveAsset(int assetId, int employeeId, String reservationDate, String startDate, String endDate) {
        String query = "INSERT INTO reservations (asset_id, employee_id, reservation_date, start_date, end_date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, assetId);
            pstmt.setInt(2, employeeId);
            pstmt.setDate(3, java.sql.Date.valueOf(reservationDate));
            pstmt.setDate(4, java.sql.Date.valueOf(startDate));
            pstmt.setDate(5, java.sql.Date.valueOf(endDate));
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean withdrawReservation(int reservationId) {
        String query = "DELETE FROM reservations WHERE reservation_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, reservationId);
            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Asset getAssetById(int assetId) {
        String query = "SELECT * FROM assets WHERE asset_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, assetId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Asset(
                        rs.getInt("asset_id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("serial_number"),
                        rs.getDate("purchase_date").toLocalDate(),
                        rs.getString("location"),
                        rs.getString("status"),
                        rs.getInt("owner_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Asset> getAllAssets() {
        String query = "SELECT * FROM assets";
        List<Asset> assets = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Asset asset = new Asset(
                        rs.getInt("asset_id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("serial_number"),
                        rs.getDate("purchase_date").toLocalDate(),
                        rs.getString("location"),
                        rs.getString("status"),
                        rs.getInt("owner_id")
                );
                assets.add(asset);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assets;
    }
}
