package com.hexaware.entity;
import java.util.Date;

public class Reservation {

    private int reservationId;
    private int assetId;
    private int employeeId;
    private Date reservationDate;
    private Date startDate;

    // Default constructor
    public Reservation() {
    }

    // Parameterized constructor
    public Reservation(int reservationId, int assetId, int employeeId, Date reservationDate, Date startDate, Date endDate, String status) {
        this.reservationId = reservationId;
        this.assetId = assetId;
        this.employeeId = employeeId;
        this.reservationDate = reservationDate;
        this.startDate = startDate;
    }

    // Getters and Setters
    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public int getAssetId() {
        return assetId;
    }

    public void setAssetId(int assetId) {
        this.assetId = assetId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;

    }
}
