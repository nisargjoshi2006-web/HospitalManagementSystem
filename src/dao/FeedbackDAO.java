package dao;

import db.DBConnection;
import model.Feedback;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class FeedbackDAO {

    // INSERT FEEDBACK
    public void addFeedback(
            int patientId,
            int rating,
            String feedbackDate,
            String comments) {

        try {

            Connection con = DBConnection.getConnection();

            String query =
                    "INSERT INTO Feedback(patient_id, rating, feedback_date, comments) VALUES (?, ?, ?, ?)";

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

    // VIEW ALL FEEDBACK
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

    // UPDATE FEEDBACK (updates rating and comments only)
    public void updateFeedback(
            int feedbackId,
            int rating,
            String comments) {

        try {

            Connection con = DBConnection.getConnection();

            String query =
                    "UPDATE Feedback SET rating=?, comments=? WHERE feedback_id=?";

            PreparedStatement pst = con.prepareStatement(query);

            pst.setInt(1, rating);
            pst.setString(2, comments);
            pst.setInt(3, feedbackId);

            int rows = pst.executeUpdate();

            System.out.println("Rows Updated = " + rows);

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DELETE FEEDBACK
    public void deleteFeedback(int feedbackId) {

        try {

            Connection con = DBConnection.getConnection();

            String query =
                    "DELETE FROM Feedback WHERE feedback_id=?";

            PreparedStatement pst = con.prepareStatement(query);

            pst.setInt(1, feedbackId);

            int rows = pst.executeUpdate();

            System.out.println("Rows Deleted = " + rows);

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // GET ALL FEEDBACK
    public ArrayList<Feedback> getAllFeedback() {

        ArrayList<Feedback> list = new ArrayList<>();

        try {

            Connection con = DBConnection.getConnection();

            String query = "SELECT * FROM Feedback";

            PreparedStatement pst = con.prepareStatement(query);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                Feedback f = new Feedback();

                f.setFeedbackId(rs.getInt("feedback_id"));
                f.setPatientId(rs.getInt("patient_id"));
                f.setRating(rs.getInt("rating"));
                f.setFeedbackDate(rs.getDate("feedback_date") != null
                        ? rs.getDate("feedback_date").toString() : "");
                f.setComments(rs.getString("comments"));

                list.add(f);
            }

            con.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return list;
    }

    public int getFeedbackCount() {

    int count = 0;

    try {

        Connection con = DBConnection.getConnection();

        String query = "SELECT COUNT(*) FROM Feedback";

        PreparedStatement pst =
                con.prepareStatement(query);

        ResultSet rs = pst.executeQuery();

        if(rs.next()) {

            count = rs.getInt(1);

        }

        con.close();

    } catch(Exception e) {

        e.printStackTrace();

    }

    return count;
}
    public Feedback searchFeedback(int feedbackId) {

        Feedback f = null;

        try {

            Connection con = DBConnection.getConnection();

            String query =
                    "SELECT * FROM Feedback WHERE feedback_id=?";

            PreparedStatement pst = con.prepareStatement(query);

            pst.setInt(1, feedbackId);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {

                f = new Feedback();

                f.setFeedbackId(rs.getInt("feedback_id"));
                f.setPatientId(rs.getInt("patient_id"));
                f.setRating(rs.getInt("rating"));
                f.setFeedbackDate(rs.getDate("feedback_date") != null
                        ? rs.getDate("feedback_date").toString() : "");
                f.setComments(rs.getString("comments"));
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return f;
    }
}