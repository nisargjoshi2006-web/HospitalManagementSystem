package test;

import dao.DoctorDAO;
import dao.DoctorScheduleDAO;
import model.DoctorSchedule;

import java.util.ArrayList;
import java.util.Scanner;

public class TestDoctorSchedule {

    public static boolean isValidDay(String day) {

        return day.equalsIgnoreCase("Monday") ||
               day.equalsIgnoreCase("Tuesday") ||
               day.equalsIgnoreCase("Wednesday") ||
               day.equalsIgnoreCase("Thursday") ||
               day.equalsIgnoreCase("Friday") ||
               day.equalsIgnoreCase("Saturday") ||
               day.equalsIgnoreCase("Sunday");
    }

    public static boolean isValidTime(String time) {

        return time.matches("^([01]\\d|2[0-3]):([0-5]\\d):([0-5]\\d)$");
    }

    public static void main(String[] args) {

        DoctorScheduleDAO dao =
                new DoctorScheduleDAO();

        DoctorDAO doctorDAO =
                new DoctorDAO();

        Scanner sc =
                new Scanner(System.in);

        while(true) {

            System.out.println("\n===== DOCTOR SCHEDULE MENU =====");
            System.out.println("1. Add Schedule");
            System.out.println("2. View Schedules");
            System.out.println("3. Search Schedule");
            System.out.println("4. Update Schedule");
            System.out.println("5. Delete Schedule");
            System.out.println("6. Count Schedules");
            System.out.println("0. Exit");

            System.out.print("Enter Choice : ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch(choice) {

                case 1:

                    System.out.print("Doctor ID : ");
                    int doctorId = sc.nextInt();
                    sc.nextLine();

                    if(!doctorDAO.doctorExists(doctorId)) {

                        System.out.println(
                                "Doctor ID does not exist!");
                        break;
                    }

                    System.out.println(
                            "Days Available:");
                    System.out.println(
                            "Monday Tuesday Wednesday Thursday Friday Saturday Sunday");

                    System.out.print("Day : ");
                    String day = sc.nextLine();

                    if(!isValidDay(day)) {

                        System.out.println(
                                "Invalid Day!");
                        break;
                    }

                    System.out.print(
                            "Start Time (HH:MM:SS) : ");
                    String startTime =
                            sc.nextLine();

                    if(!isValidTime(startTime)) {

                        System.out.println(
                                "Invalid Start Time!");
                        break;
                    }

                    System.out.print(
                            "End Time (HH:MM:SS) : ");
                    String endTime =
                            sc.nextLine();

                    if(!isValidTime(endTime)) {

                        System.out.println(
                                "Invalid End Time!");
                        break;
                    }

                    dao.addSchedule(
                            doctorId,
                            day,
                            startTime,
                            endTime
                    );

                    System.out.println(
                            "Schedule Added Successfully!");

                    break;

                case 2:

                    ArrayList<DoctorSchedule> list =
                            dao.getAllSchedules();

                    if(list.isEmpty()) {

                        System.out.println(
                                "No Schedules Found");
                    }

                    for(DoctorSchedule ds : list) {

                        System.out.println(
                                ds.getScheduleId() + " | " +
                                ds.getDoctorId() + " | " +
                                ds.getDayOfWeek() + " | " +
                                ds.getStartTime() + " | " +
                                ds.getEndTime()
                        );
                    }

                    break;

                case 3:

                    System.out.print(
                            "Enter Schedule ID : ");

                    int searchId =
                            sc.nextInt();

                    DoctorSchedule ds =
                            dao.searchSchedule(searchId);

                    if(ds != null) {

                        System.out.println(
                                "\nSchedule Found");

                        System.out.println(
                                "Schedule ID : " +
                                ds.getScheduleId());

                        System.out.println(
                                "Doctor ID : " +
                                ds.getDoctorId());

                        System.out.println(
                                "Day : " +
                                ds.getDayOfWeek());

                        System.out.println(
                                "Start Time : " +
                                ds.getStartTime());

                        System.out.println(
                                "End Time : " +
                                ds.getEndTime());
                    }
                    else {

                        System.out.println(
                                "Schedule Not Found");
                    }

                    break;

                case 4:

                    System.out.print(
                            "Schedule ID : ");

                    int updateId =
                            sc.nextInt();
                    sc.nextLine();

                    if(dao.searchSchedule(updateId)
                            == null) {

                        System.out.println(
                                "Schedule ID does not exist!");
                        break;
                    }

                    System.out.print(
                            "Doctor ID : ");

                    int newDoctorId =
                            sc.nextInt();
                    sc.nextLine();

                    if(!doctorDAO.doctorExists(
                            newDoctorId)) {

                        System.out.println(
                                "Doctor ID does not exist!");
                        break;
                    }

                    System.out.println(
                            "Days Available:");
                    System.out.println(
                            "Monday Tuesday Wednesday Thursday Friday Saturday Sunday");

                    System.out.print(
                            "Day : ");

                    String newDay =
                            sc.nextLine();

                    if(!isValidDay(newDay)) {

                        System.out.println(
                                "Invalid Day!");
                        break;
                    }

                    System.out.print(
                            "Start Time (HH:MM:SS) : ");

                    String newStart =
                            sc.nextLine();

                    if(!isValidTime(newStart)) {

                        System.out.println(
                                "Invalid Start Time!");
                        break;
                    }

                    System.out.print(
                            "End Time (HH:MM:SS) : ");

                    String newEnd =
                            sc.nextLine();

                    if(!isValidTime(newEnd)) {

                        System.out.println(
                                "Invalid End Time!");
                        break;
                    }

                    dao.updateSchedule(
                            updateId,
                            newDoctorId,
                            newDay,
                            newStart,
                            newEnd
                    );

                    System.out.println(
                            "Schedule Updated Successfully!");

                    break;

                case 5:

                    System.out.print(
                            "Enter Schedule ID : ");

                    int deleteId =
                            sc.nextInt();

                    if(dao.searchSchedule(deleteId)
                            == null) {

                        System.out.println(
                                "Schedule ID does not exist!");
                        break;
                    }

                    dao.deleteSchedule(deleteId);

                    System.out.println(
                            "Schedule Deleted Successfully!");

                    break;

                case 6:

                    System.out.println(
                            "Total Schedules = " +
                            dao.getScheduleCount());

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