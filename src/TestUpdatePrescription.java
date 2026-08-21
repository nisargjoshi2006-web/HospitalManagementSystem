import dao.PrescriptionDAO;

public class TestUpdatePrescription {

    public static void main(String[] args) {

        PrescriptionDAO dao = new PrescriptionDAO();

        dao.updatePrescription(11, "Paracetamol");
    }
}