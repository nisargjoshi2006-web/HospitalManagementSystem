import dao.PatientDAO;

public class TestPatient {

    public static void main(String[] args) {

        PatientDAO dao = new PatientDAO();

        dao.addPatient("TestUser", "Male", 20);
    }
}