package com.tabibe.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserResponse extends BaseResponse {

    public ArrayList<UResponse> getuResponses() {
        return uResponses;
    }

    @SerializedName("data")
    @Expose
    ArrayList<UResponse> uResponses;
}
