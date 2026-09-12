package test;

import dao.LabTestDAO;
import model.LabTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;

public class TestLabTest {

    public static void main(String[] args) {
        LabTestDAO dao = new LabTestDAO();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== DIAGNOSTIC LAB TESTS MENU =====");
            System.out.println("1. Order New Lab Test");
            System.out.println("2. View All Lab Tests");
            System.out.println("3. View Tests by Patient ID");
            System.out.println("4. Update Test Result & Status");
            System.out.println("5. Delete Lab Test");
            System.out.println("6. Count Total & Pending Tests");
            System.out.println("0. Exit");

            System.out.print("Enter Choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Patient ID: ");
                    int patientId = sc.nextInt();
                    System.out.print("Doctor ID: ");
                    int doctorId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Test Name (e.g., CBC Blood Test, MRI, ECG): ");
                    String testName = sc.nextLine();

                    System.out.print("Test Date (YYYY-MM-DD): ");
                    String testDate = sc.nextLine();

                    System.out.print("Cost (₹): ");
                    BigDecimal cost = sc.nextBigDecimal();
                    sc.nextLine();

                    System.out.print("Initial Result / Notes (or 'Pending'): ");
                    String result = sc.nextLine();

                    dao.addLabTest(patientId, doctorId, testName, testDate, cost, result, "Pending");
                    System.out.println("Lab Test Ordered Successfully!");
                    break;

                case 2:
                    ArrayList<LabTest> all = dao.getAllLabTests();
                    System.out.println("\nID | Patient | Doctor | Test Name | Date | Cost | Status | Result");
                    System.out.println("------------------------------------------------------------------");
                    for (LabTest t : all) {
                        System.out.printf("%d | %d | %d | %s | %s | ₹%.2f | %s | %s\n",
                                t.getTestId(), t.getPatientId(), t.getDoctorId(), t.getTestName(),
                                t.getTestDate(), t.getCost(), t.getStatus(), t.getResult());
                    }
                    break;

                case 3:
                    System.out.print("Enter Patient ID: ");
                    int pid = sc.nextInt();
                    ArrayList<LabTest> patientTests = dao.getLabTestsByPatient(pid);
                    if (patientTests.isEmpty()) {
                        System.out.println("No lab tests found for Patient ID " + pid);
                    } else {
                        System.out.println("\nTests for Patient #" + pid + ":");
                        for (LabTest t : patientTests) {
                            System.out.printf("Test #%d: %s | Date: %s | Cost: ₹%.2f | Status: %s | Result: %s\n",
                                    t.getTestId(), t.getTestName(), t.getTestDate(), t.getCost(), t.getStatus(), t.getResult());
                        }
                    }
                    break;

                case 4:
                    System.out.print("Enter Test ID to update: ");
                    int tid = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter Findings / Result: ");
                    String newResult = sc.nextLine();

                    System.out.print("Enter Status (Completed / Sample Collected): ");
                    String newStatus = sc.nextLine();

                    dao.updateTestResult(tid, newResult, newStatus);
                    System.out.println("Lab Test Result Updated Successfully!");
                    break;

                case 5:
                    System.out.print("Enter Test ID to delete: ");
                    int delId = sc.nextInt();
                    dao.deleteLabTest(delId);
                    System.out.println("Lab Test Record Deleted!");
                    break;

                case 6:
                    System.out.println("Total Lab Tests   : " + dao.getLabTestCount());
                    System.out.println("Pending Lab Tests : " + dao.getPendingTestCount());
                    break;

                case 0:
                    System.out.println("Exiting Lab Tests Menu...");
                    sc.close();
                    System.exit(0);

                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }
}
