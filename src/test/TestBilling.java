package test;

import dao.BillingDAO;
import dao.AppointmentDAO;

import java.util.Scanner;

public class TestBilling {

    public static void main(String[] args) {

        BillingDAO dao = new BillingDAO();
        AppointmentDAO appointmentDAO =
                new AppointmentDAO();

        Scanner sc = new Scanner(System.in);

        while(true) {

            System.out.println("\n===== BILLING MENU =====");
            System.out.println("1. Add Bill");
            System.out.println("2. View Bills");
            System.out.println("3. Update Bill");
            System.out.println("4. Delete Bill");
            System.out.println("5. Count Bills");
            System.out.println("0. Exit");

            System.out.print("Enter Choice : ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch(choice) {

                case 1:

                    System.out.print(
                            "Appointment ID : ");
                    int appointmentId =
                            sc.nextInt();
                    sc.nextLine();

                    if(appointmentDAO
                            .searchAppointment(
                                    appointmentId)
                            == null) {

                        System.out.println(
                                "Appointment ID does not exist!");
                        break;
                    }

                    System.out.print(
                            "Bill Date (YYYY-MM-DD) : ");
                    String billDate =
                            sc.nextLine();

                    if(!billDate.matches(
                            "\\d{4}-\\d{2}-\\d{2}")) {

                        System.out.println(
                                "Invalid Date Format!");
                        break;
                    }

                    System.out.println(
                            "Payment Methods:");
                    System.out.println(
                            "Cash");
                    System.out.println(
                            "Card");
                    System.out.println(
                            "UPI");
                    System.out.println(
                            "NetBanking");

                    System.out.print(
                            "Payment Method : ");
                    String paymentMethod =
                            sc.nextLine();

                    if(!(paymentMethod.equalsIgnoreCase("Cash")
                            || paymentMethod.equalsIgnoreCase("Card")
                            || paymentMethod.equalsIgnoreCase("UPI")
                            || paymentMethod.equalsIgnoreCase("NetBanking"))) {

                        System.out.println(
                                "Invalid Payment Method!");
                        break;
                    }

                    System.out.println(
                            "Payment Status:");
                    System.out.println(
                            "Paid");
                    System.out.println(
                            "Pending");
                    System.out.println(
                            "Failed");

                    System.out.print(
                            "Payment Status : ");
                    String paymentStatus =
                            sc.nextLine();

                    if(!(paymentStatus.equalsIgnoreCase("Paid")
                            || paymentStatus.equalsIgnoreCase("Pending")
                            || paymentStatus.equalsIgnoreCase("Failed"))) {

                        System.out.println(
                                "Invalid Payment Status!");
                        break;
                    }

                    dao.addBill(
                            appointmentId,
                            billDate,
                            paymentMethod,
                            paymentStatus
                    );

                    System.out.println(
                            "Bill Added Successfully!");

                    break;

                case 2:

                    dao.viewBills();

                    break;

                case 3:

                    System.out.print(
                            "Bill ID : ");
                    int billId =
                            sc.nextInt();
                    sc.nextLine();

                    if(!dao.billExists(
                            billId)) {

                        System.out.println(
                                "Bill ID does not exist!");
                        break;
                    }

                    System.out.println(
                            "Payment Methods:");
                    System.out.println(
                            "Cash");
                    System.out.println(
                            "Card");
                    System.out.println(
                            "UPI");
                    System.out.println(
                            "NetBanking");

                    System.out.print(
                            "New Payment Method : ");
                    String newMethod =
                            sc.nextLine();

                    if(!(newMethod.equalsIgnoreCase("Cash")
                            || newMethod.equalsIgnoreCase("Card")
                            || newMethod.equalsIgnoreCase("UPI")
                            || newMethod.equalsIgnoreCase("NetBanking"))) {

                        System.out.println(
                                "Invalid Payment Method!");
                        break;
                    }

                    System.out.println(
                            "Payment Status:");
                    System.out.println(
                            "Paid");
                    System.out.println(
                            "Pending");
                    System.out.println(
                            "Failed");

                    System.out.print(
                            "New Payment Status : ");
                    String newStatus =
                            sc.nextLine();

                    if(!(newStatus.equalsIgnoreCase("Paid")
                            || newStatus.equalsIgnoreCase("Pending")
                            || newStatus.equalsIgnoreCase("Failed"))) {

                        System.out.println(
                                "Invalid Payment Status!");
                        break;
                    }

                    dao.updateBill(
                            billId,
                            newMethod,
                            newStatus
                    );

                    System.out.println(
                            "Bill Updated Successfully!");

                    break;

                case 4:

                    System.out.print(
                            "Bill ID : ");
                    int deleteId =
                            sc.nextInt();

                    if(!dao.billExists(
                            deleteId)) {

                        System.out.println(
                                "Bill ID does not exist!");
                        break;
                    }

                    dao.deleteBill(
                            deleteId);

                    System.out.println(
                            "Bill Deleted Successfully!");

                    break;

                case 5:

                    System.out.println(
                            "Total Bills = "
                            + dao.getBillCount());

                    break;

                case 0:

                    System.out.println(
                            "Exiting...");

                    sc.close();

                    System.exit(0);

                default:

                    System.out.println(
                            "Invalid Choice");
            }
        }
    }
}