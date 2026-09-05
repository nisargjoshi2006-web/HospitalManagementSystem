package dao;

import db.DBConnection;
import model.Emergency;

import java.sql.*;
import java.util.ArrayList;

public class EmergencyDAO {

    // ADD
    public void addEmergency(
            int patientId,
            String emergencyType,
            String priorityLevel,
            String status,
            int assignedDoctor,
            String arrivalDate,
            String arrivalTime) {

        try {

            Connection con =
                    DBConnection.getConnection();

            String sql =
                    "INSERT INTO emergency " +
                    "(patient_id, emergency_type, priority_level, status, assigned_doctor, arrival_date, arrival_time) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, patientId);
            ps.setString(2, emergencyType);
            ps.setString(3, priorityLevel);
            ps.setString(4, status);
            ps.setInt(5, assignedDoctor);
            ps.setDate(6, java.sql.Date.valueOf(arrivalDate));
            ps.setTime(7, java.sql.Time.valueOf(arrivalTime));

            ps.executeUpdate();

            con.close();

        } catch(Exception e) {

            e.printStackTrace();
        }
    }

    // VIEW ALL
    public ArrayList<Emergency> getAllEmergencies() {

        ArrayList<Emergency> list =
                new ArrayList<>();

        try {

            Connection con =
                    DBConnection.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(
                            "SELECT * FROM emergency");

            ResultSet rs =
                    ps.executeQuery();

            while(rs.next()) {

                Emergency e =
                        new Emergency();

                e.setEmergencyId(
                        rs.getInt("emergency_id"));

                e.setPatientId(
                        rs.getInt("patient_id"));

                e.setEmergencyType(
                        rs.getString("emergency_type"));

                e.setPriorityLevel(
                        rs.getString("priority_level"));

                e.setStatus(
                        rs.getString("status"));

                e.setAssignedDoctor(
                        rs.getInt("assigned_doctor"));

                e.setArrivalDate(
                        rs.getString("arrival_date"));

                e.setArrivalTime(
                        rs.getString("arrival_time"));

                list.add(e);
            }

            con.close();

        } catch(Exception e) {

            e.printStackTrace();
        }

        return list;
    }

    // SEARCH
    public Emergency searchEmergency(
            int emergencyId) {

        Emergency e = null;

        try {

            Connection con =
                    DBConnection.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(
                            "SELECT * FROM emergency WHERE emergency_id=?");

            ps.setInt(1, emergencyId);

            ResultSet rs =
                    ps.executeQuery();

            if(rs.next()) {

                e = new Emergency();

                e.setEmergencyId(
                        rs.getInt("emergency_id"));

                e.setPatientId(
                        rs.getInt("patient_id"));

                e.setEmergencyType(
                        rs.getString("emergency_type"));

                e.setPriorityLevel(
                        rs.getString("priority_level"));

                e.setStatus(
                        rs.getString("status"));

                e.setAssignedDoctor(
                        rs.getInt("assigned_doctor"));

                e.setArrivalDate(
                        rs.getString("arrival_date"));

                e.setArrivalTime(
                        rs.getString("arrival_time"));
            }

            con.close();

        } catch(Exception ex) {

            ex.printStackTrace();
        }

        return e;
    }

    // UPDATE
    public void updateEmergency(
            int emergencyId,
            int patientId,
            String emergencyType,
            String priorityLevel,
            String status,
            int assignedDoctor,
            String arrivalDate,
            String arrivalTime) {

        try {

            Connection con =
                    DBConnection.getConnection();

            String sql =
                    "UPDATE emergency SET " +
                    "patient_id=?, " +
                    "emergency_type=?, " +
                    "priority_level=?, " +
                    "status=?, " +
                    "assigned_doctor=?, " +
                    "arrival_date=?, " +
                    "arrival_time=? " +
                    "WHERE emergency_id=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, patientId);
            ps.setString(2, emergencyType);
            ps.setString(3, priorityLevel);
            ps.setString(4, status);
            ps.setInt(5, assignedDoctor);
            ps.setDate(6, java.sql.Date.valueOf(arrivalDate));
            ps.setTime(7, java.sql.Time.valueOf(arrivalTime));
            ps.setInt(8, emergencyId);

            ps.executeUpdate();

            con.close();

        } catch(Exception e) {

            e.printStackTrace();
        }
    }

    // DELETE
    public void deleteEmergency(
            int emergencyId) {

        try {

            Connection con =
                    DBConnection.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(
                            "DELETE FROM emergency WHERE emergency_id=?");

            ps.setInt(1, emergencyId);

            ps.executeUpdate();

            con.close();

        } catch(Exception e) {

            e.printStackTrace();
        }
    }
    public int getEmergencyCount() {

    int count = 0;

    try {
        Connection con =
                DBConnection.getConnection();

        PreparedStatement ps =
                con.prepareStatement(
                        "SELECT COUNT(*) FROM emergency"
                );

        ResultSet rs =
                ps.executeQuery();

        if(rs.next()) {
            count = rs.getInt(1);
        }

        con.close();

    } catch(Exception e) {
        e.printStackTrace();
    }

    return count;
}
}