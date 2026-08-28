import dao.BillingDAO;

public class TestViewBill {

    public static void main(String[] args) {

        BillingDAO dao = new BillingDAO();

        dao.viewBills();
    }
}