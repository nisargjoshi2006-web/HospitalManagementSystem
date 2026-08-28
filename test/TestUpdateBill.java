import dao.BillingDAO;

public class TestUpdateBill {

    public static void main(String[] args) {

        BillingDAO dao = new BillingDAO();

        dao.updateBill(
            13,
            "Card",
            "Paid"
        );
    }
}