package Admin;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import DB_Connection.DB;

class HandleBus {

    private static final Logger logger = Logger.getLogger(HandleBus.class.getName());
    // Add a new bus to the system
    static void addBus(String busname, String source, String dest, Time deptTime, Time arrTime, Date date, int totSeat, int avaiSeat, double cost, String busType) {
        Connection con = null;
        PreparedStatement pst = null;

        try {
            con = DB.connect();
            if (con == null) {
                System.out.println("Database connection failed. Please try again later.");
                return;
            }

            String query = "INSERT INTO bus (bus_name, source, destination, departure_time, arrival_time, date, total_seats, available_seats, cost, bus_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pst = con.prepareStatement(query);
            pst.setString(1, busname);
            pst.setString(2, source);
            pst.setString(3, dest);
            pst.setTime(4, deptTime);
            pst.setTime(5, arrTime);
            pst.setDate(6, date);
            pst.setInt(7, totSeat);
            pst.setInt(8, avaiSeat);
            pst.setDouble(9, cost);
            pst.setString(10, busType);

            int rowsInserted = pst.executeUpdate();
            System.out.println(rowsInserted + " Bus Added.");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error adding bus: ", e);
        } finally {
            try {
                if (pst != null) pst.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error closing resources: ", e);
            }
        }
    }

    // Delete a bus from the system
    static void deleteBus(int id) {
        Connection con = null;
        PreparedStatement pst = null;

        try {
            con = DB.connect();
            if (con == null) {
                System.out.println("Database connection failed. Please try again later.");
                return;
            }

            // First, delete the driver associated with the bus
            String deleteDriverQuery = "DELETE FROM driver WHERE bus_id = ?";
            pst = con.prepareStatement(deleteDriverQuery);
            pst.setInt(1, id);
            pst.executeUpdate();

            // Then, delete the bus itself
            String deleteBusQuery = "DELETE FROM bus WHERE bus_id = ?";
            pst = con.prepareStatement(deleteBusQuery);
            pst.setInt(1, id);

            int busRowsAffected = pst.executeUpdate();
            if (busRowsAffected > 0) {
                System.out.println("Deleted bus successfully.");
            } else {
                System.out.println("Bus ID not found.");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting bus: ", e);
        } finally {
            try {
                if (pst != null) pst.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error closing resources: ", e);
            }
        }
    }

    // View all buses in the system
    public static void viewAllBuses() {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            con = DB.connect();
            if (con == null) {
                System.out.println("Database connection failed. Please try again later.");
                return;
            }

            String query = "SELECT * FROM bus";
            st = con.createStatement();
            rs = st.executeQuery(query);

            while (rs.next()) {
                System.out.println("Bus ID : " + rs.getInt("bus_id"));
                System.out.println("Bus Name : " + rs.getString("bus_name"));
                System.out.println("Source : " + rs.getString("source"));
                System.out.println("Destination : " + rs.getString("destination"));
                System.out.println("Departure Time : " + rs.getTime("departure_time"));
                System.out.println("Arrival Time : " + rs.getTime("arrival_time"));
                System.out.println("Date : " + rs.getDate("date"));
                System.out.println("Total Seats : " + rs.getInt("total_seats"));
                System.out.println("Available Seats : " + rs.getInt("available_seats"));
                System.out.println("Cost : ₹" + rs.getDouble("cost"));
                System.out.println("Bus Type : " + rs.getString("bus_type"));
                System.out.println();
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching bus details: ", e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error closing resources: ", e);
            }
        }
    }

    // Search for a specific bus by its ID
    static void searchBus(int busId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = DB.connect();
            if (con == null) {
                System.out.println("Database connection failed. Please try again later.");
                return;
            }

            String query = "SELECT * FROM bus WHERE bus_id = ?";
            pst = con.prepareStatement(query);
            pst.setInt(1, busId);

            rs = pst.executeQuery();

            if (rs.next()) {
                System.out.println("Bus ID       : " + rs.getInt("bus_id"));
                System.out.println("Bus Name     : " + rs.getString("bus_name"));
                System.out.println("Source       : " + rs.getString("source"));
                System.out.println("Destination  : " + rs.getString("destination"));
                System.out.println("Departure    : " + rs.getTime("departure_time"));
                System.out.println("Arrival      : " + rs.getTime("arrival_time"));
                System.out.println("Date         : " + rs.getDate("date"));
                System.out.println("Total Seats  : " + rs.getInt("total_seats"));
                System.out.println("Available    : " + rs.getInt("available_seats"));
                System.out.println("Fare         : ₹" + rs.getDouble("cost"));
                System.out.println("Bus Type     : " + rs.getString("bus_type"));
            } else {
                System.out.println("No bus found with ID: " + busId);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error searching bus: ", e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error closing resources: ", e);
            }
        }
    }

    // Update bus details (fare and available seats)
    static void updateBus(int busId, double newFare, int newAvailableSeats) {
        Connection con = null;
        PreparedStatement pst = null;

        try {
            con = DB.connect();
            if (con == null) {
                System.out.println("Database connection failed. Please try again later.");
                return;
            }

            String updateQuery = "UPDATE bus SET cost = ?, available_seats = ? WHERE bus_id = ?";
            pst = con.prepareStatement(updateQuery);
            pst.setDouble(1, newFare);
            pst.setInt(2, newAvailableSeats);
            pst.setInt(3, busId);

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Bus details updated successfully for Bus ID: " + busId);
            } else {
                System.out.println("No bus found with ID: " + busId);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating bus details: ", e);
        } finally {
            try {
                if (pst != null) pst.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error closing resources: ", e);
            }
        }
    }
}
