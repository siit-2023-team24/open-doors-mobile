package com.bookingapptim24.models;

import java.sql.Timestamp;

public class UserReport extends NewUserReport {
    private Long id;
    private Timestamp timestamp;
    private String status;

    public UserReport() {
    }

    public UserReport(Long id, String recipientUsername, String complainantUsername, Timestamp timestamp, boolean isComplainantGuest, String reason, String status) {
        super(recipientUsername, complainantUsername, isComplainantGuest, reason);
        this.id = id;
        this.timestamp = timestamp;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserReport{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", status='" + status + '\'' +
                ", recipientUsername='" + recipientUsername + '\'' +
                ", complainantUsername='" + complainantUsername + '\'' +
                ", isComplainantGuest=" + isComplainantGuest +
                ", reason='" + reason + '\'' +
                '}';
    }
}
