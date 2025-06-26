package App;
import java.util.Scanner;
import User.Login;
import User.Register;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in); 
        boolean running = true;

        while (running) {
            try {
                System.out.println();
                System.out.println("1 : Login");
                System.out.println("2 : Register");
                System.out.println("3 : Exit");
                System.out.print("Enter your choice: ");

                int choice = sc.nextInt();
                sc.nextLine(); 

                switch (choice) {
                    case 1 -> new Login();  
                    case 2 -> {  
                        new Register();
                        System.out.println("Login into your account:");
                        new Login();
                    }
                    case 3 -> {  
                        System.out.println("Hope you enjoyed our services.");
                        running = false; 
                    }
                    default -> System.out.println("Invalid choice. Please select 1, 2, or 3.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                sc.nextLine(); 
            }
        }

        sc.close(); 
    }
}
