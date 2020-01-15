package com.tabibe.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DoctorSlots  {

    @SerializedName("selected_date")
    @Expose
    String selected_date;


    @SerializedName("slots")
    @Expose
    ArrayList<DoctorSlotsDetail> slotsDetails;

    public String getSelected_date() {
        return selected_date;
    }

    public ArrayList<DoctorSlotsDetail> getSlotsDetails() {
        return slotsDetails;
    }
}
