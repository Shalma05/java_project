# Tourism Management System

A Java-based desktop application for managing tourism packages, hotels, bookings, and users.

## Features

- User authentication (login/register)
- Browse and book tour packages
- Browse and book hotels
- View and manage bookings
- Admin panel for managing users, packages, hotels, and bookings

## Technologies Used

- Java Swing for the GUI
- MySQL for the database
- JDBC for database connectivity

## Setup Instructions

1. Install MySQL and create a database named `tourism_db`
2. Run the SQL script in `database/tourism_db.sql` to set up the database schema and sample data
3. Update the database connection details in `src/utils/DatabaseConnection.java` if needed
4. Compile and run the application using the Main class

## Default Admin Login

- Username: admin
- Password: admin123

## Project Structure

- `src/models`: Contains all the data models
- `src/dao`: Data Access Objects for database operations
- `src/gui`: All GUI components
- `src/utils`: Utility classes for database connection and date handling

## Screenshots

(Add screenshots of your application here)

## Future Enhancements

- Payment gateway integration
- Email notifications for bookings
- User reviews and ratings
- Advanced search and filtering options
- Reports and analytics for admin
