package com.tabibe.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MyAppointmentResponse extends BaseResponse {

    public ArrayList<MyAppointment> getAppointments() {
        return appointments;
    }

    @SerializedName("data")
    @Expose
    ArrayList<MyAppointment> appointments;
}
