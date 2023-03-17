package com.example.abm_improved.DataClasses;

public class AppointmentType {
    private String uid;
    private String typeName;
    private String price;
    private String duration;

    public AppointmentType(String typeName, String price, String duration, String uid) {
        this.typeName = typeName;
        this.price = price;
        this.duration = duration;
        this.uid = uid;
    }


    public String getTypeName() {
        return typeName;
    }

    public String getPrice() {
        return price;
    }

    public String getDuration() {
        return duration;
    }

    public String getUid() {
        return uid;
    }
}
