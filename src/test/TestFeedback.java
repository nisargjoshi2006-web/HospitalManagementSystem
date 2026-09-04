package test;

import dao.FeedbackDAO;
import dao.PatientDAO;

import java.sql.ResultSet;
import java.util.Scanner;

public class TestFeedback {

    public static boolean isValidDate(String date) {
        return date.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    public static void main(String[] args) {

        FeedbackDAO dao = new FeedbackDAO();
        PatientDAO patientDAO = new PatientDAO();

        Scanner sc = new Scanner(System.in);

        while(true) {

            System.out.println("\n===== FEEDBACK MENU =====");
            System.out.println("1. Add Feedback");
            System.out.println("2. View Feedback");
            System.out.println("3. Search Feedback");
            System.out.println("4. Update Feedback");
            System.out.println("5. Delete Feedback");
            System.out.println("6. Count Feedback");
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

                        System.out.println(
                                "Patient ID does not exist!");

                        break;
                    }

                    System.out.println("\nRating");
                    System.out.println("1. Poor");
                    System.out.println("2. Fair");
                    System.out.println("3. Good");
                    System.out.println("4. Very Good");
                    System.out.println("5. Excellent");

                    int rating = sc.nextInt();
                    sc.nextLine();

                    if(rating < 1 || rating > 5) {

                        System.out.println(
                                "Rating must be between 1 and 5");

                        break;
                    }

                    System.out.print(
                            "Feedback Date (YYYY-MM-DD) : ");

                    String date = sc.nextLine();

                    if(!isValidDate(date)) {

                        System.out.println(
                                "Invalid Date Format!");

                        break;
                    }

                    System.out.print(
                            "Comments : ");

                    String comments =
                            sc.nextLine();

                    if(comments.trim().isEmpty()) {

                        System.out.println(
                                "Comments cannot be empty!");

                        break;
                    }

                    dao.addFeedback(
                            patientId,
                            rating,
                            date,
                            comments
                    );

                    System.out.println(
                            "Feedback Added Successfully!");

                    break;

                case 2:

                    try {

                        ResultSet rs =
                                dao.getAllFeedback();

                        while(rs.next()) {

                            System.out.println(
                                    rs.getInt("feedback_id") + " | " +
                                    rs.getInt("patient_id") + " | " +
                                    rs.getInt("rating") + " | " +
                                    rs.getDate("feedback_date") + " | " +
                                    rs.getString("comments")
                            );
                        }

                    } catch(Exception e) {

                        e.printStackTrace();
                    }

                    break;

                case 3:

                    System.out.print(
                            "Enter Feedback ID : ");

                    int searchId =
                            sc.nextInt();

                    try {

                        ResultSet rs =
                                dao.searchFeedback(searchId);

                        if(rs.next()) {

                            System.out.println(
                                    "Feedback Found");

                            System.out.println(
                                    "Feedback ID : " +
                                    rs.getInt("feedback_id"));

                            System.out.println(
                                    "Patient ID : " +
                                    rs.getInt("patient_id"));

                            System.out.println(
                                    "Rating : " +
                                    rs.getInt("rating"));

                            System.out.println(
                                    "Date : " +
                                    rs.getDate("feedback_date"));

                            System.out.println(
                                    "Comments : " +
                                    rs.getString("comments"));
                        }
                        else {

                            System.out.println(
                                    "Feedback Not Found");
                        }

                    } catch(Exception e) {

                        e.printStackTrace();
                    }

                    break;

                case 4:

                    System.out.print(
                            "Feedback ID : ");

                    int feedbackId =
                            sc.nextInt();

                    sc.nextLine();

                    try {

                        ResultSet rs =
                                dao.searchFeedback(feedbackId);

                        if(!rs.next()) {

                            System.out.println(
                                    "Feedback ID does not exist!");

                            break;
                        }

                    } catch(Exception e) {

                        e.printStackTrace();
                    }

                    System.out.println(
                            "1. Poor");
                    System.out.println(
                            "2. Fair");
                    System.out.println(
                            "3. Good");
                    System.out.println(
                            "4. Very Good");
                    System.out.println(
                            "5. Excellent");

                    int newRating =
                            sc.nextInt();

                    sc.nextLine();

                    if(newRating < 1 ||
                            newRating > 5) {

                        System.out.println(
                                "Invalid Rating!");

                        break;
                    }

                    System.out.print(
                            "Comments : ");

                    String newComments =
                            sc.nextLine();

                    dao.updateFeedback(
                            feedbackId,
                            newRating,
                            newComments
                    );

                    System.out.println(
                            "Feedback Updated Successfully!");

                    break;

                case 5:

                    System.out.print(
                            "Enter Feedback ID : ");

                    int deleteId =
                            sc.nextInt();

                    try {

                        ResultSet rs =
                                dao.searchFeedback(deleteId);

                        if(!rs.next()) {

                            System.out.println(
                                    "Feedback ID does not exist!");

                            break;
                        }

                    } catch(Exception e) {

                        e.printStackTrace();
                    }

                    dao.deleteFeedback(deleteId);

                    System.out.println(
                            "Feedback Deleted Successfully!");

                    break;

                case 6:

                    System.out.println(
                            "Total Feedback = " +
                            dao.getFeedbackCount());

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