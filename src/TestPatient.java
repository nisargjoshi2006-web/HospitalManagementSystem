import dao.PatientDAO;

public class TestPatient {

    public static void main(String[] args) {

        PatientDAO dao = new PatientDAO();

        dao.addPatient(
                "Nisarg Kumar",
                "Male",
                20,
                "B+",
                "9876543210",
                "Vellore",
                "2026-08-20"
        );
    }
}