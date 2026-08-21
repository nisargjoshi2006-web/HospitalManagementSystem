import dao.PrescriptionDAO;

public class TestAddPrescription {

    public static void main(String[] args) {

        PrescriptionDAO dao = new PrescriptionDAO();

        dao.addPrescription(
            1,
            "High Blood Pressure",
            "Amlodipine",
            "2026-09-20",
            "Regular checkup"
        );
    }
}