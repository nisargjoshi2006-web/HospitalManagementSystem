package dao;

import db.DBConnection;
import model.DoctorSchedule;

import java.sql.*;
import java.util.ArrayList;

public class DoctorScheduleDAO {

    public void addSchedule(
            int doctorId,
            String dayOfWeek,
            String startTime,
            String endTime) {

        String sql =
                "INSERT INTO doctor_schedule " +
                "(doctor_id, day_of_week, start_time, end_time) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, doctorId);
            ps.setString(2, dayOfWeek);
            ps.setTime(3, java.sql.Time.valueOf(startTime));
            ps.setTime(4, java.sql.Time.valueOf(endTime));

            ps.executeUpdate();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<DoctorSchedule> getAllSchedules() {

        ArrayList<DoctorSchedule> list =
                new ArrayList<>();

        String sql = "SELECT * FROM doctor_schedule";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while(rs.next()) {

                DoctorSchedule ds =
                        new DoctorSchedule();

                ds.setScheduleId(
                        rs.getInt("schedule_id"));

                ds.setDoctorId(
                        rs.getInt("doctor_id"));

                ds.setDayOfWeek(
                        rs.getString("day_of_week"));

                ds.setStartTime(
                        rs.getString("start_time"));

                ds.setEndTime(
                        rs.getString("end_time"));

                list.add(ds);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public DoctorSchedule searchSchedule(
            int scheduleId) {

        DoctorSchedule ds = null;

        String sql =
                "SELECT * FROM doctor_schedule WHERE schedule_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, scheduleId);

            try (ResultSet rs = ps.executeQuery()) {

                if(rs.next()) {

                    ds = new DoctorSchedule();

                    ds.setScheduleId(
                            rs.getInt("schedule_id"));

                    ds.setDoctorId(
                            rs.getInt("doctor_id"));

                    ds.setDayOfWeek(
                            rs.getString("day_of_week"));

                    ds.setStartTime(
                            rs.getString("start_time"));

                    ds.setEndTime(
                            rs.getString("end_time"));
                }
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return ds;
    }

    public void updateSchedule(
            int scheduleId,
            int doctorId,
            String day,
            String startTime,
            String endTime) {

        String sql =
                "UPDATE doctor_schedule " +
                "SET doctor_id=?, day_of_week=?, start_time=?, end_time=? " +
                "WHERE schedule_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, doctorId);
            ps.setString(2, day);
            ps.setTime(3, java.sql.Time.valueOf(startTime));
            ps.setTime(4, java.sql.Time.valueOf(endTime));
            ps.setInt(5, scheduleId);

            ps.executeUpdate();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteSchedule(
            int scheduleId) {

        String sql =
                "DELETE FROM doctor_schedule WHERE schedule_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, scheduleId);

            ps.executeUpdate();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public int getScheduleCount() {

    int count = 0;

    String sql =
            "SELECT COUNT(*) FROM doctor_schedule";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        if(rs.next()) {

            count = rs.getInt(1);
        }

    } catch(Exception e) {

        e.printStackTrace();
    }

    return count;
}
}