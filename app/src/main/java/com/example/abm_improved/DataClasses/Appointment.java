package com.example.abm_improved.DataClasses;

import com.example.abm_improved.Utils.DatabaseUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class Appointment {
    private String uid;
    private String clientUid;
    private String appointmentTypeUid;
    private String date;
    private String time;
    private Calendar startTime;
    private Calendar endTime;

    public Appointment(String uid, String clientUid, String appointmentTypeUid, String date, String time) {
        this.uid = uid;
        this.clientUid = clientUid;
        this.appointmentTypeUid = appointmentTypeUid;
        this.date = date;
        this.time = time;
        this.startTime = null;
        this.endTime = null;
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

    public Calendar getStartTime() {
        return startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public long getUidAsLong() {
        long mostSignificantBits = UUID.fromString(this.uid).getMostSignificantBits();
        long leastSignificantBits = UUID.fromString(this.uid).getLeastSignificantBits();

        // Concatenate the most significant bits and least significant bits using a bitwise OR operation
        return mostSignificantBits | leastSignificantBits;
    }

    public void setStartEndTime() {
        startTime = Calendar.getInstance();
        startTime.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
        startTime.set(Calendar.MONTH, Integer.parseInt(date.substring(4, 6)) - 1);
        startTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date.substring(6, 8)));
        startTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.substring(0, 2)));
        startTime.set(Calendar.MINUTE, Integer.parseInt(time.substring(2, 4)));
        startTime.set(Calendar.SECOND, 0);
        startTime.set(Calendar.MILLISECOND, 0);

        endTime = (Calendar) startTime.clone();
        ArrayList<AppointmentType> appointmentTypes = DatabaseUtils.getAppointmentTypes();
        for (AppointmentType appointmentType : appointmentTypes) {
            if (appointmentType.getUid().equals(appointmentTypeUid)) {
                endTime.add(Calendar.MINUTE, Integer.parseInt(appointmentType.getDuration()));
                break;
            }
        }
    }
}
