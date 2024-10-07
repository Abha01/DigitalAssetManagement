package com.hexaware.dao;

import com.hexaware.entity.Asset;

import java.util.List;

public interface AssetManagementService {
    void addAsset(Asset asset);
    boolean updateAsset(Asset asset);
    boolean deleteAsset(int assetId);
    boolean allocateAsset(int assetId, int employeeId, String allocationDate);
    boolean deallocateAsset(int assetId, int employeeId, String returnDate);
    boolean performMaintenance(int assetId, String maintenanceDate, String description, double cost);
    boolean reserveAsset(int assetId, int employeeId, String reservationDate, String startDate, String endDate);
    boolean withdrawReservation(int reservationId);
    Asset getAssetById(int assetId);
    List<Asset> getAllAssets();
}
