package com.tabibe.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DoctorSlotsResponse extends BaseResponse {

    public ArrayList<DoctorSlots> getSlots() {
        return slots;
    }

    @SerializedName("data")
    @Expose
    ArrayList<DoctorSlots> slots;
}
