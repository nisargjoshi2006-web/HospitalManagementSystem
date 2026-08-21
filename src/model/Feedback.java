package model;

public class Feedback {

    private int feedbackId;
    private int patientId;
    private int rating;
    private String feedbackDate;
    private String comments;

    public Feedback() {
    }

    public Feedback(int patientId,
                    int rating,
                    String feedbackDate,
                    String comments) {

        this.patientId = patientId;
        this.rating = rating;
        this.feedbackDate = feedbackDate;
        this.comments = comments;
    }

    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getFeedbackDate() {
        return feedbackDate;
    }

    public void setFeedbackDate(String feedbackDate) {
        this.feedbackDate = feedbackDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}