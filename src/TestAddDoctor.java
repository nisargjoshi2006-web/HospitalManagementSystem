import dao.DoctorDAO;

public class TestAddDoctor {
    public static void main(String[] args) {

        DoctorDAO dao = new DoctorDAO();

        dao.addDoctor(
            "Dr. Test",
            1,
            "MBBS",
            500,
            "9999999999"
        );
    }
}