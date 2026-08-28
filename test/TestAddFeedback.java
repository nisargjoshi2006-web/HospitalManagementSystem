import dao.FeedbackDAO;

public class TestAddFeedback {

    public static void main(String[] args) {

        FeedbackDAO dao = new FeedbackDAO();

        dao.addFeedback(
            1,
            5,
            "2026-08-21",
            "Very good service"
        );
    }
}