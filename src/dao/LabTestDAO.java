package dao;

import db.DBConnection;
import model.LabTest;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class LabTestDAO {

    // ADD LAB TEST
    public void addLabTest(int patientId, int doctorId, String testName, String testDate, BigDecimal cost, String result, String status) {
        String sql = "INSERT INTO Lab_Tests (patient_id, doctor_id, test_name, test_date, cost, result, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, patientId);
            pst.setInt(2, doctorId);
            pst.setString(3, testName);
            pst.setDate(4, Date.valueOf(testDate));
            pst.setBigDecimal(5, cost);
            pst.setString(6, result);
            pst.setString(7, status);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // GET ALL LAB TESTS
    public ArrayList<LabTest> getAllLabTests() {
        ArrayList<LabTest> list = new ArrayList<>();
        String sql = "SELECT * FROM Lab_Tests ORDER BY test_date DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                LabTest test = new LabTest();
                test.setTestId(rs.getInt("test_id"));
                test.setPatientId(rs.getInt("patient_id"));
                test.setDoctorId(rs.getInt("doctor_id"));
                test.setTestName(rs.getString("test_name"));
                test.setTestDate(rs.getDate("test_date") != null ? rs.getDate("test_date").toString() : "");
                test.setCost(rs.getBigDecimal("cost"));
                test.setResult(rs.getString("result"));
                test.setStatus(rs.getString("status"));
                list.add(test);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // GET LAB TESTS BY PATIENT
    public ArrayList<LabTest> getLabTestsByPatient(int patientId) {
        ArrayList<LabTest> list = new ArrayList<>();
        String sql = "SELECT * FROM Lab_Tests WHERE patient_id=? ORDER BY test_date DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, patientId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    LabTest test = new LabTest();
                    test.setTestId(rs.getInt("test_id"));
                    test.setPatientId(rs.getInt("patient_id"));
                    test.setDoctorId(rs.getInt("doctor_id"));
                    test.setTestName(rs.getString("test_name"));
                    test.setTestDate(rs.getDate("test_date") != null ? rs.getDate("test_date").toString() : "");
                    test.setCost(rs.getBigDecimal("cost"));
                    test.setResult(rs.getString("result"));
                    test.setStatus(rs.getString("status"));
                    list.add(test);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // UPDATE TEST RESULT & STATUS
    public void updateTestResult(int testId, String result, String status) {
        String sql = "UPDATE Lab_Tests SET result=?, status=? WHERE test_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, result);
            pst.setString(2, status);
            pst.setInt(3, testId);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DELETE LAB TEST
    public void deleteLabTest(int testId) {
        String sql = "DELETE FROM Lab_Tests WHERE test_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, testId);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // COUNT LAB TESTS
    public int getLabTestCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM Lab_Tests";
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

    // COUNT PENDING TESTS
    public int getPendingTestCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM Lab_Tests WHERE status='Pending'";
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
