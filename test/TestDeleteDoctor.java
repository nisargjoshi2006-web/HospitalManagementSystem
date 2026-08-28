import dao.DoctorDAO;

public class TestDeleteDoctor {
    public static void main(String[] args) {

        DoctorDAO dao = new DoctorDAO();

        dao.deleteDoctor(7);
    }
}