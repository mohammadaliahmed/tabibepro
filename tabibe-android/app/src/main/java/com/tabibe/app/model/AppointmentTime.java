package com.tabibe.app.model;

import com.google.gson.annotations.SerializedName;

public class AppointmentTime {

    @SerializedName("appintmenTime")
    public String appointmentTime;

    public AppointmentTime(String appointmentTime){
        this.appointmentTime = appointmentTime;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }
}
