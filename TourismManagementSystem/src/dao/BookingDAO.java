package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.Booking;
import utils.DatabaseConnection;

public class BookingDAO {
    
    public boolean createBooking(Booking booking) {
        String sql = "INSERT INTO bookings (user_id, package_id, booking_date, travel_date, num_travelers, total_price, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, booking.getUserId());
            stmt.setInt(2, booking.getPackageId());
            stmt.setDate(3, new java.sql.Date(booking.getBookingDate().getTime()));
            stmt.setDate(4, new java.sql.Date(booking.getTravelDate().getTime()));
            stmt.setInt(5, booking.getNumTravelers());
            stmt.setDouble(6, booking.getTotalPrice());
            stmt.setString(7, booking.getStatus());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        booking.setBookingId(rs.getInt(1));
                    }
                }
                
                // Update available seats in the package
                PackageDAO packageDAO = new PackageDAO();
                packageDAO.updateAvailableSeats(booking.getPackageId(), booking.getNumTravelers());
                
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public List<Booking> getBookingsByUserId(int userId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, p.name as package_name FROM bookings b JOIN packages p ON b.package_id = p.package_id WHERE b.user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Booking booking = new Booking();
                    booking.setBookingId(rs.getInt("booking_id"));
                    booking.setUserId(rs.getInt("user_id"));
                    booking.setPackageId(rs.getInt("package_id"));
                    booking.setBookingDate(rs.getDate("booking_date"));
                    booking.setTravelDate(rs.getDate("travel_date"));
                    booking.setNumTravelers(rs.getInt("num_travelers"));
                    booking.setTotalPrice(rs.getDouble("total_price"));
                    booking.setStatus(rs.getString("status"));
                    booking.setPackageName(rs.getString("package_name"));
                    bookings.add(booking);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return bookings;
    }
    
    public Booking getBookingById(int bookingId) {
        String sql = "SELECT b.*, p.name as package_name FROM bookings b JOIN packages p ON b.package_id = p.package_id WHERE b.booking_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, bookingId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Booking booking = new Booking();
                    booking.setBookingId(rs.getInt("booking_id"));
                    booking.setUserId(rs.getInt("user_id"));
                    booking.setPackageId(rs.getInt("package_id"));
                    booking.setBookingDate(rs.getDate("booking_date"));
                    booking.setTravelDate(rs.getDate("travel_date"));
                    booking.setNumTravelers(rs.getInt("num_travelers"));
                    booking.setTotalPrice(rs.getDouble("total_price"));
                    booking.setStatus(rs.getString("status"));
                    booking.setPackageName(rs.getString("package_name"));
                    return booking;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public boolean updateBookingStatus(int bookingId, String status) {
        String sql = "UPDATE bookings SET status = ? WHERE booking_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, bookingId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean cancelBooking(int bookingId) {
        // First get the booking to know how many seats to add back
        Booking booking = getBookingById(bookingId);
        if (booking == null) return false;
        
        String sql = "UPDATE bookings SET status = 'CANCELLED' WHERE booking_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, bookingId);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // Add seats back to the package
                String updatePackageSql = "UPDATE packages SET available_seats = available_seats + ? WHERE package_id = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updatePackageSql)) {
                    updateStmt.setInt(1, booking.getNumTravelers());
                    updateStmt.setInt(2, booking.getPackageId());
                    updateStmt.executeUpdate();
                }
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, p.name as package_name FROM bookings b JOIN packages p ON b.package_id = p.package_id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Booking booking = new Booking();
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setUserId(rs.getInt("user_id"));
                booking.setPackageId(rs.getInt("package_id"));
                booking.setBookingDate(rs.getDate("booking_date"));
                booking.setTravelDate(rs.getDate("travel_date"));
                booking.setNumTravelers(rs.getInt("num_travelers"));
                booking.setTotalPrice(rs.getDouble("total_price"));
                booking.setStatus(rs.getString("status"));
                booking.setPackageName(rs.getString("package_name"));
                bookings.add(booking);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return bookings;
    }
}
