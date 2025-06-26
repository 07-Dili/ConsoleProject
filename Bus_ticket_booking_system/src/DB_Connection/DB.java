package DB_Connection;

import java.sql.*;

public class DB {

    private static final String URL = "jdbc:mysql://localhost:3306/booking_ticket";
    private static final String USER = "root";
    private static final String PASS = "Dilip";

    public static Connection connect() {
        Connection con=null;
        try {
            con = DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            System.err.println("Connection error: " + e.getMessage());
        }
        return con;
    }

    static ResultSet getUserRecords() {
        try {
            Connection con=connect();
            Statement st=con.createStatement();
            String query = "SELECT * FROM user";
            ResultSet res=st.executeQuery(query);
            return res;
        } catch (SQLException e) {
            System.err.println("Error fetching user records: " + e.getMessage());
            return null;
        }
    }

    public static String checkLogin(String email, String pass) {
        try {
            Connection con=connect();
            String query = "SELECT pass, user_name FROM user WHERE email = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String existingPass = rs.getString("pass");
                String existingName = rs.getString("user_name");

                if (existingPass.equals(pass)) {
                    return existingName;
                }
            }
            return "sorry";
        } catch (SQLException e) {
            System.err.println("Login error: " + e.getMessage());
            return "error";
        }
    }

    public static boolean checkAdmin(String email, String pass) {
        try {
            Connection con=connect();
            Statement st=con.createStatement();
            String query = "SELECT email, pass FROM admin";
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                String existingEmail = rs.getString("email");
                String existingPass = rs.getString("pass");

                if (existingEmail.equals(email) && existingPass.equals(pass)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Admin check error: " + e.getMessage());
        }
        return false;
    }

    public static boolean insertRecords(String name, int age, String gender, String email, long phno, String pass) {
        try {
            Connection con=connect();
            String query = "INSERT INTO user (user_name, gender, age, phno, email, pass) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(query);

            pst.setString(1, name);
            pst.setString(2, gender);
            pst.setInt(3, age);
            pst.setLong(4, phno);
            pst.setString(5, email);
            pst.setString(6, pass);

            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Insert error: " + e.getMessage());
            return false;
        }
    }

    static boolean updateRecord(String name, int age, String gender, String email, long phno, String pass) {
        try {
            Connection con=connect();
            String query = "UPDATE user SET user_name = ?, age = ?, gender = ?, phno = ?, pass = ? WHERE email = ?";
            PreparedStatement pst = con.prepareStatement(query);

            pst.setString(1, name);
            pst.setInt(2, age);
            pst.setString(3, gender);
            pst.setLong(4, phno);
            pst.setString(5, pass);
            pst.setString(6, email);

            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Update error: " + e.getMessage());
            return false;
        }
    }

    static boolean deleteRecords(String email) {
        try {
            String query = "DELETE FROM user WHERE email = ?";
            Connection con=connect();
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, email);

            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Delete error: " + e.getMessage());
            return false;
        }
    }
}
