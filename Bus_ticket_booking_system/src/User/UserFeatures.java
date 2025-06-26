package User;
import DB_Connection.DB;
import java.sql.*;
import java.util.*;
import App.Main;

public class UserFeatures {
    Scanner sc = new Scanner(System.in);

    UserFeatures(String user_email) {
        while (true) {
            try {
                System.out.println();
                System.out.println("1 : Book Ticket");
                System.out.println("2 : View Available Buses");
                System.out.println("3 : View My Bookings");
                System.out.println("4 : Cancel Ticket");
                System.out.println("5 : Edit Profile");
                System.out.println("6 : Logout");

                System.out.print("Enter choice: ");
                if (!sc.hasNextInt()) {
                    System.out.println("Please enter a valid number.");
                    sc.nextLine();
                    continue;
                }

                int choice = sc.nextInt();
                switch (choice) {
                    case 1 -> bookTicket(user_email);
                    case 2 -> viewAvailableBuses();
                    case 3 -> viewMyBookings(user_email);
                    case 4 -> cancelTicket(user_email);
                    case 5 -> editProfile(user_email);
                    case 6 -> {
                        System.out.println("Logging out...");
                        Main.main(new String[]{});
                        return;
                    }
                    default -> System.out.println("Invalid choice. Try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void editProfile(String curEmail) {
        try {
            sc.nextLine(); 
            System.out.print("Enter new username: ");
            String newUserName = sc.nextLine().trim();

            System.out.print("Enter new gender: ");
            String newGender = sc.nextLine().trim();

            System.out.print("Enter new age: ");
            int newAge = sc.nextInt();

            System.out.print("Enter new phone number: ");
            long newPhno = sc.nextLong();
            sc.nextLine();

            System.out.print("Enter new email: ");
            String newEmail = sc.nextLine().trim();

            System.out.print("Enter new password: ");
            String newPassword = sc.nextLine();

            try (Connection con = DB.connect();
                 PreparedStatement pst = con.prepareStatement(
                         "UPDATE user SET user_name=?, gender=?, age=?, phno=?, email=?, pass=? WHERE email=?")) {

                pst.setString(1, newUserName);
                pst.setString(2, newGender);
                pst.setInt(3, newAge);
                pst.setLong(4, newPhno);
                pst.setString(5, newEmail);
                pst.setString(6, newPassword);
                pst.setString(7, curEmail);

                int rows = pst.executeUpdate();
                System.out.println(rows > 0 ? " Profile updated successfully." :
                                               " No profile found for: " + curEmail);
            }
        } catch (Exception e) {
            System.out.println(" Error updating profile: " + e.getMessage());
        }
    }

    public void viewAvailableBuses() {
        try (Connection con = DB.connect();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM bus WHERE available_seats > 0")) {

            System.out.println("\nAvailable Buses:");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("Bus ID         : " + rs.getInt("bus_id"));
                System.out.println("Bus Name       : " + rs.getString("bus_name"));
                System.out.println("From           : " + rs.getString("source"));
                System.out.println("To             : " + rs.getString("destination"));
                System.out.println("Departure      : " + rs.getTime("departure_time"));
                System.out.println("Arrival        : " + rs.getTime("arrival_time"));
                System.out.println("Date           : " + rs.getDate("date"));
                System.out.println("Available Seats: " + rs.getInt("available_seats"));
                System.out.println("Cost per Seat  : ₹" + rs.getDouble("cost"));
                System.out.println("Bus Type       : " + rs.getString("bus_type"));
                System.out.println();
            }

            if (!found) {
                System.out.println("No buses available at the moment.");
            }
        } catch (Exception e) {
            System.out.println(" Error fetching bus details: " + e.getMessage());
        }
    }

    private void viewMyBookings(String userEmail) {
        try (Connection con = DB.connect();
             PreparedStatement pst = con.prepareStatement(
                     "SELECT * FROM booking WHERE email = ? ORDER BY journey_date, booking_time")) {

            pst.setString(1, userEmail);
            try (ResultSet rs = pst.executeQuery()) {
                boolean hasBookings = false;
                System.out.println("\nYour Bookings:");
                while (rs.next()) {
                    hasBookings = true;
                    System.out.println("Booking ID     : " + rs.getInt("booking_id"));
                    System.out.println("Bus ID         : " + rs.getInt("bus_id"));
                    System.out.println("Journey Date   : " + rs.getDate("journey_date"));
                    System.out.println("Seats Booked   : " + rs.getInt("seat_count"));
                    System.out.println("Total Fare     : ₹" + rs.getBigDecimal("total_fare"));
                    System.out.println("Booked On      : " + rs.getTimestamp("booking_time"));
                    System.out.println();
                }

                if (!hasBookings) {
                    System.out.println("You have no bookings yet.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error viewing bookings: " + e.getMessage());
        }
    }

    private void cancelTicket(String userEmail) {
        try {
            viewMyBookings(userEmail);

            System.out.print("\nEnter Booking ID to cancel: ");
            int bookingId = sc.nextInt();
            sc.nextLine(); 

            try (Connection con = DB.connect();
                 PreparedStatement pst = con.prepareStatement(
                         "SELECT seat_count, bus_id FROM booking WHERE booking_id = ? AND email = ?")) {

                pst.setInt(1, bookingId);
                pst.setString(2, userEmail);
                ResultSet rs = pst.executeQuery();

                if (!rs.next()) {
                    System.out.println(" No booking found with this ID.");
                    return;
                }

                int seatCount = rs.getInt("seat_count");
                int busId = rs.getInt("bus_id");

                System.out.print("Confirm cancellation (yes/no): ");
                String confirm = sc.nextLine();
                if (!confirm.equalsIgnoreCase("yes")) {
                    System.out.println("Cancellation aborted.");
                    return;
                }

                try (PreparedStatement deletePst = con.prepareStatement(
                        "DELETE FROM booking WHERE booking_id = ? AND email = ?")) {
                    deletePst.setInt(1, bookingId);
                    deletePst.setString(2, userEmail);
                    deletePst.executeUpdate();
                }

                try (PreparedStatement updateSeats = con.prepareStatement(
                        "UPDATE bus SET available_seats = available_seats + ? WHERE bus_id = ?")) {
                    updateSeats.setInt(1, seatCount);
                    updateSeats.setInt(2, busId);
                    updateSeats.executeUpdate();
                }

                System.out.println("Booking cancelled successfully.");
            }
        } catch (Exception e) {
            System.out.println("Error cancelling ticket: " + e.getMessage());
        }
    }

    private void bookTicket(String userEmail) {
        try {
            sc.nextLine();
            System.out.print("Enter journey date (YYYY-MM-DD): ");
            String journeyDate = sc.nextLine().trim();

            try (Connection con = DB.connect();
                 PreparedStatement pst = con.prepareStatement("SELECT * FROM bus WHERE date = ?")) {
                pst.setString(1, journeyDate);
                ResultSet rs = pst.executeQuery();

                boolean busFound = false;
                while (rs.next()) {
                    busFound = true;
                    System.out.println("Bus ID: " + rs.getInt("bus_id") +
                            ", From: " + rs.getString("source") +
                            ", To: " + rs.getString("destination") +
                            ", Seats: " + rs.getInt("available_seats") +
                            ", Fare: ₹" + rs.getDouble("cost"));
                }

                if (!busFound) {
                    System.out.println("No buses available on that date.");
                    return;
                }

                System.out.print("Enter Bus ID to book: ");
                int busId = sc.nextInt();

                PreparedStatement getBus = con.prepareStatement(
                        "SELECT available_seats, cost FROM bus WHERE bus_id = ?");
                getBus.setInt(1, busId);
                rs = getBus.executeQuery();

                if (!rs.next()) {
                    System.out.println("Invalid Bus ID.");
                    return;
                }

                int available = rs.getInt("available_seats");
                double costPerSeat = rs.getDouble("cost");

                System.out.print("Enter number of seats to book: ");
                int seats = sc.nextInt();
                sc.nextLine();

                if (seats <= 0 || seats > available) {
                    System.out.println("Invalid seat count.");
                    return;
                }

                double totalFare = seats * costPerSeat;
                System.out.println("Total fare: ₹" + totalFare);
                System.out.print("Confirm booking (yes/no): ");
                String confirm = sc.nextLine();

                if (!confirm.equalsIgnoreCase("yes")) {
                    System.out.println("Booking cancelled.");
                    return;
                }

                try (PreparedStatement insertBooking = con.prepareStatement(
                        "INSERT INTO booking (email, bus_id, seat_count, journey_date, total_fare) VALUES (?, ?, ?, ?, ?)")) {
                    insertBooking.setString(1, userEmail);
                    insertBooking.setInt(2, busId);
                    insertBooking.setInt(3, seats);
                    insertBooking.setString(4, journeyDate);
                    insertBooking.setDouble(5, totalFare);
                    insertBooking.executeUpdate();
                }

                try (PreparedStatement updateSeats = con.prepareStatement(
                        "UPDATE bus SET available_seats = available_seats - ? WHERE bus_id = ?")) {
                    updateSeats.setInt(1, seats);
                    updateSeats.setInt(2, busId);
                    updateSeats.executeUpdate();
                }

                System.out.println(" Booking successful!");
            }
        } catch (Exception e) {
            System.out.println(" Error booking ticket: " + e.getMessage());
        }
    }
}
