package User;

import java.util.Scanner;
import Admin.Admin;
import DB_Connection.DB;

public class Login{
    public Login() {
        Scanner sc = new Scanner(System.in); 
        try {
            System.out.print("Enter email: ");
            String email = sc.nextLine().trim();

            System.out.print("Enter password: ");
            String pass = sc.nextLine();

            verifyLogin(email, pass);

        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
        }
    }

    private static void verifyLogin(String email, String pass) throws Exception {
        DB.connect();

        if (DB.checkAdmin(email, pass)) {
            System.out.println("Logged in as Admin.");
            new Admin();
            return;
        }
        String matchedUserName = DB.checkLogin(email, pass);

        if (matchedUserName.equalsIgnoreCase("sorry")) {
            System.out.println("Invalid email or password. Please try again.");
        } else {
            System.out.println("Welcome back, " + matchedUserName + "!");
            new UserFeatures(email);
        }
    }
}
