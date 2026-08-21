package model;

public class Doctor {

    private int doctorId;
    private String doctorName;
    private int specializationId;
    private int experience;
    private String contact;

    public Doctor() {}

    public Doctor(
            String doctorName,
            int specializationId,
            int experience,
            String contact) {

        this.doctorName = doctorName;
        this.specializationId = specializationId;
        this.experience = experience;
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

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}