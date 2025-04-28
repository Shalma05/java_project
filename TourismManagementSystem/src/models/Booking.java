package models;

import java.util.Date;

public class Booking {
    private int bookingId;
    private int userId;
    private int packageId;
    private Date bookingDate;
    private Date travelDate;
    private int numTravelers;
    private double totalPrice;
    private String status;
    private String packageName; // For display purposes
    
    // Constructors
    public Booking() {}
    
    public Booking(int bookingId, int userId, int packageId, Date bookingDate, Date travelDate, int numTravelers, double totalPrice, String status) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.packageId = packageId;
        this.bookingDate = bookingDate;
        this.travelDate = travelDate;
        this.numTravelers = numTravelers;
        this.totalPrice = totalPrice;
        this.status = status;
    }
    
    // Getters and Setters
    public int getBookingId() {
        return bookingId;
    }
    
    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public int getPackageId() {
        return packageId;
    }
    
    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }
    
    public Date getBookingDate() {
        return bookingDate;
    }
    
    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }
    
    public Date getTravelDate() {
        return travelDate;
    }
    
    public void setTravelDate(Date travelDate) {
        this.travelDate = travelDate;
    }
    
    public int getNumTravelers() {
        return numTravelers;
    }
    
    public void setNumTravelers(int numTravelers) {
        this.numTravelers = numTravelers;
    }
    
    public double getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getPackageName() {
        return packageName;
    }
    
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    
    @Override
    public String toString() {
        return "Booking #" + bookingId + " - " + packageName + " - Travel Date: " + travelDate + " - $" + totalPrice;
    }
}
