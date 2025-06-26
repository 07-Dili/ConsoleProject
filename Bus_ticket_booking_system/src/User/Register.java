package User;

import java.util.Scanner;
import DB_Connection.DB;

public class Register {
    private String name, gender, email, pass;
    private int age;
    private long phno;

    public Register() throws Exception {
        Scanner sc = new Scanner(System.in); // Do not close this scanner here

        try {
            System.out.print("Enter your name: ");
            name = sc.nextLine();

            System.out.print("Enter your gender: ");
            gender = sc.nextLine();

            System.out.print("Enter your age: ");
            while (!sc.hasNextInt()) {
                System.out.print("Invalid age. Enter a valid number: ");
                sc.next();
            }
            age = sc.nextInt();
            sc.nextLine(); // consume newline

            System.out.print("Enter your phone number: ");
            while (!sc.hasNextLong()) {
                System.out.print("Invalid phone number. Enter digits only: ");
                sc.next();
            }
            phno = sc.nextLong();
            sc.nextLine(); // consume newline

            System.out.print("Enter your email: ");
            email = sc.nextLine();

            System.out.print("Enter password: ");
            pass = sc.nextLine();

        } catch (Exception e) {
            System.out.println("Input error: " + e.getMessage());
            return;
        }

        DB.connect();
        boolean done = DB.insertRecords(name, age, gender, email, phno, pass);

        if (done) {
            System.out.println("Registered Successfully!");
        } else {
            System.out.println("Registration failed. Please check your details or try a different email.");
        }
    }
}
