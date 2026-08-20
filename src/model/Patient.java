package model;

public class Patient {

    private int patientId;
    private String patientName;
    private String gender;
    private int age;

    public Patient() {
    }

    public Patient(int patientId, String patientName,
                   String gender, int age) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.gender = gender;
        this.age = age;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}