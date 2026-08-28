import dao.FeedbackDAO;

public class TestUpdateFeedback {

    public static void main(String[] args) {

        FeedbackDAO dao = new FeedbackDAO();

        dao.updateFeedback(
            1,
            4,
            "Good service"
        );
    }
}