package Admin;
import DB_Connection.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class HandleUsers {
    
    // Delete user based on email
    static void deleteUser(String email) {
        try (Connection con = DB.connect()) {
            if (con == null) {
                System.out.println("Database connection failed. Try again later.");
                return;
            }

            String query = "DELETE FROM user WHERE email = ?";
            try (PreparedStatement pst = con.prepareStatement(query)) {
                pst.setString(1, email);
                int rows = pst.executeUpdate();

                if (rows > 0) {
                    System.out.println("User with email " + email + " deleted successfully.");
                } else {
                    System.out.println("No user found with email: " + email);
                }
            }
        } catch (Exception e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }

    // View all registered users
    static void viewAllUsers() {
        try (Connection con = DB.connect()) {
            if (con == null) {
                System.out.println("Database connection failed. Try again later.");
                return;
            }

            String query = "SELECT * FROM user";
            try (PreparedStatement pst = con.prepareStatement(query);
                 ResultSet rs = pst.executeQuery()) {

                System.out.println("All Registered Users:");
                while (rs.next()) {
                    String name = rs.getString("user_name");
                    String gender = rs.getString("gender");
                    int age = rs.getInt("age");
                    long phone = rs.getLong("phno");
                    String email = rs.getString("email");
                    String pass = rs.getString("pass");

                    System.out.println("Name: " + name + ", Gender: " + gender + ", Age: " + age +
                            ", Phone: " + phone + ", Email: " + email + ", Password: " + pass);
                }
            }
        } catch (Exception e) {
            System.out.println("Error fetching users: " + e.getMessage());
        }
    }
}
