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

        try {
            Connection con = DBConnection.getConnection();

            String sql =
                    "INSERT INTO doctor_schedule " +
                    "(doctor_id, day_of_week, start_time, end_time) " +
                    "VALUES (?, ?, ?, ?)";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, doctorId);
            ps.setString(2, dayOfWeek);
            ps.setString(3, startTime);
            ps.setString(4, endTime);

            ps.executeUpdate();

            con.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<DoctorSchedule> getAllSchedules() {

        ArrayList<DoctorSchedule> list =
                new ArrayList<>();

        try {

            Connection con =
                    DBConnection.getConnection();

            String sql =
                    "SELECT * FROM doctor_schedule";

            PreparedStatement ps =
        con.prepareStatement(
                "SELECT * FROM doctor_schedule"
        );

ResultSet rs = ps.executeQuery();

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

            con.close();

        } catch(Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public DoctorSchedule searchSchedule(
            int scheduleId) {

        DoctorSchedule ds = null;

        try {

            Connection con =
                    DBConnection.getConnection();

            String sql =
                    "SELECT * FROM doctor_schedule WHERE schedule_id=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, scheduleId);

            ResultSet rs =
                    ps.executeQuery();

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

            con.close();

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

        try {

            Connection con =
                    DBConnection.getConnection();

            String sql =
                    "UPDATE doctor_schedule " +
                    "SET doctor_id=?, day_of_week=?, start_time=?, end_time=? " +
                    "WHERE schedule_id=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, doctorId);
            ps.setString(2, day);
            ps.setString(3, startTime);
            ps.setString(4, endTime);
            ps.setInt(5, scheduleId);

            ps.executeUpdate();

            con.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteSchedule(
            int scheduleId) {

        try {

            Connection con =
                    DBConnection.getConnection();

            String sql =
                    "DELETE FROM doctor_schedule WHERE schedule_id=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, scheduleId);

            ps.executeUpdate();

            con.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}