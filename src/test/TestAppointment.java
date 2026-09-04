package test;

import dao.AppointmentDAO;
import dao.PatientDAO;
import dao.DoctorDAO;
import model.Appointment;

import java.util.ArrayList;
import java.util.Scanner;

public class TestAppointment {

    public static boolean isValidDate(String date) {
        return date.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    public static boolean isValidTime(String time) {
        return time.matches("\\d{2}:\\d{2}:\\d{2}");
    }

    public static void main(String[] args) {

        AppointmentDAO dao = new AppointmentDAO();
        PatientDAO patientDAO = new PatientDAO();
        DoctorDAO doctorDAO = new DoctorDAO();

        Scanner sc = new Scanner(System.in);

        while(true) {

            System.out.println("\n===== APPOINTMENT MENU =====");
            System.out.println("1. Add Appointment");
            System.out.println("2. View Appointments");
            System.out.println("3. Search Appointment");
            System.out.println("4. Update Appointment");
            System.out.println("5. Delete Appointment");
            System.out.println("6. Count Appointments");
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

                    System.out.print("Doctor ID : ");
                    int doctorId = sc.nextInt();
                    sc.nextLine();

                    if(!doctorDAO.doctorExists(doctorId)) {
                        System.out.println("Doctor ID does not exist!");
                        break;
                    }

                    System.out.print("Appointment Date (YYYY-MM-DD) : ");
                    String date = sc.nextLine();

                    if(!isValidDate(date)) {
                        System.out.println("Invalid Date Format!");
                        break;
                    }

                    System.out.print("Appointment Time (HH:MM:SS) : ");
                    String time = sc.nextLine();

                    if(!isValidTime(time)) {
                        System.out.println("Invalid Time Format!");
                        break;
                    }

                    System.out.print("Room Number : ");
                    String room = sc.nextLine();

                    if(room.isEmpty()) {
                        System.out.println("Room Number cannot be empty!");
                        break;
                    }

                    System.out.println("Select Status");
                    System.out.println("1. Scheduled");
                    System.out.println("2. Completed");
                    System.out.println("3. Cancelled");

                    int statusChoice = sc.nextInt();
                    sc.nextLine();

                    String status = "";

                    switch(statusChoice) {
                        case 1:
                            status = "Scheduled";
                            break;
                        case 2:
                            status = "Completed";
                            break;
                        case 3:
                            status = "Cancelled";
                            break;
                        default:
                            System.out.println("Invalid Status!");
                            break;
                    }

                    dao.addAppointment(
                            patientId,
                            doctorId,
                            date,
                            time,
                            room,
                            status
                    );

                    System.out.println("Appointment Added Successfully!");

                    break;

                case 2:

                    ArrayList<Appointment> list =
                            dao.getAllAppointments();

                    if(list.isEmpty()) {
                        System.out.println("No Appointments Found");
                    }

                    for(Appointment a : list) {

                        System.out.println(
                                a.getAppointmentId() + " | " +
                                a.getPatientId() + " | " +
                                a.getDoctorId() + " | " +
                                a.getAppointmentDate() + " | " +
                                a.getAppointmentTime() + " | " +
                                a.getRoomNumber() + " | " +
                                a.getStatus()
                        );
                    }

                    break;

                case 3:

                    System.out.print(
                            "Enter Appointment ID : ");

                    int searchId = sc.nextInt();

                    Appointment a =
                            dao.searchAppointment(searchId);

                    if(a != null) {

                        System.out.println(
                                "Appointment Found");

                        System.out.println(
                                "Appointment ID : " +
                                a.getAppointmentId());

                        System.out.println(
                                "Patient ID : " +
                                a.getPatientId());

                        System.out.println(
                                "Doctor ID : " +
                                a.getDoctorId());

                        System.out.println(
                                "Date : " +
                                a.getAppointmentDate());

                        System.out.println(
                                "Time : " +
                                a.getAppointmentTime());

                        System.out.println(
                                "Room : " +
                                a.getRoomNumber());

                        System.out.println(
                                "Status : " +
                                a.getStatus());
                    }
                    else {

                        System.out.println(
                                "Appointment Not Found");
                    }

                    break;

                case 4:

                    System.out.print(
                            "Appointment ID : ");

                    int appointmentId =
                            sc.nextInt();
                    sc.nextLine();

                    if(dao.searchAppointment(
                            appointmentId) == null) {

                        System.out.println(
                                "Appointment ID does not exist!");

                        break;
                    }

                    System.out.print("Patient ID : ");
                    int newPatientId =
                            sc.nextInt();
                    sc.nextLine();

                    if(!patientDAO.patientExists(
                            newPatientId)) {

                        System.out.println(
                                "Patient ID does not exist!");

                        break;
                    }

                    System.out.print("Doctor ID : ");
                    int newDoctorId =
                            sc.nextInt();
                    sc.nextLine();

                    if(!doctorDAO.doctorExists(
                            newDoctorId)) {

                        System.out.println(
                                "Doctor ID does not exist!");

                        break;
                    }

                    System.out.print(
                            "Date (YYYY-MM-DD) : ");

                    String newDate =
                            sc.nextLine();

                    if(!isValidDate(newDate)) {

                        System.out.println(
                                "Invalid Date Format!");

                        break;
                    }

                    System.out.print(
                            "Time (HH:MM:SS) : ");

                    String newTime =
                            sc.nextLine();

                    if(!isValidTime(newTime)) {

                        System.out.println(
                                "Invalid Time Format!");

                        break;
                    }

                    System.out.print(
                            "Room Number : ");

                    String newRoom =
                            sc.nextLine();

                    System.out.println(
                            "1. Scheduled");
                    System.out.println(
                            "2. Completed");
                    System.out.println(
                            "3. Cancelled");

                    int newStatusChoice =
                            sc.nextInt();
                    sc.nextLine();

                    String newStatus = "";

                    switch(newStatusChoice) {

                        case 1:
                            newStatus =
                                    "Scheduled";
                            break;

                        case 2:
                            newStatus =
                                    "Completed";
                            break;

                        case 3:
                            newStatus =
                                    "Cancelled";
                            break;

                        default:
                            System.out.println(
                                    "Invalid Status");
                            break;
                    }

                    dao.updateAppointment(
                            appointmentId,
                            newPatientId,
                            newDoctorId,
                            newDate,
                            newTime,
                            newRoom,
                            newStatus
                    );

                    System.out.println(
                            "Appointment Updated Successfully!");

                    break;

                case 5:

                    System.out.print(
                            "Enter Appointment ID : ");

                    int deleteId =
                            sc.nextInt();

                    if(dao.searchAppointment(
                            deleteId) == null) {

                        System.out.println(
                                "Appointment ID does not exist!");

                        break;
                    }

                    dao.deleteAppointment(deleteId);

                    System.out.println(
                            "Appointment Deleted Successfully!");

                    break;

                case 6:

                    System.out.println(
                            "Total Appointments = " +
                            dao.getAppointmentCount());

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