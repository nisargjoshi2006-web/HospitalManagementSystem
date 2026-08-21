package model;

public class Doctor {

    private int doctorId;
    private String doctorName;
    private int specializationId;
    private String qualification;
    private double consultationFee;
    private String contact;

    public Doctor() {

    }

    public Doctor(
            String doctorName,
            int specializationId,
            String qualification,
            double consultationFee,
            String contact) {

        this.doctorName = doctorName;
        this.specializationId = specializationId;
        this.qualification = qualification;
        this.consultationFee = consultationFee;
        this.contact = contact;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public int getSpecializationId() {
        return specializationId;
    }

    public void setSpecializationId(int specializationId) {
        this.specializationId = specializationId;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public double getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(double consultationFee) {
        this.consultationFee = consultationFee;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}