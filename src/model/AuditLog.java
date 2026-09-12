package model;

import java.sql.Timestamp;

public class AuditLog {

    private int logId;
    private String actionType;
    private int recordId;
    private Timestamp logTime;
    private String details;

    public AuditLog() {}

    public AuditLog(int logId, String actionType, int recordId, Timestamp logTime, String details) {
        this.logId = logId;
        this.actionType = actionType;
        this.recordId = recordId;
        this.logTime = logTime;
        this.details = details;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public Timestamp getLogTime() {
        return logTime;
    }

    public void setLogTime(Timestamp logTime) {
        this.logTime = logTime;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
