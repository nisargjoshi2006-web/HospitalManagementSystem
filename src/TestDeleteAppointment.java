import dao.AppointmentDAO;

public class TestDeleteAppointment {

    public static void main(String[] args) {

        AppointmentDAO dao = new AppointmentDAO();

        dao.deleteAppointment(13);
    }
}