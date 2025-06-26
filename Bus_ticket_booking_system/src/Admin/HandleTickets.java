package Admin;
import DB_Connection.*;
import java.sql.*;

public class HandleTickets {


    // View all bookings
    static void viewAllBookings() {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            con = DB.connect();
            if (con == null) {
                System.out.println("Database connection failed. Please try again later.");
                return;
            }

            String query = "SELECT * FROM booking";
            st = con.createStatement();
            rs = st.executeQuery(query);

            boolean hasBookings = false;
            System.out.println("All Bookings:");

            while (rs.next()) {
                hasBookings = true;
                System.out.println("Booking ID     : " + rs.getInt("booking_id"));
                System.out.println("Email          : " + rs.getString("email"));
                System.out.println("Bus ID         : " + rs.getInt("bus_id"));
                System.out.println("Journey Date   : " + rs.getDate("journey_date"));
                System.out.println("Seats Booked   : " + rs.getInt("seat_count"));
                System.out.println("Total Fare     : ₹" + rs.getBigDecimal("total_fare"));
                System.out.println("Booked On      : " + rs.getTimestamp("booking_time"));
                System.out.println("-------------------------------");
            }

            if (!hasBookings) {
                System.out.println("No bookings available.");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching bookings: " + e.getMessage());
        } finally {
            try {
                // Closing resources
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }
}
