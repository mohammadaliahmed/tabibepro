package com.tabibe.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LoginResponse extends BaseResponse {

    public ArrayList<LoginUser> getLoginUser() {
        return loginUser;
    }

    @SerializedName("data")
    @Expose
    ArrayList<LoginUser> loginUser;


}
