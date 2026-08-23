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

        } catch (Exception e) {
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

            Statement st =
                    con.createStatement();

            ResultSet rs =
                    st.executeQuery(sql);

            while (rs.next()) {

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

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}