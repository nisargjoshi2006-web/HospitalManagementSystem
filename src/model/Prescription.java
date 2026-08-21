package model;

public class Prescription {

    private int prescriptionId;
    private int appointmentId;
    private String diagnosis;
    private String medicine;
    private String nextVisitDate;
    private String remarks;

    public Prescription() {
    }

    public Prescription(
            int appointmentId,
            String diagnosis,
            String medicine,
            String nextVisitDate,
            String remarks) {

        this.appointmentId = appointmentId;
        this.diagnosis = diagnosis;
        this.medicine = medicine;
        this.nextVisitDate = nextVisitDate;
        this.remarks = remarks;
    }

    public int getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(int prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

    public String getNextVisitDate() {
        return nextVisitDate;
    }

    public void setNextVisitDate(String nextVisitDate) {
        this.nextVisitDate = nextVisitDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}