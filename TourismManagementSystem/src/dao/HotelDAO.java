package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import models.Hotel;
import utils.DatabaseConnection;

public class HotelDAO {
    
    public List<Hotel> getAllHotels() {
        List<Hotel> hotels = new ArrayList<>();
        String sql = "SELECT * FROM hotels";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Hotel hotel = new Hotel();
                hotel.setHotelId(rs.getInt("hotel_id"));
                hotel.setName(rs.getString("name"));
                hotel.setLocation(rs.getString("location"));
                hotel.setDescription(rs.getString("description"));
                hotel.setPricePerNight(rs.getDouble("price_per_night"));
                hotel.setRating(rs.getDouble("rating"));
                hotel.setAvailableRooms(rs.getInt("available_rooms"));
                hotels.add(hotel);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return hotels;
    }
    
    public Hotel getHotelById(int hotelId) {
        String sql = "SELECT * FROM hotels WHERE hotel_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, hotelId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Hotel hotel = new Hotel();
                    hotel.setHotelId(rs.getInt("hotel_id"));
                    hotel.setName(rs.getString("name"));
                    hotel.setLocation(rs.getString("location"));
                    hotel.setDescription(rs.getString("description"));
                    hotel.setPricePerNight(rs.getDouble("price_per_night"));
                    hotel.setRating(rs.getDouble("rating"));
                    hotel.setAvailableRooms(rs.getInt("available_rooms"));
                    return hotel;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public boolean addHotel(Hotel hotel) {
        String sql = "INSERT INTO hotels (name, location, description, price_per_night, rating, available_rooms) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, hotel.getName());
            stmt.setString(2, hotel.getLocation());
            stmt.setString(3, hotel.getDescription());
            stmt.setDouble(4, hotel.getPricePerNight());
            stmt.setDouble(5, hotel.getRating());
            stmt.setInt(6, hotel.getAvailableRooms());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        hotel.setHotelId(rs.getInt(1));
                    }
                }
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean updateHotel(Hotel hotel) {
        String sql = "UPDATE hotels SET name = ?, location = ?, description = ?, price_per_night = ?, rating = ?, available_rooms = ? WHERE hotel_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, hotel.getName());
            stmt.setString(2, hotel.getLocation());
            stmt.setString(3, hotel.getDescription());
            stmt.setDouble(4, hotel.getPricePerNight());
            stmt.setDouble(5, hotel.getRating());
            stmt.setInt(6, hotel.getAvailableRooms());
            stmt.setInt(7, hotel.getHotelId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteHotel(int hotelId) {
        String sql = "DELETE FROM hotels WHERE hotel_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, hotelId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateAvailableRooms(int hotelId, int roomsToReduce) {
        String sql = "UPDATE hotels SET available_rooms = available_rooms - ? WHERE hotel_id = ? AND available_rooms >= ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, roomsToReduce);
            stmt.setInt(2, hotelId);
            stmt.setInt(3, roomsToReduce);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
