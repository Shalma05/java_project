package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.HotelBooking;
import utils.DatabaseConnection;

public class HotelBookingDAO {
    
    public boolean createHotelBooking(HotelBooking booking) {
        String sql = "INSERT INTO hotel_bookings (user_id, hotel_id, booking_date, check_in_date, check_out_date, num_rooms, total_price, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, booking.getUserId());
            stmt.setInt(2, booking.getHotelId());
            stmt.setDate(3, new java.sql.Date(booking.getBookingDate().getTime()));
            stmt.setDate(4, new java.sql.Date(booking.getCheckInDate().getTime()));
            stmt.setDate(5, new java.sql.Date(booking.getCheckOutDate().getTime()));
            stmt.setInt(6, booking.getNumRooms());
            stmt.setDouble(7, booking.getTotalPrice());
            stmt.setString(8, booking.getStatus());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        booking.setHotelBookingId(rs.getInt(1));
                    }
                }
                
                // Update available rooms in the hotel
                HotelDAO hotelDAO = new HotelDAO();
                hotelDAO.updateAvailableRooms(booking.getHotelId(), booking.getNumRooms());
                
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public List<HotelBooking> getHotelBookingsByUserId(int userId) {
        List<HotelBooking> bookings = new ArrayList<>();
        String sql = "SELECT hb.*, h.name as hotel_name FROM hotel_bookings hb JOIN hotels h ON hb.hotel_id = h.hotel_id WHERE hb.user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    HotelBooking booking = new HotelBooking();
                    booking.setHotelBookingId(rs.getInt("hotel_booking_id"));
                    booking.setUserId(rs.getInt("user_id"));
                    booking.setHotelId(rs.getInt("hotel_id"));
                    booking.setBookingDate(rs.getDate("booking_date"));
                    booking.setCheckInDate(rs.getDate("check_in_date"));
                    booking.setCheckOutDate(rs.getDate("check_out_date"));
                    booking.setNumRooms(rs.getInt("num_rooms"));
                    booking.setTotalPrice(rs.getDouble("total_price"));
                    booking.setStatus(rs.getString("status"));
                    booking.setHotelName(rs.getString("hotel_name"));
                    bookings.add(booking);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return bookings;
    }
    
    public HotelBooking getHotelBookingById(int bookingId) {
        String sql = "SELECT hb.*, h.name as hotel_name FROM hotel_bookings hb JOIN hotels h ON hb.hotel_id = h.hotel_id WHERE hb.hotel_booking_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, bookingId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    HotelBooking booking = new HotelBooking();
                    booking.setHotelBookingId(rs.getInt("hotel_booking_id"));
                    booking.setUserId(rs.getInt("user_id"));
                    booking.setHotelId(rs.getInt("hotel_id"));
                    booking.setBookingDate(rs.getDate("booking_date"));
                    booking.setCheckInDate(rs.getDate("check_in_date"));
                    booking.setCheckOutDate(rs.getDate("check_out_date"));
                    booking.setNumRooms(rs.getInt("num_rooms"));
                    booking.setTotalPrice(rs.getDouble("total_price"));
                    booking.setStatus(rs.getString("status"));
                    booking.setHotelName(rs.getString("hotel_name"));
                    return booking;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public boolean updateHotelBookingStatus(int bookingId, String status) {
        String sql = "UPDATE hotel_bookings SET status = ? WHERE hotel_booking_id = ?";
        
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
    
    public boolean cancelHotelBooking(int bookingId) {
        // First get the booking to know how many rooms to add back
        HotelBooking booking = getHotelBookingById(bookingId);
        if (booking == null) return false;
        
        String sql = "UPDATE hotel_bookings SET status = 'CANCELLED' WHERE hotel_booking_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, bookingId);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // Add rooms back to the hotel
                String updateHotelSql = "UPDATE hotels SET available_rooms = available_rooms + ? WHERE hotel_id = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateHotelSql)) {
                    updateStmt.setInt(1, booking.getNumRooms());
                    updateStmt.setInt(2, booking.getHotelId());
                    updateStmt.executeUpdate();
                }
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public List<HotelBooking> getAllHotelBookings() {
        List<HotelBooking> bookings = new ArrayList<>();
        String sql = "SELECT hb.*, h.name as hotel_name FROM hotel_bookings hb JOIN hotels h ON hb.hotel_id = h.hotel_id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                HotelBooking booking = new HotelBooking();
                booking.setHotelBookingId(rs.getInt("hotel_booking_id"));
                booking.setUserId(rs.getInt("user_id"));
                booking.setHotelId(rs.getInt("hotel_id"));
                booking.setBookingDate(rs.getDate("booking_date"));
                booking.setCheckInDate(rs.getDate("check_in_date"));
                booking.setCheckOutDate(rs.getDate("check_out_date"));
                booking.setNumRooms(rs.getInt("num_rooms"));
                booking.setTotalPrice(rs.getDouble("total_price"));
                booking.setStatus(rs.getString("status"));
                booking.setHotelName(rs.getString("hotel_name"));
                bookings.add(booking);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return bookings;
    }
}

