package test;

import dao.UserDAO;
import model.User;

import java.util.Scanner;

public class TestLogin {

    public static void main(String[] args) {

        UserDAO dao = new UserDAO();
        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println("\n===== LOGIN TEST MENU =====");
            System.out.println("1. Login");
            System.out.println("2. Register New User");
            System.out.println("0. Exit");

            System.out.print("Enter Choice : ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:

                    System.out.print("Username : ");
                    String username = sc.nextLine();

                    System.out.print("Password : ");
                    String password = sc.nextLine();

                    User user = dao.authenticate(
                            username, password
                    );

                    if (user != null) {

                        System.out.println(
                                "\nLogin Successful!");

                        System.out.println(
                                "User ID   : " +
                                user.getUserId());

                        System.out.println(
                                "Full Name : " +
                                user.getFullName());

                        System.out.println(
                                "Role      : " +
                                user.getRole());

                    } else {

                        System.out.println(
                                "Invalid username or password!");
                    }

                    break;

                case 2:

                    System.out.print("Username : ");
                    String newUsername = sc.nextLine();

                    if (dao.userExists(newUsername)) {

                        System.out.println(
                                "Username already exists!");
                        break;
                    }

                    System.out.print("Password : ");
                    String newPassword = sc.nextLine();

                    System.out.print("Full Name : ");
                    String fullName = sc.nextLine();

                    System.out.println("Role:");
                    System.out.println("1. Admin");
                    System.out.println("2. Receptionist");

                    System.out.print("Enter Choice : ");
                    int roleChoice = sc.nextInt();
                    sc.nextLine();

                    String role = "";

                    switch (roleChoice) {
                        case 1:
                            role = "Admin";
                            break;
                        case 2:
                            role = "Receptionist";
                            break;
                        default:
                            System.out.println(
                                    "Invalid Role!");
                            continue;
                    }

                    dao.addUser(
                            newUsername,
                            newPassword,
                            fullName,
                            role
                    );

                    System.out.println(
                            "User Registered Successfully!");

                    break;

                case 0:

                    System.out.println("Exiting...");
                    sc.close();
                    System.exit(0);

                default:

                    System.out.println("Invalid Choice");
            }
        }
    }
}
