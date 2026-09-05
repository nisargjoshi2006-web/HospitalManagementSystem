package test;

import dao.FeedbackDAO;
import dao.PatientDAO;
import model.Feedback;

import java.util.ArrayList;
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

                    ArrayList<Feedback> feedbackList =
                            dao.getAllFeedback();

                    if (feedbackList.isEmpty()) {
                        System.out.println("No Feedback Found");
                    }

                    for (Feedback f : feedbackList) {

                        System.out.println(
                                f.getFeedbackId() + " | " +
                                f.getPatientId() + " | " +
                                f.getRating() + " | " +
                                f.getFeedbackDate() + " | " +
                                f.getComments()
                        );
                    }

                    break;

                case 3:

                    System.out.print(
                            "Enter Feedback ID : ");

                    int searchId =
                            sc.nextInt();

                    Feedback found =
                            dao.searchFeedback(searchId);

                    if (found != null) {

                        System.out.println(
                                "Feedback Found");

                        System.out.println(
                                "Feedback ID : " +
                                found.getFeedbackId());

                        System.out.println(
                                "Patient ID : " +
                                found.getPatientId());

                        System.out.println(
                                "Rating : " +
                                found.getRating());

                        System.out.println(
                                "Date : " +
                                found.getFeedbackDate());

                        System.out.println(
                                "Comments : " +
                                found.getComments());
                    }
                    else {

                        System.out.println(
                                "Feedback Not Found");
                    }

                    break;

                case 4:

                    System.out.print(
                            "Feedback ID : ");

                    int feedbackId =
                            sc.nextInt();

                    sc.nextLine();

                    if (dao.searchFeedback(feedbackId) == null) {

                        System.out.println(
                                "Feedback ID does not exist!");

                        break;
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

                    if (dao.searchFeedback(deleteId) == null) {

                        System.out.println(
                                "Feedback ID does not exist!");

                        break;
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