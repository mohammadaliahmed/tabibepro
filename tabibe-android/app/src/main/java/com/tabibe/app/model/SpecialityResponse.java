package com.tabibe.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SpecialityResponse extends BaseResponse{


    @SerializedName("data")
    @Expose
    ArrayList<Speciality> data;

    public ArrayList<Speciality> getData() {
        return data;
    }

}
