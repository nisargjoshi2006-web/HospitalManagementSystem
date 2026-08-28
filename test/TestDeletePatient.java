import dao.PatientDAO;

public class TestDeletePatient {

    public static void main(String[] args) {

        PatientDAO dao = new PatientDAO();

        dao.deletePatient(13);

    }
}