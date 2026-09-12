package model;

import java.math.BigDecimal;

public class BedAllocation {

    private int allocationId;
    private int patientId;
    private String wardType;
    private String bedNumber;
    private String admitDate;
    private String dischargeDate;
    private BigDecimal dailyCharge;
    private String status;

    public BedAllocation() {}

    public BedAllocation(int allocationId, int patientId, String wardType, String bedNumber, String admitDate, String dischargeDate, BigDecimal dailyCharge, String status) {
        this.allocationId = allocationId;
        this.patientId = patientId;
        this.wardType = wardType;
        this.bedNumber = bedNumber;
        this.admitDate = admitDate;
        this.dischargeDate = dischargeDate;
        this.dailyCharge = dailyCharge;
        this.status = status;
    }

    public int getAllocationId() {
        return allocationId;
    }

    public void setAllocationId(int allocationId) {
        this.allocationId = allocationId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getWardType() {
        return wardType;
    }

    public void setWardType(String wardType) {
        this.wardType = wardType;
    }

    public String getBedNumber() {
        return bedNumber;
    }

    public void setBedNumber(String bedNumber) {
        this.bedNumber = bedNumber;
    }

    public String getAdmitDate() {
        return admitDate;
    }

    public void setAdmitDate(String admitDate) {
        this.admitDate = admitDate;
    }

    public String getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(String dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public BigDecimal getDailyCharge() {
        return dailyCharge;
    }

    public void setDailyCharge(BigDecimal dailyCharge) {
        this.dailyCharge = dailyCharge;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
