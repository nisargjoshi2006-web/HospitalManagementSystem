import dao.FeedbackDAO;

public class TestViewFeedback {

    public static void main(String[] args) {

        FeedbackDAO dao = new FeedbackDAO();

        dao.viewFeedback();
    }
}