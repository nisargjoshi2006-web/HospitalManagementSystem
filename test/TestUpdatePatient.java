import dao.PatientDAO;

public class TestUpdatePatient {

    public static void main(String[] args) {

        PatientDAO dao = new PatientDAO();

        dao.updatePatient(
                1,
                "Updated Name",
                "Male",
                30,
                "O+",
                "9999999999",
                "Vellore"
        );

        System.out.println("Patient Updated");
    }
}