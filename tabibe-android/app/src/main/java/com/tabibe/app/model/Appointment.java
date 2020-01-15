package com.tabibe.app.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Appointment {


    @SerializedName("Appointment_date")
    public String appointmentDate;

    @SerializedName("appointmentTime")
    public List<AppointmentTime> appointmentTimeList;

    public Appointment(String appointmentDate, List<AppointmentTime> appointmentTimeList) {
        this.appointmentDate = appointmentDate;
        this.appointmentTimeList = appointmentTimeList;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public List<AppointmentTime> getAppointmentTimeList() {
        return appointmentTimeList;
    }




}
