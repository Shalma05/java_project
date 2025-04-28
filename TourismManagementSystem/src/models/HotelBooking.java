package models;

import java.util.Date;

public class HotelBooking {
    private int hotelBookingId;
    private int userId;
    private int hotelId;
    private Date bookingDate;
    private Date checkInDate;
    private Date checkOutDate;
    private int numRooms;
    private double totalPrice;
    private String status;
    private String hotelName; // For display purposes
    
    // Constructors
    public HotelBooking() {}
    
    public HotelBooking(int hotelBookingId, int userId, int hotelId, Date bookingDate, Date checkInDate, Date checkOutDate, int numRooms, double totalPrice, String status) {
        this.hotelBookingId = hotelBookingId;
        this.userId = userId;
        this.hotelId = hotelId;
        this.bookingDate = bookingDate;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numRooms = numRooms;
        this.totalPrice = totalPrice;
        this.status = status;
    }
    
    // Getters and Setters
    public int getHotelBookingId() {
        return hotelBookingId;
    }
    
    public void setHotelBookingId(int hotelBookingId) {
        this.hotelBookingId = hotelBookingId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public int getHotelId() {
        return hotelId;
    }
    
    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }
    
    public Date getBookingDate() {
        return bookingDate;
    }
    
    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }
    
    public Date getCheckInDate() {
        return checkInDate;
    }
    
    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }
    
    public Date getCheckOutDate() {
        return checkOutDate;
    }
    
    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }
    
    public int getNumRooms() {
        return numRooms;
    }
    
    public void setNumRooms(int numRooms) {
        this.numRooms = numRooms;
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
    
    public String getHotelName() {
        return hotelName;
    }
    
    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }
    
    @Override
    public String toString() {
        return "Hotel Booking #" + hotelBookingId + " - " + hotelName + " - Check-in: " + checkInDate + " - $" + totalPrice;
    }
}
