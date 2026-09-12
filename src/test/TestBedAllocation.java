package test;

import dao.BedAllocationDAO;
import model.BedAllocation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;

public class TestBedAllocation {

    public static void main(String[] args) {
        BedAllocationDAO dao = new BedAllocationDAO();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== INPATIENT & BED ALLOCATION MENU =====");
            System.out.println("1. Admit Patient & Allocate Bed");
            System.out.println("2. View All Active Occupied Beds");
            System.out.println("3. Discharge Patient");
            System.out.println("4. Bed Statistics (Occupied vs Total)");
            System.out.println("0. Exit");

            System.out.print("Enter Choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Patient ID: ");
                    int patientId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Ward Type (General Ward, ICU, Private AC Room, Emergency): ");
                    String wardType = sc.nextLine();

                    System.out.print("Bed Number (e.g., GW-105, ICU-02): ");
                    String bedNumber = sc.nextLine();

                    System.out.print("Admit Date (YYYY-MM-DD): ");
                    String admitDate = sc.nextLine();

                    System.out.print("Daily Bed Charge (₹): ");
                    BigDecimal charge = sc.nextBigDecimal();

                    try {
                        dao.allocateBed(patientId, wardType, bedNumber, admitDate, charge, "Occupied");
                        System.out.println("Bed Allocated and Patient Admitted Successfully!");
                    } catch (Exception ex) {
                        System.out.println("Allocation Failed: " + ex.getMessage());
                    }
                    break;

                case 2:
                    ArrayList<BedAllocation> active = dao.getActiveAllocations();
                    if (active.isEmpty()) {
                        System.out.println("No currently occupied beds.");
                    } else {
                        System.out.println("\nAllocation ID | Patient ID | Ward | Bed | Admit Date | Daily Charge | Status");
                        System.out.println("-----------------------------------------------------------------------------");
                        for (BedAllocation b : active) {
                            System.out.printf("%d | %d | %s | %s | %s | ₹%.2f | %s\n",
                                    b.getAllocationId(), b.getPatientId(), b.getWardType(),
                                    b.getBedNumber(), b.getAdmitDate(), b.getDailyCharge(), b.getStatus());
                        }
                    }
                    break;

                case 3:
                    System.out.print("Enter Allocation ID to discharge: ");
                    int allocId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter Discharge Date (YYYY-MM-DD): ");
                    String disDate = sc.nextLine();

                    dao.dischargePatient(allocId, disDate);
                    System.out.println("Patient Discharged Successfully!");
                    break;

                case 4:
                    System.out.println("Currently Occupied Beds : " + dao.getOccupiedBedCount());
                    System.out.println("Total Admission History : " + dao.getTotalAllocationCount());
                    break;

                case 0:
                    System.out.println("Exiting Bed Allocation Menu...");
                    sc.close();
                    System.exit(0);

                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }
}
