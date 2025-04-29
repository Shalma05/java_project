# 🧳 Tourism Management System

A **Java-based desktop application** for managing tourism packages, hotels, bookings, and user accounts, featuring an intuitive admin panel and a user-friendly interface.

---

## 🚀 Features

- 🔐 User authentication (Login/Register)
- 🏝️ Browse and book tour packages
- 🏨 Browse and book hotels
- 📋 View and manage bookings
- 🛠️ Admin panel to manage:
  - Users
  - Packages
  - Hotels
  - Bookings

---

## 🛠 Technologies Used

- **Java Swing** – GUI components  
- **MySQL** – Relational database  
- **JDBC** – Database connectivity  

---

## 🖥️ Setup Instructions

1. **Install MySQL** and create a database named:
   ```
   tourism_db
   ```
2. Run the SQL script located at:
   ```
   database/tourism_db.sql
   ```
   This will set up the database schema and insert sample data.

3. Update the database connection details (host, username, password) in:
   ```
   src/utils/DatabaseConnection.java
   ```

4. Compile and run the application using the `Main` class located at:
   ```
   src/main/Main.java
   ```

---

## 🔐 Default Admin Login

- **Username:** `admin`  
- **Password:** `admin123`

---

## 📁 Project Structure

```
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
│   │   ├── PackagesPanel.java
│   │   ├── BookPackageFrame.java
│   │   ├── BrowseHotelsPanel.java
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
```

---

## 🔮 Future Enhancements

- 💳 **Payment gateway integration**
- 📧 **Email notifications for bookings**
- 🌟 **User reviews and ratings**
- 🔎 **Advanced search and filtering**
- 📊 **Reports and analytics for admin panel**

---

## 📌 License

This project is for educational purposes. Please customize and extend it as needed.

---

## 🙌 Acknowledgements

Thanks to all open-source contributors and Java desktop GUI resources that inspired this project.

---
