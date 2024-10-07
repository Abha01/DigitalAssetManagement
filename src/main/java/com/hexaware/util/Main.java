package com.hexaware.util;

import com.hexaware.dao.AssetManagementService;
import com.hexaware.dao.AssetManagementServiceImpl;
import com.hexaware.entity.Asset;

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
                    // Call add asset method
                    Asset assetToAdd = getAssetDetailsFromUser(scanner);
                    assetManagementService.addAsset(assetToAdd);
                    System.out.println("Asset added successfully.");
                    break;
                case 2:
                    System.out.print("Enter asset ID to update: ");
                    int assetId = scanner.nextInt();
                    Asset assetToUpdate = getAssetDetailsFromUser(scanner);
                    assetToUpdate.setAssetId(assetId);
                    boolean isUpdated = assetManagementService.updateAsset(assetToUpdate);
                    if (isUpdated) {
                        System.out.println("Asset updated successfully.");
                    } else {
                        System.out.println("Asset update failed.");
                    }
                    break;
                case 3:
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
                    // Allocate asset logic
                    break;
                case 5:
                    // Deallocate asset logic
                    break;
                case 6:
                    // Perform maintenance logic
                    break;
                case 7:
                    // Reserve asset logic
                    break;
                case 8:
                    // Withdraw reservation logic
                    break;
                case 9:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static Asset getAssetDetailsFromUser(Scanner scanner) {
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

        return new Asset(0, name, type, serialNumber, purchaseDate, location, status, ownerId); // assetId is generated in DB
    }
}
