package test;

import dao.PatientDAO;
import model.Patient;

import java.util.ArrayList;
import java.util.Scanner;

public class TestPatient {

    public static void main(String[] args) {

        PatientDAO dao = new PatientDAO();
        Scanner sc = new Scanner(System.in);

        while(true) {

            System.out.println("\n===== PATIENT MENU =====");
            System.out.println("1. Add Patient");
            System.out.println("2. View Patients");
            System.out.println("3. Search Patient");
            System.out.println("4. Update Patient");
            System.out.println("5. Delete Patient");
            System.out.println("6. Count Patients");
            System.out.println("0. Exit");

            System.out.print("Enter Choice : ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch(choice) {

                case 1:

                    System.out.print("Patient Name : ");
                    String name = sc.nextLine();

                    if(!name.matches("[a-zA-Z ]+")) {
                        System.out.println("Invalid Name!");
                        break;
                    }

                    System.out.println("Gender:");
                    System.out.println("1. Male");
                    System.out.println("2. Female");

                    int genderChoice = sc.nextInt();
                    sc.nextLine();

                    String gender;

                    if(genderChoice == 1)
                        gender = "Male";
                    else if(genderChoice == 2)
                        gender = "Female";
                    else {
                        System.out.println("Invalid Gender!");
                        break;
                    }

                    System.out.print("Age : ");
                    int age = sc.nextInt();
                    sc.nextLine();

                    if(age < 1 || age > 120) {
                        System.out.println("Invalid Age!");
                        break;
                    }

                    System.out.println("Blood Group:");
                    System.out.println("1. A+");
                    System.out.println("2. A-");
                    System.out.println("3. B+");
                    System.out.println("4. B-");
                    System.out.println("5. AB+");
                    System.out.println("6. AB-");
                    System.out.println("7. O+");
                    System.out.println("8. O-");

                    int bloodChoice = sc.nextInt();
                    sc.nextLine();

                    String bloodGroup = switch(bloodChoice) {
                        case 1 -> "A+";
                        case 2 -> "A-";
                        case 3 -> "B+";
                        case 4 -> "B-";
                        case 5 -> "AB+";
                        case 6 -> "AB-";
                        case 7 -> "O+";
                        case 8 -> "O-";
                        default -> "";
                    };

                    if(bloodGroup.equals("")) {
                        System.out.println("Invalid Blood Group!");
                        break;
                    }

                    System.out.print("Contact Number : ");
                    String contact = sc.nextLine();

                    if(!contact.matches("\\d{10}")) {
                        System.out.println("Invalid Mobile Number!");
                        break;
                    }

                    System.out.print("Address : ");
                    String address = sc.nextLine();

                    System.out.print("Registration Date (YYYY-MM-DD) : ");
                    String date = sc.nextLine();

                    if(!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                        System.out.println("Invalid Date Format!");
                        break;
                    }

                    dao.addPatient(
                            name,
                            gender,
                            age,
                            bloodGroup,
                            contact,
                            address,
                            date
                    );

                    System.out.println("Patient Added Successfully!");
                    break;

                case 2:

                    ArrayList<Patient> patients =
                            dao.getAllPatients();

                    if(patients.isEmpty()) {
                        System.out.println("No Patients Found.");
                    }

                    for(Patient p : patients) {

                        System.out.println(
                                p.getPatientId() + " | " +
                                p.getPatientName() + " | " +
                                p.getGender() + " | " +
                                p.getAge() + " | " +
                                p.getBloodGroup() + " | " +
                                p.getContact() + " | " +
                                p.getAddress()
                        );
                    }

                    break;

                case 3:

                    System.out.print("Enter Patient ID : ");
                    int searchId = sc.nextInt();

                    Patient p =
                            dao.searchPatient(searchId);

                    if(p != null) {

                        System.out.println("\nPatient Found");

                        System.out.println("ID : "
                                + p.getPatientId());

                        System.out.println("Name : "
                                + p.getPatientName());

                        System.out.println("Gender : "
                                + p.getGender());

                        System.out.println("Age : "
                                + p.getAge());

                        System.out.println("Blood Group : "
                                + p.getBloodGroup());

                        System.out.println("Contact : "
                                + p.getContact());

                        System.out.println("Address : "
                                + p.getAddress());
                    }
                    else {

                        System.out.println(
                                "Patient Not Found");
                    }

                    break;

                case 4:

                    System.out.print("Patient ID : ");
                    int updateId = sc.nextInt();
                    sc.nextLine();

                    if(dao.searchPatient(updateId) == null) {
                        System.out.println(
                                "Patient ID does not exist!");
                        break;
                    }

                    System.out.print("Patient Name : ");
                    String newName = sc.nextLine();

                    if(!newName.matches("[a-zA-Z ]+")) {
                        System.out.println("Invalid Name!");
                        break;
                    }

                    System.out.println("Gender:");
                    System.out.println("1. Male");
                    System.out.println("2. Female");

                    int newGenderChoice = sc.nextInt();
                    sc.nextLine();

                    String newGender;

                    if(newGenderChoice == 1)
                        newGender = "Male";
                    else if(newGenderChoice == 2)
                        newGender = "Female";
                    else {
                        System.out.println("Invalid Gender!");
                        break;
                    }

                    System.out.print("Age : ");
                    int newAge = sc.nextInt();
                    sc.nextLine();

                    if(newAge < 1 || newAge > 120) {
                        System.out.println("Invalid Age!");
                        break;
                    }

                    System.out.println("Blood Group:");
                    System.out.println("1. A+");
                    System.out.println("2. A-");
                    System.out.println("3. B+");
                    System.out.println("4. B-");
                    System.out.println("5. AB+");
                    System.out.println("6. AB-");
                    System.out.println("7. O+");
                    System.out.println("8. O-");

                    int newBloodChoice = sc.nextInt();
                    sc.nextLine();

                    String newBloodGroup = switch(newBloodChoice) {
                        case 1 -> "A+";
                        case 2 -> "A-";
                        case 3 -> "B+";
                        case 4 -> "B-";
                        case 5 -> "AB+";
                        case 6 -> "AB-";
                        case 7 -> "O+";
                        case 8 -> "O-";
                        default -> "";
                    };

                    if(newBloodGroup.equals("")) {
                        System.out.println("Invalid Blood Group!");
                        break;
                    }

                    System.out.print("Contact : ");
                    String newContact = sc.nextLine();

                    if(!newContact.matches("\\d{10}")) {
                        System.out.println("Invalid Mobile Number!");
                        break;
                    }

                    System.out.print("Address : ");
                    String newAddress = sc.nextLine();

                    dao.updatePatient(
                            updateId,
                            newName,
                            newGender,
                            newAge,
                            newBloodGroup,
                            newContact,
                            newAddress
                    );

                    System.out.println(
                            "Patient Updated Successfully!");

                    break;

                case 5:

                    System.out.print(
                            "Enter Patient ID : ");

                    int deleteId =
                            sc.nextInt();

                    if(dao.searchPatient(deleteId)
                            == null) {

                        System.out.println(
                                "Patient ID does not exist!");
                        break;
                    }

                    dao.deletePatient(deleteId);

                    System.out.println(
                            "Patient Deleted Successfully!");

                    break;

                case 6:

                    System.out.println(
                            "Total Patients = "
                                    + dao.getPatientCount());

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