package com.tabibe.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseRequest {

    @SerializedName("api_username")
    @Expose
    String api_username;

    @SerializedName("api_password")
    @Expose
    String api_password;
}
