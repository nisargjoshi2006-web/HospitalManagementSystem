package dao;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import db.DBConnection;
import model.Doctor;

public class DoctorDAO {

    // ADD DOCTOR
    public void addDoctor(String name,
                          int specializationId,
                          String qualification,
                          double fee,
                          String contact) {
        String sql = "INSERT INTO Doctor(doctor_name,specialization_id,qualification,consultation_fee,contact) VALUES(?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, specializationId);
            ps.setString(3, qualification);
            ps.setDouble(4, fee);
            ps.setString(5, contact);
            int rows = ps.executeUpdate();
            System.out.println("Rows Inserted = " + rows);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    // VIEW ALL DOCTORS
    public ArrayList<Doctor> getAllDoctors() {
        ArrayList<Doctor> doctorList = new ArrayList<>();
        String sql = "SELECT d.*, s.specialization_name FROM Doctor d LEFT JOIN Specializations s ON d.specialization_id = s.specialization_id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while(rs.next()) {
                Doctor d = new Doctor();
                d.setDoctorId(rs.getInt("doctor_id"));
                d.setDoctorName(rs.getString("doctor_name"));
                d.setSpecializationId(rs.getInt("specialization_id"));
                d.setSpecializationName(rs.getString("specialization_name"));
                d.setQualification(rs.getString("qualification"));
                d.setConsultationFee(rs.getDouble("consultation_fee"));
                d.setContact(rs.getString("contact"));
                doctorList.add(d);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return doctorList;
    }

    // UPDATE DOCTOR
    public void updateDoctor(int id,
                             String name,
                             int specializationId,
                             String qualification,
                             double fee,
                             String contact) {
        String sql = "UPDATE Doctor SET doctor_name=?, specialization_id=?, qualification=?, consultation_fee=?, contact=? WHERE doctor_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, specializationId);
            ps.setString(3, qualification);
            ps.setDouble(4, fee);
            ps.setString(5, contact);
            ps.setInt(6, id);
            int rows = ps.executeUpdate();
            System.out.println("Rows Updated = " + rows);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    // DELETE DOCTOR
    public void deleteDoctor(int id) {
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);
            String sql0 = "DELETE FROM prescriptions WHERE appointment_id IN (SELECT appointment_id FROM appointments WHERE doctor_id=?)";
            try (PreparedStatement ps0 = con.prepareStatement(sql0)) {
                ps0.setInt(1, id);
                ps0.executeUpdate();
            }

            String sql1 = "DELETE FROM billing WHERE appointment_id IN (SELECT appointment_id FROM appointments WHERE doctor_id=?)";
            try (PreparedStatement ps1 = con.prepareStatement(sql1)) {
                ps1.setInt(1, id);
                ps1.executeUpdate();
            }

            String sql2 = "DELETE FROM appointments WHERE doctor_id=?";
            try (PreparedStatement ps2 = con.prepareStatement(sql2)) {
                ps2.setInt(1, id);
                ps2.executeUpdate();
            }

            String sql3 = "DELETE FROM doctor_schedule WHERE doctor_id=?";
            try (PreparedStatement ps3 = con.prepareStatement(sql3)) {
                ps3.setInt(1, id);
                ps3.executeUpdate();
            }

            String sqlEmergency = "UPDATE Emergency SET assigned_doctor=NULL WHERE assigned_doctor=?";
            try (PreparedStatement psEmergency = con.prepareStatement(sqlEmergency)) {
                psEmergency.setInt(1, id);
                psEmergency.executeUpdate();
            }

            String sql4 = "DELETE FROM Doctor WHERE doctor_id=?";
            try (PreparedStatement ps4 = con.prepareStatement(sql4)) {
                ps4.setInt(1, id);
                int rows = ps4.executeUpdate();
                System.out.println("Rows Deleted = " + rows);
            }
            con.commit();
        } catch(Exception e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    // SEARCH DOCTOR
    public Doctor searchDoctor(int id) {
        Doctor d = null;
        String sql = "SELECT d.*, s.specialization_name FROM Doctor d LEFT JOIN Specializations s ON d.specialization_id = s.specialization_id WHERE d.doctor_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    d = new Doctor();
                    d.setDoctorId(rs.getInt("doctor_id"));
                    d.setDoctorName(rs.getString("doctor_name"));
                    d.setSpecializationId(rs.getInt("specialization_id"));
                    d.setSpecializationName(rs.getString("specialization_name"));
                    d.setQualification(rs.getString("qualification"));
                    d.setConsultationFee(rs.getDouble("consultation_fee"));
                    d.setContact(rs.getString("contact"));
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    public int getDoctorCount() {
        int count = 0;
        String query = "SELECT COUNT(*) FROM Doctor";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            if(rs.next()) {
                count = rs.getInt(1);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public boolean doctorExists(int doctorId) {
        boolean exists = false;
        String sql = "SELECT * FROM Doctor WHERE doctor_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    exists = true;
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return exists;
    }

    // SEARCH DOCTORS BY KEYWORD (name or specialization)
    public ArrayList<Doctor> searchDoctorsByKeyword(String keyword) {
        ArrayList<Doctor> list = new ArrayList<>();
        String sql = "SELECT d.*, s.specialization_name FROM Doctor d LEFT JOIN Specializations s ON d.specialization_id = s.specialization_id WHERE d.doctor_name LIKE ? OR s.specialization_name LIKE ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            String pattern = "%" + keyword + "%";
            pst.setString(1, pattern);
            pst.setString(2, pattern);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Doctor d = new Doctor();
                    d.setDoctorId(rs.getInt("doctor_id"));
                    d.setDoctorName(rs.getString("doctor_name"));
                    d.setSpecializationId(rs.getInt("specialization_id"));
                    d.setSpecializationName(rs.getString("specialization_name"));
                    d.setQualification(rs.getString("qualification"));
                    d.setConsultationFee(rs.getDouble("consultation_fee"));
                    d.setContact(rs.getString("contact"));
                    list.add(d);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
