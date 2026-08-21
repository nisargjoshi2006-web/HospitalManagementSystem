import dao.AppointmentDAO;

public class TestViewAppointment {

    public static void main(String[] args) {

        AppointmentDAO dao = new AppointmentDAO();

        dao.viewAppointments();
    }
}