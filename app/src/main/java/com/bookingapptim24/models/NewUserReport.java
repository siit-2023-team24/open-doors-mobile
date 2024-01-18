package com.bookingapptim24.models;

public class NewUserReport {
    protected String recipientUsername;

    protected String complainantUsername;

    protected boolean isComplainantGuest;

    protected String reason;

    public NewUserReport() {
    }

    public NewUserReport(String recipientUsername, String complainantUsername, boolean isComplainantGuest, String reason) {
        this.recipientUsername = recipientUsername;
        this.complainantUsername = complainantUsername;
        this.isComplainantGuest = isComplainantGuest;
        this.reason = reason;
    }

    public String getRecipientUsername() {
        return recipientUsername;
    }

    public void setRecipientUsername(String recipientUsername) {
        this.recipientUsername = recipientUsername;
    }

    public String getComplainantUsername() {
        return complainantUsername;
    }

    public void setComplainantUsername(String complainantUsername) {
        this.complainantUsername = complainantUsername;
    }

    public boolean isComplainantGuest() {
        return isComplainantGuest;
    }

    public void setComplainantGuest(boolean complainantGuest) {
        isComplainantGuest = complainantGuest;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "NewUserReport{" +
                "recipientUsername='" + recipientUsername + '\'' +
                ", complainantUsername='" + complainantUsername + '\'' +
                ", isComplainantGuest=" + isComplainantGuest +
                ", reason='" + reason + '\'' +
                '}';
    }
}
