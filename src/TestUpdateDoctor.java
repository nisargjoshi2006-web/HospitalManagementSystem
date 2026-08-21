import dao.DoctorDAO;

public class TestUpdateDoctor {

    public static void main(String[] args) {

        DoctorDAO dao = new DoctorDAO();

        dao.updateDoctor(1, "Dr. Rajesh Sharma");
    }
}