import dao.PrescriptionDAO;

public class TestDeletePrescription {

    public static void main(String[] args) {

        PrescriptionDAO dao = new PrescriptionDAO();

        dao.deletePrescription(11);
    }
}