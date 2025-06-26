package Admin;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import App.Main;
import java.util.logging.*;
import DB_Connection.*;

public class Admin extends DB {
    
    private static final Logger logger = Logger.getLogger(Admin.class.getName());

    public Admin() throws Exception {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("1 : addBus, 2 : updateBus, 3 : deleteBus, 4 : viewAllBuses, 5 : searchBus,");
            System.out.println("6 : addDriver, 7 : updateDriver, 8 : deleteDriver, 9 : viewAllDrivers,");
            System.out.println("10 : viewAllUsers, 11 : deleteUser, 12 : viewAllBookings");
            System.out.println("13 : changeAdminPassword, 14 : logout");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine(); 
            try {
                switch (choice) {
                    case 1: {
                        addBusProcess(sc);
                        break;
                    }

                    case 2: {
                        updateBusProcess(sc);
                        break;
                    }

                    case 3: {
                        deleteBusProcess(sc);
                        break;
                    }

                    case 4: {
                        HandleBus.viewAllBuses();
                        break;
                    }

                    case 5: {
                        searchBusProcess(sc);
                        break;
                    }

                    case 6: {
                        addDriverProcess(sc);
                        break;
                    }

                    case 7: {
                        updateDriverProcess(sc);
                        break;
                    }

                    case 8: {
                        deleteDriverProcess(sc);
                        break;
                    }

                    case 9: {
                        HandleDrivers.viewAllDrivers();
                        break;
                    }

                    case 10: {
                        HandleUsers.viewAllUsers();
                        break;
                    }

                    case 11: {
                        deleteUserProcess(sc);
                        break;
                    }

                    case 12: {
                        HandleTickets.viewAllBookings();
                        break;
                    }

                    case 13: {
                        changeAdminPasswordProcess(sc);
                        break;
                    }

                    case 14: {
                        logout();
                        return;
                    }

                    default:
                        System.out.println("Invalid choice. Please select given choice only.");
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Database error occurred", e);
                System.out.println("An error occurred, please try again.");
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Unexpected error occurred", e);
                System.out.println("An unexpected error occurred, please try again.");
            }
            System.out.println();
        }
    }

    private static void addBusProcess(Scanner sc) throws SQLException {
        System.out.print("Bus Name: ");
        String busname = sc.nextLine();
        System.out.print("Source: ");
        String source = sc.nextLine();
        System.out.print("Destination: ");
        String dest = sc.nextLine();
        System.out.print("Departure Time (HH:MM:SS): ");
        Time deptTime = Time.valueOf(sc.nextLine());
        System.out.print("Arrival Time (HH:MM:SS): ");
        Time arrTime = Time.valueOf(sc.nextLine());
        System.out.print("Date (YYYY-MM-DD): ");
        Date date = Date.valueOf(sc.nextLine());
        System.out.print("Total Seats: ");
        int totSeat = sc.nextInt();
        System.out.print("Available Seats: ");
        int avaiSeat = sc.nextInt();
        System.out.print("Fare: ");
        double cost = sc.nextDouble();
        sc.nextLine();
        System.out.print("Bus Type (AC/Non-AC): ");
        String busType = sc.nextLine();

        HandleBus.addBus(busname, source, dest, deptTime, arrTime, date, totSeat, avaiSeat, cost, busType);
    }

    private static void updateBusProcess(Scanner sc) throws SQLException {
        System.out.print("Enter Bus ID to update: ");
        int busId = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter new cost: ");
        double cost = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter available seats: ");
        int availableSeats = sc.nextInt();

        HandleBus.updateBus(busId, cost, availableSeats);
    }

    private static void deleteBusProcess(Scanner sc) throws SQLException {
        System.out.print("Enter Bus ID to delete: ");
        int busId = sc.nextInt();
        HandleBus.deleteBus(busId);
    }

    private static void searchBusProcess(Scanner sc) throws SQLException {
        System.out.print("Enter Bus ID to search: ");
        int busId = sc.nextInt();
        HandleBus.searchBus(busId);
    }

    private static void addDriverProcess(Scanner sc) throws SQLException {
        System.out.print("Driver ID: ");
        int driverId = sc.nextInt();
        sc.nextLine(); // Consume newline
        System.out.print("Driver Name: ");
        String driverName = sc.nextLine();
        System.out.print("Phone Number: ");
        long phone = sc.nextLong();
        System.out.print("Bus ID: ");
        int busId = sc.nextInt();

        HandleDrivers.addDriver(driverId, driverName, phone, busId);
    }

    private static void updateDriverProcess(Scanner sc) throws SQLException {
        System.out.print("Enter Driver ID to update: ");
        int driverId = sc.nextInt();
        sc.nextLine(); // Consume newline
        System.out.print("Enter Driver name: ");
        String name = sc.nextLine();
        System.out.print("Enter Driver phone number: ");
        long phoneNo = sc.nextLong();
        System.out.print("Enter Bus ID: ");
        int busId = sc.nextInt();
        
        HandleDrivers.updateDriver(driverId, name, phoneNo, busId);
    }

    private static void deleteDriverProcess(Scanner sc) throws SQLException {
        System.out.print("Enter Driver ID to delete: ");
        int driverId = sc.nextInt();
        HandleDrivers.deleteDriver(driverId);
    }

    private static void deleteUserProcess(Scanner sc) throws SQLException {
        System.out.print("Enter user email to delete: ");
        String email = sc.nextLine();
        HandleUsers.deleteUser(email);
    }

    private static void changeAdminPasswordProcess(Scanner sc) throws SQLException {
        System.out.print("Enter Email: ");
        String email = sc.nextLine();
        System.out.print("Enter new password: ");
        String newpass = sc.nextLine();
        changeAdminPassword(email, newpass);
    }

    private static void changeAdminPassword(String email, String newpass) throws SQLException {
        try (Connection con = DB.connect();
             PreparedStatement pst = con.prepareStatement("UPDATE admin SET pass=? WHERE email=?")) {
            pst.setString(1, newpass);
            pst.setString(2, email);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Password updated successfully.");
            } else {
                System.out.println("Failed to update password.");
            }
        }
    }

    private static void logout() {
        String[] arguments = {};
        Main.main(arguments);
    }
}
