package dao;

public class TestEmergency {

    public static void main(String[] args) {

        EmergencyDAO dao = new EmergencyDAO();

        dao.addEmergency(
                1,
                "Accident",
                "High",
                "Admitted",
                3,
                "2026-09-04",
                "12:30:00"
        );

        System.out.println("Emergency Added Successfully");
    }
}