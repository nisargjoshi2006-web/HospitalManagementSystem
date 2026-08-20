import dao.PatientDAO;

public class TestViewPatient {

    public static void main(String[] args) {

        PatientDAO dao = new PatientDAO();

        dao.viewPatients();
    }
}