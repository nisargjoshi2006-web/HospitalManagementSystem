package test;

import dao.EmergencyDAO;
import dao.PatientDAO;
import dao.DoctorDAO;
import model.Emergency;

import java.util.ArrayList;
import java.util.Scanner;

public class TestEmergency {

    public static boolean isValidDate(String date) {
        return date.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    public static boolean isValidTime(String time) {
        return time.matches("\\d{2}:\\d{2}:\\d{2}");
    }

    public static void main(String[] args) {

        EmergencyDAO dao = new EmergencyDAO();
        PatientDAO patientDAO = new PatientDAO();
        DoctorDAO doctorDAO = new DoctorDAO();

        Scanner sc = new Scanner(System.in);

        while(true) {

            System.out.println("\n===== EMERGENCY MENU =====");
            System.out.println("1. Add Emergency");
            System.out.println("2. View Emergencies");
            System.out.println("3. Search Emergency");
            System.out.println("4. Update Emergency");
            System.out.println("5. Delete Emergency");
            System.out.println("6. Count Emergencies");
            System.out.println("0. Exit");

            System.out.print("Enter Choice : ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch(choice) {

                case 1:

                    System.out.print("Patient ID : ");
                    int patientId = sc.nextInt();
                    sc.nextLine();

                    if(!patientDAO.patientExists(patientId)) {
                        System.out.println("Patient ID does not exist!");
                        break;
                    }

                    System.out.print("Assigned Doctor ID : ");
                    int doctorId = sc.nextInt();
                    sc.nextLine();

                    if(!doctorDAO.doctorExists(doctorId)) {
                        System.out.println("Doctor ID does not exist!");
                        break;
                    }

                    System.out.println("\nSelect Emergency Type");
                    System.out.println("1. Accident");
                    System.out.println("2. Heart Attack");
                    System.out.println("3. Stroke");
                    System.out.println("4. Burn Injury");
                    System.out.println("5. Poisoning");

                    int typeChoice = sc.nextInt();
                    sc.nextLine();

                    String emergencyType = "";

                    switch(typeChoice) {
                        case 1:
                            emergencyType = "Accident";
                            break;
                        case 2:
                            emergencyType = "Heart Attack";
                            break;
                        case 3:
                            emergencyType = "Stroke";
                            break;
                        case 4:
                            emergencyType = "Burn Injury";
                            break;
                        case 5:
                            emergencyType = "Poisoning";
                            break;
                        default:
                            System.out.println("Invalid Emergency Type!");
                            break;
                    }

                    System.out.println("\nSelect Priority Level");
                    System.out.println("1. Low");
                    System.out.println("2. Medium");
                    System.out.println("3. High");
                    System.out.println("4. Critical");

                    int priorityChoice = sc.nextInt();
                    sc.nextLine();

                    String priority = "";

                    switch(priorityChoice) {
                        case 1:
                            priority = "Low";
                            break;
                        case 2:
                            priority = "Medium";
                            break;
                        case 3:
                            priority = "High";
                            break;
                        case 4:
                            priority = "Critical";
                            break;
                        default:
                            System.out.println("Invalid Priority!");
                            break;
                    }

                    System.out.println("\nSelect Status");
                    System.out.println("1. Waiting");
                    System.out.println("2. Under Treatment");
                    System.out.println("3. Admitted");
                    System.out.println("4. Discharged");

                    int statusChoice = sc.nextInt();
                    sc.nextLine();

                    String status = "";

                    switch(statusChoice) {
                        case 1:
                            status = "Waiting";
                            break;
                        case 2:
                            status = "Under Treatment";
                            break;
                        case 3:
                            status = "Admitted";
                            break;
                        case 4:
                            status = "Discharged";
                            break;
                        default:
                            System.out.println("Invalid Status!");
                            break;
                    }

                    System.out.print("Arrival Date (YYYY-MM-DD) : ");
                    String arrivalDate = sc.nextLine();

                    if(!isValidDate(arrivalDate)) {
                        System.out.println("Invalid Date Format!");
                        break;
                    }

                    System.out.print("Arrival Time (HH:MM:SS) : ");
                    String arrivalTime = sc.nextLine();

                    if(!isValidTime(arrivalTime)) {
                        System.out.println("Invalid Time Format!");
                        break;
                    }

                    dao.addEmergency(
                            patientId,
                            emergencyType,
                            priority,
                            status,
                            doctorId,
                            arrivalDate,
                            arrivalTime
                    );

                    System.out.println("Emergency Added Successfully!");

                    break;

                case 2:

                    ArrayList<Emergency> list =
                            dao.getAllEmergencies();

                    if(list.isEmpty()) {
                        System.out.println("No Emergency Records Found");
                    }

                    for(Emergency e : list) {

                        System.out.println(
                                e.getEmergencyId() + " | " +
                                e.getPatientId() + " | " +
                                e.getEmergencyType() + " | " +
                                e.getPriorityLevel() + " | " +
                                e.getStatus() + " | " +
                                e.getAssignedDoctor() + " | " +
                                e.getArrivalDate() + " | " +
                                e.getArrivalTime()
                        );
                    }

                    break;

                case 3:

                    System.out.print("Enter Emergency ID : ");
                    int searchId = sc.nextInt();

                    Emergency e =
                            dao.searchEmergency(searchId);

                    if(e != null) {

                        System.out.println("\nEmergency Found");

                        System.out.println(
                                "Emergency ID : " +
                                e.getEmergencyId());

                        System.out.println(
                                "Patient ID : " +
                                e.getPatientId());

                        System.out.println(
                                "Emergency Type : " +
                                e.getEmergencyType());

                        System.out.println(
                                "Priority Level : " +
                                e.getPriorityLevel());

                        System.out.println(
                                "Status : " +
                                e.getStatus());

                        System.out.println(
                                "Assigned Doctor : " +
                                e.getAssignedDoctor());

                        System.out.println(
                                "Arrival Date : " +
                                e.getArrivalDate());

                        System.out.println(
                                "Arrival Time : " +
                                e.getArrivalTime());
                    }
                    else {

                        System.out.println(
                                "Emergency Not Found");
                    }

                    break;

                case 4:

                    System.out.print("Emergency ID : ");
                    int emergencyId = sc.nextInt();
                    sc.nextLine();

                    if(dao.searchEmergency(emergencyId) == null) {
                        System.out.println(
                                "Emergency ID does not exist!");
                        break;
                    }

                    System.out.print("Patient ID : ");
                    int newPatientId = sc.nextInt();
                    sc.nextLine();

                    if(!patientDAO.patientExists(newPatientId)) {
                        System.out.println(
                                "Patient ID does not exist!");
                        break;
                    }

                    System.out.print("Doctor ID : ");
                    int newDoctorId = sc.nextInt();
                    sc.nextLine();

                    if(!doctorDAO.doctorExists(newDoctorId)) {
                        System.out.println(
                                "Doctor ID does not exist!");
                        break;
                    }

                    System.out.println("1. Accident");
                    System.out.println("2. Heart Attack");
                    System.out.println("3. Stroke");
                    System.out.println("4. Burn Injury");
                    System.out.println("5. Poisoning");

                    int newTypeChoice = sc.nextInt();
                    sc.nextLine();

                    String newType = "";

                    switch(newTypeChoice) {
                        case 1:
                            newType = "Accident";
                            break;
                        case 2:
                            newType = "Heart Attack";
                            break;
                        case 3:
                            newType = "Stroke";
                            break;
                        case 4:
                            newType = "Burn Injury";
                            break;
                        case 5:
                            newType = "Poisoning";
                            break;
                    }

                    System.out.println("1. Low");
                    System.out.println("2. Medium");
                    System.out.println("3. High");
                    System.out.println("4. Critical");

                    int newPriorityChoice = sc.nextInt();
                    sc.nextLine();

                    String newPriority = "";

                    switch(newPriorityChoice) {
                        case 1:
                            newPriority = "Low";
                            break;
                        case 2:
                            newPriority = "Medium";
                            break;
                        case 3:
                            newPriority = "High";
                            break;
                        case 4:
                            newPriority = "Critical";
                            break;
                    }

                    System.out.println("1. Waiting");
                    System.out.println("2. Under Treatment");
                    System.out.println("3. Admitted");
                    System.out.println("4. Discharged");

                    int newStatusChoice = sc.nextInt();
                    sc.nextLine();

                    String newStatus = "";

                    switch(newStatusChoice) {
                        case 1:
                            newStatus = "Waiting";
                            break;
                        case 2:
                            newStatus = "Under Treatment";
                            break;
                        case 3:
                            newStatus = "Admitted";
                            break;
                        case 4:
                            newStatus = "Discharged";
                            break;
                    }

                    System.out.print("Arrival Date (YYYY-MM-DD) : ");
                    String newDate = sc.nextLine();

                    if(!isValidDate(newDate)) {
                        System.out.println("Invalid Date Format!");
                        break;
                    }

                    System.out.print("Arrival Time (HH:MM:SS) : ");
                    String newTime = sc.nextLine();

                    if(!isValidTime(newTime)) {
                        System.out.println("Invalid Time Format!");
                        break;
                    }

                    dao.updateEmergency(
                            emergencyId,
                            newPatientId,
                            newType,
                            newPriority,
                            newStatus,
                            newDoctorId,
                            newDate,
                            newTime
                    );

                    System.out.println(
                            "Emergency Updated Successfully!");

                    break;

                case 5:

                    System.out.print(
                            "Enter Emergency ID : ");

                    int deleteId = sc.nextInt();

                    if(dao.searchEmergency(deleteId) == null) {

                        System.out.println(
                                "Emergency ID does not exist!");

                        break;
                    }

                    dao.deleteEmergency(deleteId);

                    System.out.println(
                            "Emergency Deleted Successfully!");

                    break;

                case 6:

                    System.out.println(
                            "Total Emergencies = " +
                            dao.getEmergencyCount());

                    break;

                case 0:

                    System.out.println("Exiting...");

                    sc.close();

                    System.exit(0);

                default:

                    System.out.println(
                            "Invalid Choice");
            }
        }
    }
}