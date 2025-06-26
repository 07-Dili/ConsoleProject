package Admin;
import DB_Connection.*;
import java.sql.*;

public class HandleDrivers {


    // Add a new driver to the system
    static void addDriver(int driverId, String driverName, long phoneNo, int busId) {
        Connection con = null;
        PreparedStatement pst = null;

        try {
            con = DB.connect();
            if (con == null) {
                System.out.println("Database connection failed. Please try again later.");
                return;
            }

            String query = "INSERT INTO driver (driver_id, driver_name, phone_no, bus_id) VALUES (?, ?, ?, ?)";
            pst = con.prepareStatement(query);
            pst.setInt(1, driverId);
            pst.setString(2, driverName);
            pst.setLong(3, phoneNo);
            pst.setInt(4, busId);

            int rows = pst.executeUpdate();
            if (rows > 0) {
                System.out.println("Driver added successfully.");
            } else {
                System.out.println("Driver addition failed.");
            }
        } catch (SQLException e) {
            System.out.println("Error adding driver: " + e.getMessage());
        } finally {
            try {
                if (pst != null) pst.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    // Delete a driver from the system
    static void deleteDriver(int driverId) {
        Connection con = null;
        PreparedStatement pst = null;

        try {
            con = DB.connect();
            if (con == null) {
                System.out.println("Database connection failed. Please try again later.");
                return;
            }

            String query = "DELETE FROM driver WHERE driver_id = ?";
            pst = con.prepareStatement(query);
            pst.setInt(1, driverId);

            int rows = pst.executeUpdate();
            if (rows > 0) {
                System.out.println("Driver deleted successfully.");
            } else {
                System.out.println("No driver found with ID: " + driverId);
            }
        } catch (SQLException e) {
            System.out.println("Error deleting driver: " + e.getMessage());
        } finally {
            try {
                if (pst != null) pst.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    // Update driver details
    static void updateDriver(int driverId, String newName, long newPhoneNo, int newBusId) {
        Connection con = null;
        PreparedStatement pst = null;

        try {
            con = DB.connect();
            if (con == null) {
                System.out.println("Database connection failed. Please try again later.");
                return;
            }

            String query = "UPDATE driver SET driver_name = ?, phone_no = ?, bus_id = ? WHERE driver_id = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, newName);
            pst.setLong(2, newPhoneNo);
            pst.setInt(3, newBusId);
            pst.setInt(4, driverId);

            int rows = pst.executeUpdate();
            if (rows > 0) {
                System.out.println("Driver updated successfully.");
            } else {
                System.out.println("No driver found with ID: " + driverId);
            }
        } catch (SQLException e) {
            System.out.println("Error updating driver: " + e.getMessage());
        } finally {
            try {
                if (pst != null) pst.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    // View all drivers in the system
    static void viewAllDrivers() {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = DB.connect();
            if (con == null) {
                System.out.println("Database connection failed. Please try again later.");
                return;
            }

            String query = "SELECT * FROM driver";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            boolean hasDrivers = false;
            while (rs.next()) {
                hasDrivers = true;
                int id = rs.getInt("driver_id");
                String name = rs.getString("driver_name");
                long phone = rs.getLong("phone_no");
                int busId = rs.getInt("bus_id");

                System.out.println("ID: " + id + ", Name: " + name + ", Phone: " + phone + ", Bus ID: " + busId);
            }

            if (!hasDrivers) {
                System.out.println("No drivers found.");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching drivers: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }
}
