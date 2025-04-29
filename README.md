🧳 TOURISM MANAGEMENT SYSTEM

A Java-based desktop application for managing tourism packages, hotels, bookings, and user accounts with an intuitive admin panel and user-friendly interface

🚀 FEATURES:

🔐 User authentication (Login/Register)

🏝️ Browse and book tour packages

🏨 Browse and book hotels

📋 View and manage bookings

🛠️ Admin panel to manage:

      *Users

      *Packages

      *Hotels

      *Bookings

🛠 TECHNOLOGIES USED:

      *Java Swing – GUI components

      *MySQL – Database

      *JDBC – Database connectivity
      
SETUP INSTRUCTIONS:

1.Install MySQL and create a database named tourism_db

2.Run the SQL script in database/tourism_db.sql to set up the database schema and sample data

3.Update the database connection details in src/utils/DatabaseConnection.java if needed

4.Compile and run the application using the Main class

DEFAULT ADMIN LOGIN:

•Username: admin

•Password: admin123

PROJECT STRUCTURE:

TourismManagementSystem/
│
├── src/
│   ├── main/
│   │   └── Main.java
│   │
│   ├── models/
│   │   ├── User.java
│   │   ├── Package.java
│   │   ├── Booking.java
│   │   ├── Hotel.java
│   │   └── HotelBooking.java
│   │
│   ├── dao/
│   │   ├── UserDAO.java
│   │   ├── PackageDAO.java
│   │   ├── BookingDAO.java
│   │   ├── HotelDAO.java
│   │   └── HotelBookingDAO.java
│   │
│   ├── gui/
│   │   ├── LoginFrame.java
│   │   ├── RegisterFrame.java
│   │   ├── UserDashboard.java
│   │   ├── AdminDashboard.java
│   │   ├── PackagesPanel.java (or BrowsePackagesPanel.java)
│   │   ├── BookPackageFrame.java
│   │   ├── BrowseHotelsPanel.java (or HotelsPanel.java)
│   │   ├── BookHotelFrame.java
│   │   ├── UserBookingsPanel.java
│   │   ├── UserHotelBookingsPanel.java
│   │   ├── ManageUsersPanel.java
│   │   ├── ManagePackagesPanel.java
│   │   ├── ManageHotelsPanel.java
│   │   ├── ViewBookingsPanel.java
│   │   ├── ViewHotelBookingsPanel.java
│   │   ├── ButtonRenderer.java
│   │   └── ButtonEditor.java
│   │
│   └── utils/
│       ├── DatabaseConnection.java
│       └── DateUtils.java
│
├── lib/
│   ├── mysql-connector-j-8.0.xx.jar
│   └── jcalendar-1.4.jar (optional)
│
└── database/
    └── tourism_db.sql


FUTURE ENHANCEMENTS:

•Payment gateway integration

•Email notifications for bookings

•User reviews and ratings

•Advanced search and filtering options

•Reports and analytics for admin..can you give me in a way to put in the readme file in github


