package com.tabibe.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignUpResponse extends BaseResponse {


    public SignUpUser getUser() {
        return user;
    }

    @SerializedName("data")
    @Expose
    SignUpUser user;
}
