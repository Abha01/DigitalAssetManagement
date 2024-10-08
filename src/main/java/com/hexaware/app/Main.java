package com.hexaware.app;

import com.hexaware.dao.AssetManagementService;
import com.hexaware.dao.AssetManagementServiceImpl;
import com.hexaware.entity.Asset;
import com.hexaware.util.DBConnUtil;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Connection connection = DBConnUtil.getConnection();
        AssetManagementService assetManagementService = new AssetManagementServiceImpl(connection);

        while (true) {
            System.out.println("1. Add Asset");
            System.out.println("2. Update Asset");
            System.out.println("3. Delete Asset");
            System.out.println("4. Allocate Asset");
            System.out.println("5. Deallocate Asset");
            System.out.println("6. Perform Maintenance");
            System.out.println("7. Reserve Asset");
            System.out.println("8. Withdraw Reservation");
            System.out.println("9. Exit");

            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    // Add asset logic directly
                    System.out.print("Enter Asset Name: ");
                    String name = scanner.next();
                    System.out.print("Enter Asset Type: ");
                    String type = scanner.next();
                    System.out.print("Enter Serial Number: ");
                    String serialNumber = scanner.next();
                    System.out.print("Enter Purchase Date (YYYY-MM-DD): ");
                    LocalDate purchaseDate = LocalDate.parse(scanner.next());
                    System.out.print("Enter Location: ");
                    String location = scanner.next();
                    System.out.print("Enter Status: ");
                    String status = scanner.next();
                    System.out.print("Enter Owner ID: ");
                    int ownerId = scanner.nextInt();

                    Asset assetToAdd = new Asset(0, name, type, serialNumber, purchaseDate, location, status, ownerId);
                    assetManagementService.addAsset(assetToAdd);
                    System.out.println("Asset added successfully.");
                    break;

                case 2:
                    // Update asset logic directly
                    System.out.print("Enter asset ID to update: ");
                    int assetId = scanner.nextInt();
                    System.out.print("Enter new Asset Name: ");
                    String newName = scanner.next();
                    System.out.print("Enter new Asset Type: ");
                    String newType = scanner.next();
                    System.out.print("Enter new Serial Number: ");
                    String newSerialNumber = scanner.next();
                    System.out.print("Enter new Purchase Date (YYYY-MM-DD): ");
                    LocalDate newPurchaseDate = LocalDate.parse(scanner.next());
                    System.out.print("Enter new Location: ");
                    String newLocation = scanner.next();
                    System.out.print("Enter new Status: ");
                    String newStatus = scanner.next();
                    System.out.print("Enter new Owner ID: ");
                    int newOwnerId = scanner.nextInt();

                    Asset assetToUpdate = new Asset(assetId, newName, newType, newSerialNumber, newPurchaseDate, newLocation, newStatus, newOwnerId);
                    boolean isUpdated = assetManagementService.updateAsset(assetToUpdate);
                    if (isUpdated) {
                        System.out.println("Asset updated successfully.");
                    } else {
                        System.out.println("Asset update failed.");
                    }
                    break;

                case 3:
                    // Delete asset logic directly
                    System.out.print("Enter asset ID to delete: ");
                    int deleteId = scanner.nextInt();
                    boolean isDeleted = assetManagementService.deleteAsset(deleteId);
                    if (isDeleted) {
                        System.out.println("Asset deleted successfully.");
                    } else {
                        System.out.println("Asset deletion failed.");
                    }
                    break;

                case 4:
                    // Allocate asset logic directly
                    System.out.print("Enter asset ID to allocate: ");
                    int allocateAssetId = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Enter employee ID to allocate to: ");
                    int employeeId = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Enter allocationDate to allocate to: ");
                    String allocationDate = scanner.nextLine();

                    boolean isAllocated = assetManagementService.allocateAsset(allocateAssetId, employeeId,allocationDate);
                    if (isAllocated) {
                        System.out.println("Asset allocated successfully.");
                    } else {
                        System.out.println("Asset allocation failed.");
                    }
                    break;

                case 5:
                    // Deallocate asset logic directly
                    System.out.print("Enter asset ID to deallocate: ");
                    int deallocateAssetId = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println("Enter employeeId to deallocate: ");
                    int employee_Id = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println("Enter employeeId to deallocate: ");
                    String returnDate = scanner.nextLine();


                    boolean isDeallocated = assetManagementService.deallocateAsset(deallocateAssetId,employee_Id,returnDate);
                    if (isDeallocated) {
                        System.out.println("Asset deallocated successfully.");
                    } else {
                        System.out.println("Asset deallocation failed.");
                    }
                    break;

                case 6:
                    // Perform maintenance logic directly
                    System.out.print("Enter asset ID for maintenance: ");
                    int maintenanceAssetId = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Enter maintenanceDate for maintenance: ");
                    String maintenanceDate= scanner.nextLine();

                    System.out.print("Enter description for maintenance: ");
                    String description= scanner.nextLine();

                    System.out.print("Enter cost for maintenance: ");
                    double cost= scanner.nextDouble();


                    boolean isMaintained = assetManagementService.performMaintenance(maintenanceAssetId, maintenanceDate,  description, cost);
                    if (isMaintained) {
                        System.out.println("Asset maintenance performed successfully.");
                    } else {
                        System.out.println("Asset maintenance failed.");
                    }
                    break;

                case 7:

                    System.out.print("Enter  asset_Id for reservation: ");
                    int asset_Id = scanner.nextInt();
                    scanner.nextLine();


                    System.out.print("Enter  employeeId for reservation: ");
                    int employee_ID = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Enter reservationDate for reservation: ");
                    String reservationDate = scanner.nextLine();

                    System.out.print("Enter startDate for reservation: ");
                    String startDate = scanner.nextLine();

                    System.out.print("Enter endDate for reservation: ");
                    String endDate = scanner.nextLine();

                    boolean isReserved = assetManagementService.reserveAsset( asset_Id,  employee_ID, reservationDate,  startDate, endDate);
                    if (isReserved) {
                        System.out.println("Asset reserved successfully.");
                    } else {
                        System.out.println("Asset reservation failed.");
                    }
                    break;

                case 8:
                    // Withdraw reservation logic directly
                    System.out.print("Enter asset ID to withdraw reservation: ");
                    int withdrawReservationAssetId = scanner.nextInt();

                    boolean isReservationWithdrawn = assetManagementService.withdrawReservation(withdrawReservationAssetId);
                    if (isReservationWithdrawn) {
                        System.out.println("Reservation withdrawn successfully.");
                    } else {
                        System.out.println("Reservation withdrawal failed.");
                    }
                    break;

                case 9:
                    // Exit the program
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
