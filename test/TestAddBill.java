import dao.BillingDAO;

public class TestAddBill {

    public static void main(String[] args) {

        BillingDAO dao = new BillingDAO();

        dao.addBill(
            1,
            "2026-08-21",
            "UPI",
            "Paid"
        );
    }
}