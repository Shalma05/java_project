package models;

public class Package {
    private int packageId;
    private String name;
    private String description;
    private String destination;
    private int duration;
    private double price;
    private int availableSeats;
    
    // Constructors
    public Package() {}
    
    public Package(int packageId, String name, String description, String destination, int duration, double price, int availableSeats) {
        this.packageId = packageId;
        this.name = name;
        this.description = description;
        this.destination = destination;
        this.duration = duration;
        this.price = price;
        this.availableSeats = availableSeats;
    }
    
    // Getters and Setters
    public int getPackageId() {
        return packageId;
    }
    
    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getDestination() {
        return destination;
    }
    
    public void setDestination(String destination) {
        this.destination = destination;
    }
    
    public int getDuration() {
        return duration;
    }
    
    public void setDuration(int duration) {
        this.duration = duration;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public int getAvailableSeats() {
        return availableSeats;
    }
    
    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }
    
    @Override
    public String toString() {
        return name + " - " + destination + " (" + duration + " days) - $" + price;
    }
}
