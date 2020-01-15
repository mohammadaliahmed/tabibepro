package com.tabibe.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CityResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    ArrayList<City> data;

    public ArrayList<City> getData() {
        return data;
    }

}
