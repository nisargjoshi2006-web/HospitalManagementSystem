import dao.AppointmentDAO;

public class TestUpdateAppointment {

    public static void main(String[] args) {

        AppointmentDAO dao = new AppointmentDAO();

        dao.updateAppointment(13, "Completed");
    }
}