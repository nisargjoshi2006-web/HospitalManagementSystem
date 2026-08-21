import dao.BillingDAO;

public class TestDeleteBill {

    public static void main(String[] args) {

        BillingDAO dao = new BillingDAO();

        dao.deleteBill(13);
    }
}