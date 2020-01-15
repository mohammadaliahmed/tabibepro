package com.tabibe.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DoctorSlotsDetail {

    String appointment_date;

    @SerializedName("start_time")
    @Expose
    String startTime;

    @SerializedName("end_time")
    @Expose
    String end_time;

    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("is_free")
    @Expose
    String is_free;

    public String getAppointment_date() {
        return appointment_date;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getId() {
        return id;
    }

    public String getIs_free() {
        return is_free;
    }

    public void setAppointmentDate(String date){
        this.appointment_date = date;
    }

}
