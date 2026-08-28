import dao.PrescriptionDAO;

public class TestViewPrescription {

    public static void main(String[] args) {

        PrescriptionDAO dao = new PrescriptionDAO();

        dao.viewPrescriptions();
    }
}