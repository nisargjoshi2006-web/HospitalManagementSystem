package test;

import dao.DoctorDAO;
import model.Doctor;

import java.util.ArrayList;
import java.util.Scanner;

public class TestDoctor {

    public static void main(String[] args) {

        DoctorDAO dao = new DoctorDAO();
        Scanner sc = new Scanner(System.in);

        while(true) {

            System.out.println("\n===== DOCTOR MENU =====");
            System.out.println("1. Add Doctor");
            System.out.println("2. View Doctors");
            System.out.println("3. Search Doctor");
            System.out.println("4. Update Doctor");
            System.out.println("5. Delete Doctor");
            System.out.println("6. Count Doctors");
            System.out.println("0. Exit");

            System.out.print("Enter Choice : ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch(choice) {

                case 1:

                    System.out.print("Doctor Name : ");
                    String name = sc.nextLine();

                    if(!name.matches("[a-zA-Z ]+")) {
                        System.out.println("Invalid Doctor Name!");
                        break;
                    }

                    System.out.println("Specialization:");
                    System.out.println("1. Cardiologist");
                    System.out.println("2. Neurologist");
                    System.out.println("3. Orthopedic");
                    System.out.println("4. Pediatrician");

                    System.out.print("Enter Choice : ");
                    int specializationId = sc.nextInt();
                    sc.nextLine();

                    if(specializationId < 1 || specializationId > 4) {
                        System.out.println("Invalid Specialization!");
                        break;
                    }

                    System.out.println("Qualification:");
                    System.out.println("1. MBBS");
                    System.out.println("2. MD");
                    System.out.println("3. MS");
                    System.out.println("4. BDS");

                    System.out.print("Enter Choice : ");
                    int qChoice = sc.nextInt();
                    sc.nextLine();

                    String qualification = "";

                    switch(qChoice) {
                        case 1:
                            qualification = "MBBS";
                            break;
                        case 2:
                            qualification = "MD";
                            break;
                        case 3:
                            qualification = "MS";
                            break;
                        case 4:
                            qualification = "BDS";
                            break;
                        default:
                            System.out.println("Invalid Qualification!");
                            continue;
                    }

                    System.out.print("Consultation Fee : ");
                    double fee = sc.nextDouble();
                    sc.nextLine();

                    if(fee <= 0) {
                        System.out.println("Fee must be greater than 0");
                        break;
                    }

                    System.out.print("Contact (10 digits) : ");
                    String contact = sc.nextLine();

                    if(!contact.matches("\\d{10}")) {
                        System.out.println("Invalid Contact Number!");
                        break;
                    }

                    dao.addDoctor(
                            name,
                            specializationId,
                            qualification,
                            fee,
                            contact
                    );

                    System.out.println("Doctor Added Successfully!");

                    break;

                case 2:

                    ArrayList<Doctor> doctors =
                            dao.getAllDoctors();

                    if(doctors.isEmpty()) {
                        System.out.println("No Doctors Found.");
                    }

                    for(Doctor d : doctors) {

                        System.out.println(
                                d.getDoctorId() + " | " +
                                d.getDoctorName() + " | " +
                                d.getSpecializationId() + " | " +
                                d.getQualification() + " | " +
                                d.getConsultationFee() + " | " +
                                d.getContact()
                        );
                    }

                    break;

                case 3:

                    System.out.print("Enter Doctor ID : ");
                    int searchId = sc.nextInt();

                    if(searchId <= 0) {
                        System.out.println("Invalid Doctor ID!");
                        break;
                    }

                    Doctor d =
                            dao.searchDoctor(searchId);

                    if(d != null) {

                        System.out.println("\nDoctor Found");

                        System.out.println(
                                "Doctor ID : " +
                                d.getDoctorId());

                        System.out.println(
                                "Name : " +
                                d.getDoctorName());

                        System.out.println(
                                "Specialization ID : " +
                                d.getSpecializationId());

                        System.out.println(
                                "Qualification : " +
                                d.getQualification());

                        System.out.println(
                                "Consultation Fee : " +
                                d.getConsultationFee());

                        System.out.println(
                                "Contact : " +
                                d.getContact());
                    }
                    else {

                        System.out.println(
                                "Doctor Not Found");
                    }

                    break;

                case 4:

                    System.out.print("Doctor ID : ");
                    int updateId = sc.nextInt();
                    sc.nextLine();

                    if(dao.searchDoctor(updateId) == null) {
                        System.out.println(
                                "Doctor ID does not exist!");
                        break;
                    }

                    System.out.print("Doctor Name : ");
                    String newName = sc.nextLine();

                    if(!newName.matches("[a-zA-Z ]+")) {
                        System.out.println(
                                "Invalid Doctor Name!");
                        break;
                    }

                    System.out.println("Specialization:");
                    System.out.println("1. Cardiologist");
                    System.out.println("2. Neurologist");
                    System.out.println("3. Orthopedic");
                    System.out.println("4. Pediatrician");

                    System.out.print("Enter Choice : ");
                    int newSpecId = sc.nextInt();
                    sc.nextLine();

                    if(newSpecId < 1 || newSpecId > 4) {
                        System.out.println(
                                "Invalid Specialization!");
                        break;
                    }

                    System.out.println("Qualification:");
                    System.out.println("1. MBBS");
                    System.out.println("2. MD");
                    System.out.println("3. MS");
                    System.out.println("4. BDS");

                    System.out.print("Enter Choice : ");
                    int newQChoice = sc.nextInt();
                    sc.nextLine();

                    String newQualification = "";

                    switch(newQChoice) {
                        case 1:
                            newQualification = "MBBS";
                            break;
                        case 2:
                            newQualification = "MD";
                            break;
                        case 3:
                            newQualification = "MS";
                            break;
                        case 4:
                            newQualification = "BDS";
                            break;
                        default:
                            System.out.println(
                                    "Invalid Qualification!");
                            continue;
                    }

                    System.out.print(
                            "Consultation Fee : ");

                    double newFee =
                            sc.nextDouble();
                    sc.nextLine();

                    if(newFee <= 0) {
                        System.out.println(
                                "Fee must be greater than 0");
                        break;
                    }

                    System.out.print(
                            "Contact (10 digits) : ");

                    String newContact =
                            sc.nextLine();

                    if(!newContact.matches("\\d{10}")) {
                        System.out.println(
                                "Invalid Contact Number!");
                        break;
                    }

                    dao.updateDoctor(
                            updateId,
                            newName,
                            newSpecId,
                            newQualification,
                            newFee,
                            newContact
                    );

                    System.out.println(
                            "Doctor Updated Successfully!");

                    break;

                case 5:

                    System.out.print(
                            "Enter Doctor ID : ");

                    int deleteId =
                            sc.nextInt();

                    if(dao.searchDoctor(deleteId)
                            == null) {

                        System.out.println(
                                "Doctor ID does not exist!");
                        break;
                    }

                    dao.deleteDoctor(deleteId);

                    System.out.println(
                            "Doctor Deleted Successfully!");

                    break;

                case 6:

                    System.out.println(
                            "Total Doctors = " +
                            dao.getDoctorCount());

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