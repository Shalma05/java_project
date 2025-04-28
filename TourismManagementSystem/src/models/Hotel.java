package models;

public class Hotel {
    private int hotelId;
    private String name;
    private String location;
    private String description;
    private double pricePerNight;
    private double rating;
    private int availableRooms;
    
    // Constructors
    public Hotel() {}
    
    public Hotel(int hotelId, String name, String location, String description, double pricePerNight, double rating, int availableRooms) {
        this.hotelId = hotelId;
        this.name = name;
        this.location = location;
        this.description = description;
        this.pricePerNight = pricePerNight;
        this.rating = rating;
        this.availableRooms = availableRooms;
    }
    
    // Getters and Setters
    public int getHotelId() {
        return hotelId;
    }
    
    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public double getPricePerNight() {
        return pricePerNight;
    }
    
    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }
    
    public double getRating() {
        return rating;
    }
    
    public void setRating(double rating) {
        this.rating = rating;
    }
    
    public int getAvailableRooms() {
        return availableRooms;
    }
    
    public void setAvailableRooms(int availableRooms) {
        this.availableRooms = availableRooms;
    }
    
    @Override
    public String toString() {
        return name + " - " + location + " ($" + pricePerNight + "/night) - Rating: " + rating;
    }
}

