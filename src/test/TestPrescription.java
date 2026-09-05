package test;

import dao.PrescriptionDAO;
import dao.AppointmentDAO;
import model.Prescription;

import java.util.Scanner;

public class TestPrescription {

    public static void main(String[] args) {

        PrescriptionDAO dao = new PrescriptionDAO();
        AppointmentDAO appointmentDAO =
                new AppointmentDAO();

        Scanner sc = new Scanner(System.in);

        while(true) {

            System.out.println("\n===== PRESCRIPTION MENU =====");
            System.out.println("1. Add Prescription");
            System.out.println("2. View Prescriptions");
            System.out.println("3. Search Prescription");
            System.out.println("4. Update Prescription");
            System.out.println("5. Delete Prescription");
            System.out.println("6. Count Prescriptions");
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
                            "Diagnosis : ");
                    String diagnosis =
                            sc.nextLine();

                    System.out.print(
                            "Medicine : ");
                    String medicine =
                            sc.nextLine();

                    System.out.print(
                            "Next Visit Date (YYYY-MM-DD) : ");
                    String nextVisitDate =
                            sc.nextLine();

                    if(!nextVisitDate.matches(
                            "\\d{4}-\\d{2}-\\d{2}")) {

                        System.out.println(
                                "Invalid Date Format!");
                        break;
                    }

                    System.out.print(
                            "Remarks : ");
                    String remarks =
                            sc.nextLine();

                    dao.addPrescription(
                            appointmentId,
                            diagnosis,
                            medicine,
                            nextVisitDate,
                            remarks
                    );

                    System.out.println(
                            "Prescription Added Successfully!");

                    break;

                case 2:

                    dao.viewPrescriptions();

                    break;

                case 3:

                    System.out.print(
                            "Enter Prescription ID : ");

                    int searchId =
                            sc.nextInt();

                    Prescription p =
                            dao.searchPrescription(searchId);

                    if (p != null) {

                        System.out.println(
                                "\nPrescription Found");

                        System.out.println(
                                "Prescription ID : "
                                + p.getPrescriptionId());

                        System.out.println(
                                "Appointment ID : "
                                + p.getAppointmentId());

                        System.out.println(
                                "Diagnosis : "
                                + p.getDiagnosis());

                        System.out.println(
                                "Medicine : "
                                + p.getMedicine());

                        System.out.println(
                                "Next Visit Date : "
                                + p.getNextVisitDate());

                        System.out.println(
                                "Remarks : "
                                + p.getRemarks());
                    }
                    else {

                        System.out.println(
                                "Prescription Not Found");
                    }

                    break;

                case 4:

                    System.out.print(
                            "Prescription ID : ");

                    int updateId =
                            sc.nextInt();
                    sc.nextLine();

                    if(!dao.prescriptionExists(
                            updateId)) {

                        System.out.println(
                                "Prescription ID does not exist!");

                        break;
                    }

                    System.out.print(
                            "Appointment ID : ");

                    int newAppointmentId =
                            sc.nextInt();
                    sc.nextLine();

                    if(appointmentDAO
                            .searchAppointment(
                                    newAppointmentId)
                            == null) {

                        System.out.println(
                                "Appointment ID does not exist!");

                        break;
                    }

                    System.out.print(
                            "Diagnosis : ");

                    String newDiagnosis =
                            sc.nextLine();

                    System.out.print(
                            "Medicine : ");

                    String newMedicine =
                            sc.nextLine();

                    System.out.print(
                            "Next Visit Date (YYYY-MM-DD) : ");

                    String newDate =
                            sc.nextLine();

                    if(!newDate.matches(
                            "\\d{4}-\\d{2}-\\d{2}")) {

                        System.out.println(
                                "Invalid Date Format!");

                        break;
                    }

                    System.out.print(
                            "Remarks : ");

                    String newRemarks =
                            sc.nextLine();

                    dao.updatePrescription(
                            updateId,
                            newAppointmentId,
                            newDiagnosis,
                            newMedicine,
                            newDate,
                            newRemarks
                    );

                    System.out.println(
                            "Prescription Updated Successfully!");

                    break;

                case 5:

                    System.out.print(
                            "Prescription ID : ");

                    int deleteId =
                            sc.nextInt();

                    if(!dao.prescriptionExists(
                            deleteId)) {

                        System.out.println(
                                "Prescription ID does not exist!");

                        break;
                    }

                    dao.deletePrescription(
                            deleteId);

                    System.out.println(
                            "Prescription Deleted Successfully!");

                    break;

                case 6:

                    System.out.println(
                            "Total Prescriptions = "
                            + dao.getPrescriptionCount());

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