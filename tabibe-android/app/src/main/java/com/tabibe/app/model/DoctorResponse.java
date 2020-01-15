package com.tabibe.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DoctorResponse extends BaseResponse{

    public ArrayList<Doctor> getDoctors() {
        return doctors;
    }

    @SerializedName("data")
    @Expose
    ArrayList<Doctor> doctors;
}
