package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import models.Package;
import utils.DatabaseConnection;

public class PackageDAO {
    
    public List<Package> getAllPackages() {
        List<Package> packages = new ArrayList<>();
        String sql = "SELECT * FROM packages";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Package pkg = new Package();
                pkg.setPackageId(rs.getInt("package_id"));
                pkg.setName(rs.getString("name"));
                pkg.setDescription(rs.getString("description"));
                pkg.setDestination(rs.getString("destination"));
                pkg.setDuration(rs.getInt("duration"));
                pkg.setPrice(rs.getDouble("price"));
                pkg.setAvailableSeats(rs.getInt("available_seats"));
                packages.add(pkg);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return packages;
    }
    
    public Package getPackageById(int packageId) {
        String sql = "SELECT * FROM packages WHERE package_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, packageId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Package pkg = new Package();
                    pkg.setPackageId(rs.getInt("package_id"));
                    pkg.setName(rs.getString("name"));
                    pkg.setDescription(rs.getString("description"));
                    pkg.setDestination(rs.getString("destination"));
                    pkg.setDuration(rs.getInt("duration"));
                    pkg.setPrice(rs.getDouble("price"));
                    pkg.setAvailableSeats(rs.getInt("available_seats"));
                    return pkg;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public boolean addPackage(Package pkg) {
        String sql = "INSERT INTO packages (name, description, destination, duration, price, available_seats) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, pkg.getName());
            stmt.setString(2, pkg.getDescription());
            stmt.setString(3, pkg.getDestination());
            stmt.setInt(4, pkg.getDuration());
            stmt.setDouble(5, pkg.getPrice());
            stmt.setInt(6, pkg.getAvailableSeats());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        pkg.setPackageId(rs.getInt(1));
                    }
                }
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean updatePackage(Package pkg) {
        String sql = "UPDATE packages SET name = ?, description = ?, destination = ?, duration = ?, price = ?, available_seats = ? WHERE package_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, pkg.getName());
            stmt.setString(2, pkg.getDescription());
            stmt.setString(3, pkg.getDestination());
            stmt.setInt(4, pkg.getDuration());
            stmt.setDouble(5, pkg.getPrice());
            stmt.setInt(6, pkg.getAvailableSeats());
            stmt.setInt(7, pkg.getPackageId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deletePackage(int packageId) {
        String sql = "DELETE FROM packages WHERE package_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, packageId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateAvailableSeats(int packageId, int seatsToReduce) {
        String sql = "UPDATE packages SET available_seats = available_seats - ? WHERE package_id = ? AND available_seats >= ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, seatsToReduce);
            stmt.setInt(2, packageId);
            stmt.setInt(3, seatsToReduce);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
