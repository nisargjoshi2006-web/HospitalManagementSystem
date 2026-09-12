package dao;

import db.DBConnection;
import model.AuditLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AuditLogDAO {

    // LOG AN ACTION
    public void logAction(String actionType, int recordId, String details) {
        String sql = "INSERT INTO Audit_Logs (action_type, record_id, details) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, actionType);
            pst.setInt(2, recordId);
            pst.setString(3, details);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // GET RECENT AUDIT LOGS
    public ArrayList<AuditLog> getRecentLogs(int limit) {
        ArrayList<AuditLog> list = new ArrayList<>();
        String sql = "SELECT * FROM Audit_Logs ORDER BY log_time DESC LIMIT ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, limit);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    AuditLog log = new AuditLog();
                    log.setLogId(rs.getInt("log_id"));
                    log.setActionType(rs.getString("action_type"));
                    log.setRecordId(rs.getInt("record_id"));
                    log.setLogTime(rs.getTimestamp("log_time"));
                    log.setDetails(rs.getString("details"));
                    list.add(log);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
