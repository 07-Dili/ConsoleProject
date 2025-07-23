# 🚌 Console-Based Bus Ticket Booking System (Java + MySQL)

## 📖 Project Overview

This is a **console-based bus ticket booking system** developed in **Java** with **MySQL** as the backend database. The system allows users to register, log in, book tickets, view or cancel their bookings, and manage their profiles. It also includes an admin module for managing buses, drivers, and user activities.

---

## 💻 Features

### 👤 User Module:
- ✅ User registration and login
- 📅 Book a bus ticket
- 🔍 View available buses
- 🧾 View your bookings
- ❌ Cancel a booking
- 📝 Edit your profile

### 🔧 Admin Module:
- ➕ Add a new bus
- ➕ Add a driver
- 🗑️ Delete a bus
- 👀 View all users
- 📑 View all bookings

---

## 🛠️ Technologies Used

- **Java** (Core Java, JDBC)
- **MySQL** (Relational Database)
- **MYSQL workbench** ( - for MySQL server)
- **VScode** (Recommended IDE)

---

## 🗃️ Database Schema (MySQL)

### 1. `users`  
Stores user information  
Fields: `id`, `name`, `email`, `password`, `phone`

### 2. `buses`  
Stores bus details  
Fields: `bus_id`, `bus_name`, `source`, `destination`, `available_seats`, `driver_id`

### 3. `drivers`  
Stores driver information  
Fields: `driver_id`, `driver_name`, `phone`

### 4. `bookings`  
Stores user bookings  
Fields: `booking_id`, `user_id`, `bus_id`, `seats_booked`, `booking_date`

---

## 🔐 Admin Credentials (Default)

- **Username**: `admin@gmail.com`
- **Password**: `admin12345`

## 🚀 How to Run the Project

1. **Clone the repository** or download the source code.
2. **Create the MySQL database** and import the required tables using the provided SQL script (`database.sql`).
3. Open the project in your preferred Java IDE.
4. Update the **JDBC connection string** with your database credentials in the code.
5. Run the Java application (usually the `Main.java` or `App.java` file).
6. Interact with the system through the terminal.


