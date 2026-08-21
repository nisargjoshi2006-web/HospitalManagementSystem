package dao;

import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FeedbackDAO {

    // INSERT
    public void addFeedback(
            int patientId,
            int rating,
            String feedbackDate,
            String comments) {

        try {

            Connection con = DBConnection.getConnection();

            String query =
                "INSERT INTO Feedback(patient_id, rating, feedback_date, comments) " +
                "VALUES (?, ?, ?, ?)";

            PreparedStatement pst = con.prepareStatement(query);

            pst.setInt(1, patientId);
            pst.setInt(2, rating);
            pst.setDate(3, java.sql.Date.valueOf(feedbackDate));
            pst.setString(4, comments);

            int rows = pst.executeUpdate();

            System.out.println("Rows Inserted = " + rows);

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // VIEW
    public void viewFeedback() {

        try {

            Connection con = DBConnection.getConnection();

            String query = "SELECT * FROM Feedback";

            PreparedStatement pst = con.prepareStatement(query);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                System.out.println(
                    rs.getInt("feedback_id") + " | " +
                    rs.getInt("patient_id") + " | " +
                    rs.getInt("rating") + " | " +
                    rs.getDate("feedback_date") + " | " +
                    rs.getString("comments")
                );
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // UPDATE
    public void updateFeedback(int id, int newRating, String newComments) {

        try {

            Connection con = DBConnection.getConnection();

            String query =
                "UPDATE Feedback SET rating=?, comments=? WHERE feedback_id=?";

            PreparedStatement pst = con.prepareStatement(query);

            pst.setInt(1, newRating);
            pst.setString(2, newComments);
            pst.setInt(3, id);

            int rows = pst.executeUpdate();

            System.out.println("Rows Updated = " + rows);

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deleteFeedback(int id) {

        try {

            Connection con = DBConnection.getConnection();

            String query =
                "DELETE FROM Feedback WHERE feedback_id=?";

            PreparedStatement pst = con.prepareStatement(query);

            pst.setInt(1, id);

            int rows = pst.executeUpdate();

            System.out.println("Rows Deleted = " + rows);

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public ResultSet getAllFeedback() {

    try {

        Connection con = DBConnection.getConnection();

        String query = "SELECT * FROM Feedback";

        PreparedStatement pst =
                con.prepareStatement(query);

        return pst.executeQuery();

    } catch(Exception e) {

        e.printStackTrace();

    }

    return null;
}
}