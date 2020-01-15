package com.tabibe.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignUpUser {

    public int getUser_id() {
        return user_id;
    }

    @SerializedName("user_id")
    @Expose
    int user_id;
}
