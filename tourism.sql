-- Create database
CREATE DATABASE IF NOT EXISTS tourist_db;
USE tourist_db;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    address VARCHAR(200),
    is_admin BOOLEAN DEFAULT FALSE
);

-- Create packages table
CREATE TABLE IF NOT EXISTS packages (
    package_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    destination VARCHAR(100) NOT NULL,
    description TEXT,
    duration INT NOT NULL,
    price DOUBLE NOT NULL,
    available_seats INT NOT NULL
);

-- Create bookings table
CREATE TABLE IF NOT EXISTS bookings (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    package_id INT NOT NULL,
    booking_date DATE NOT NULL,
    travel_date DATE NOT NULL,
    num_travelers INT NOT NULL,
    total_price DOUBLE NOT NULL,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (package_id) REFERENCES packages(package_id)
);

-- Create hotels table
CREATE TABLE IF NOT EXISTS hotels (
    hotel_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    location VARCHAR(100) NOT NULL,
    description TEXT,
    price_per_night DOUBLE NOT NULL,
    rating DOUBLE NOT NULL,
    available_rooms INT NOT NULL
);

-- Create hotel bookings table
CREATE TABLE IF NOT EXISTS hotel_bookings (
    hotel_booking_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    hotel_id INT NOT NULL,
    booking_date DATE NOT NULL,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    num_rooms INT NOT NULL,
    total_price DOUBLE NOT NULL,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (hotel_id) REFERENCES hotels(hotel_id)
);

-- Insert admin user
INSERT INTO users (username, password, full_name, email, is_admin)
VALUES ('admin', 'admin123', 'Administrator', 'admin@tourism.com', TRUE);

-- Insert sample packages
INSERT INTO packages (name, destination, description, duration, price, available_seats)
VALUES 
('Paris Adventure', 'Paris, France', 'Experience the romance and beauty of Paris with this 7-day adventure package. Visit the Eiffel Tower, Louvre Museum, and enjoy authentic French cuisine.', 7, 1299.99, 20),
('Tokyo Explorer', 'Tokyo, Japan', 'Discover the fascinating blend of traditional and modern culture in Tokyo. Visit temples, experience the bustling city life, and enjoy delicious Japanese food.', 10, 1899.99, 15),
('New York City Tour', 'New York, USA', 'Explore the Big Apple with this comprehensive city tour. See Times Square, Central Park, the Statue of Liberty, and experience the vibrant nightlife.', 5, 999.99, 25),
('Bali Relaxation', 'Bali, Indonesia', 'Unwind in the tropical paradise of Bali. Enjoy beautiful beaches, lush rice terraces, and traditional Balinese culture and cuisine.', 8, 1499.99, 18),
('African Safari', 'Kenya', 'Experience the wildlife of Africa with this thrilling safari adventure. See lions, elephants, giraffes, and more in their natural habitat.', 12, 2499.99, 10);

-- Insert sample hotels
INSERT INTO hotels (name, location, description, price_per_night, rating, available_rooms)
VALUES 
('Grand Plaza Hotel', 'Paris, France', 'Luxurious 5-star hotel in the heart of Paris with stunning views of the Eiffel Tower.', 299.99, 4.8, 50),
('Tokyo Skyline Hotel', 'Tokyo, Japan', 'Modern hotel with state-of-the-art amenities and convenient access to Tokyo\'s major attractions.', 249.99, 4.5, 75),
('Manhattan Suites', 'New York, USA', 'Elegant suites in the center of Manhattan, offering comfort and convenience for your New York stay.', 279.99, 4.3, 60),
('Bali Beach Resort', 'Bali, Indonesia', 'Beachfront resort with private villas, infinity pools, and world-class spa services.', 199.99, 4.7, 40),
('Safari Lodge', 'Nairobi, Kenya', 'Authentic safari lodge offering comfortable accommodations and guided wildlife tours.', 189.99, 4.6, 30);

select * from users;
select * from packages;
select * from bookings;
select * from hotels;