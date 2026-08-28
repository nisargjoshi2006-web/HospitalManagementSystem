import dao.FeedbackDAO;

public class TestDeleteFeedback {

    public static void main(String[] args) {

        FeedbackDAO dao = new FeedbackDAO();

        dao.deleteFeedback(10);
    }
}