package com.tabibe.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginUser {

    public String getId() {
        return id;
    }

    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("first_name")
    @Expose
    String first_name;

    @SerializedName("last_name")
    @Expose
    String last_name;

    @SerializedName("image_path")
    @Expose
    String image_path;

    @SerializedName("phone")
    @Expose
    String phone;

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getImage_path() {
        return image_path;
    }

    public String getPhone() {
        return phone;
    }
}
