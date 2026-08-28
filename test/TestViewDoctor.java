import dao.DoctorDAO;

public class TestViewDoctor {

    public static void main(String[] args) {

        DoctorDAO dao = new DoctorDAO();

        dao.viewDoctors();
    }
}