package com.example.abm_improved.DataClasses;

public class Appointment {
    private String uid;
    private String clientUid;
    private String appointmentTypeUid;
    private String date;
    private String time;

    public Appointment(String uid, String clientUid, String appointmentTypeUid, String date, String time) {
        this.uid = uid;
        this.clientUid = clientUid;
        this.appointmentTypeUid = appointmentTypeUid;
        this.date = date;
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public String getClientUid() {
        return clientUid;
    }

    public String getAppointmentTypeUid() {
        return appointmentTypeUid;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
