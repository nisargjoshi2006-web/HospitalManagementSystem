package test;

import dao.PatientDAO;
import dao.DoctorDAO;
import dao.AppointmentDAO;
import dao.EmergencyDAO;
import dao.FeedbackDAO;
import dao.DoctorScheduleDAO;
import dao.BillingDAO;
import dao.PrescriptionDAO;

public class TestDashboard {

    public static void main(String[] args) {

        PatientDAO patientDAO = new PatientDAO();
        DoctorDAO doctorDAO = new DoctorDAO();
        AppointmentDAO appointmentDAO = new AppointmentDAO();
        EmergencyDAO emergencyDAO = new EmergencyDAO();
        FeedbackDAO feedbackDAO = new FeedbackDAO();
        DoctorScheduleDAO scheduleDAO = new DoctorScheduleDAO();
        BillingDAO billingDAO = new BillingDAO();
        PrescriptionDAO prescriptionDAO = new PrescriptionDAO();

        System.out.println("\n===== HOSPITAL DASHBOARD =====");
        System.out.println("Total Patients        : " + patientDAO.getPatientCount());
        System.out.println("Total Doctors         : " + doctorDAO.getDoctorCount());
        System.out.println("Total Appointments    : " + appointmentDAO.getAppointmentCount());
        System.out.println("Total Emergencies     : " + emergencyDAO.getEmergencyCount());
        System.out.println("Total Prescriptions   : " + prescriptionDAO.getPrescriptionCount());
        System.out.println("Total Bills           : " + billingDAO.getBillCount());
        System.out.printf("Total Revenue (Paid)  : ₹ %.2f\n", billingDAO.getTotalRevenue());
        System.out.println("Total Feedbacks       : " + feedbackDAO.getFeedbackCount());
        System.out.println("Total Schedules       : " + scheduleDAO.getScheduleCount());
    }
}
