package dao;

import db.DBConnection;
import model.BedAllocation;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BedAllocationDAO {

    // CHECK IF BED IS OCCUPIED
    public boolean isBedOccupied(String wardType, String bedNumber) {
        String sql = "SELECT COUNT(*) FROM Bed_Allocation WHERE ward_type=? AND bed_number=? AND status='Occupied'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, wardType);
            pst.setString(2, bedNumber);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // CHECK IF PATIENT IS CURRENTLY ADMITTED
    public boolean isPatientAdmitted(int patientId) {
        String sql = "SELECT COUNT(*) FROM Bed_Allocation WHERE patient_id=? AND status='Occupied'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, patientId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ALLOCATE BED WITH VALIDATION
    public void allocateBed(int patientId, String wardType, String bedNumber, String admitDate, BigDecimal dailyCharge, String status) throws Exception {
        if (isPatientAdmitted(patientId)) {
            throw new IllegalArgumentException("Patient ID " + patientId + " is already admitted to another active bed!");
        }
        if (isBedOccupied(wardType, bedNumber)) {
            throw new IllegalArgumentException("Bed " + bedNumber + " in " + wardType + " is currently OCCUPIED!");
        }

        String sql = "INSERT INTO Bed_Allocation (patient_id, ward_type, bed_number, admit_date, daily_charge, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, patientId);
            pst.setString(2, wardType);
            pst.setString(3, bedNumber);
            pst.setDate(4, Date.valueOf(admitDate));
            pst.setBigDecimal(5, dailyCharge);
            pst.setString(6, status);
            pst.executeUpdate();
        }
    }

    // GET ALL ACTIVE ALLOCATIONS
    public ArrayList<BedAllocation> getActiveAllocations() {
        ArrayList<BedAllocation> list = new ArrayList<>();
        String sql = "SELECT * FROM Bed_Allocation WHERE status='Occupied' ORDER BY admit_date DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                BedAllocation b = new BedAllocation();
                b.setAllocationId(rs.getInt("allocation_id"));
                b.setPatientId(rs.getInt("patient_id"));
                b.setWardType(rs.getString("ward_type"));
                b.setBedNumber(rs.getString("bed_number"));
                b.setAdmitDate(rs.getDate("admit_date") != null ? rs.getDate("admit_date").toString() : "");
                b.setDischargeDate(rs.getDate("discharge_date") != null ? rs.getDate("discharge_date").toString() : "");
                b.setDailyCharge(rs.getBigDecimal("daily_charge"));
                b.setStatus(rs.getString("status"));
                list.add(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // DISCHARGE PATIENT
    public void dischargePatient(int allocationId, String dischargeDate) {
        String sql = "UPDATE Bed_Allocation SET discharge_date=?, status='Discharged' WHERE allocation_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setDate(1, Date.valueOf(dischargeDate));
            pst.setInt(2, allocationId);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // COUNT OCCUPIED BEDS
    public int getOccupiedBedCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM Bed_Allocation WHERE status='Occupied'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    // TOTAL BED ALLOCATIONS COUNT
    public int getTotalAllocationCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM Bed_Allocation";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }
}
