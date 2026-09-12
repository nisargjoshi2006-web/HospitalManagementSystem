package model;

import java.math.BigDecimal;

public class LabTest {

    private int testId;
    private int patientId;
    private int doctorId;
    private String testName;
    private String testDate;
    private BigDecimal cost;
    private String result;
    private String status;

    public LabTest() {}

    public LabTest(int testId, int patientId, int doctorId, String testName, String testDate, BigDecimal cost, String result, String status) {
        this.testId = testId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.testName = testName;
        this.testDate = testDate;
        this.cost = cost;
        this.result = result;
        this.status = status;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTestDate() {
        return testDate;
    }

    public void setTestDate(String testDate) {
        this.testDate = testDate;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
