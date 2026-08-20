import dao.PatientDAO;

public class TestUpdatePatient {

    public static void main(String[] args) {

        PatientDAO dao = new PatientDAO();

        dao.updatePatient(12, "Nisarg Kumar");
    }
}
