package com.tabibe.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UResponse {

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



    public String getId() {
        return id;
    }

    @SerializedName("email")
    @Expose
    String email;

    @SerializedName("parients")
    @Expose
    ArrayList<Patient> patients;

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getImage_path() {
        return image_path;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<Patient> getPatients() {
        return patients;
    }



    public String getPhone() {
        return phone;
    }


}
