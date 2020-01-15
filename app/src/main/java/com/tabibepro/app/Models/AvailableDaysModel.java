package com.tabibepro.app.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AvailableDaysModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("doctor_id")
    @Expose
    private String doctorId;
    @SerializedName("weekday")
    @Expose
    private String weekday;
    @SerializedName("status")
    @Expose
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
