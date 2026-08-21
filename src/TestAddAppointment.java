import dao.AppointmentDAO;

public class TestAddAppointment {

    public static void main(String[] args) {

        AppointmentDAO dao = new AppointmentDAO();

        dao.addAppointment(
            1,
            1,
            "2026-08-25",
            "10:00:00",
            "101",
            "Pending"
        );
    }
}