package model;

public class Patient {

    private int patientId;
    private String patientName;
    private String gender;
    private int age;
    private String bloodGroup;
    private String contact;
    private String address;

    public Patient() {

    }

    public Patient(int patientId,
                   String patientName,
                   String gender,
                   int age,
                   String bloodGroup,
                   String contact,
                   String address) {

        this.patientId = patientId;
        this.patientName = patientName;
        this.gender = gender;
        this.age = age;
        this.bloodGroup = bloodGroup;
        this.contact = contact;
        this.address = address;
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

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}