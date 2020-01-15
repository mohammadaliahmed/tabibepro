package com.tabibe.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Doctor implements Serializable {

    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("city_id")
    @Expose
    String city_id;

    @SerializedName("speciality_id")
    @Expose
    String speciality_id;

    @SerializedName("first_name")
    @Expose
    String first_name;

    @SerializedName("last_name")
    @Expose
    String last_name;

    @SerializedName("postal_address")
    @Expose
    String postal_address;

    @SerializedName("clinic_address")
    @Expose
    String clinic_address;

    @SerializedName("email_address")
    @Expose
    String email_address;

    @SerializedName("phone_number")
    @Expose
    String phone_number;

    @SerializedName("medical_post")
    @Expose
    String medical_post;

    @SerializedName("latitude")
    @Expose
    String latitude;

    @SerializedName("longitude")
    @Expose
    String longitude;

    @SerializedName("trainings")
    @Expose
    String trainings;

    @SerializedName("associations")
    @Expose
    String associations;

    @SerializedName("works_publications")
    @Expose
    String works_publications;


    @SerializedName("spoken_languages")
    @Expose
    String spoken_languages;

    @SerializedName("experiences")
    @Expose
    String experiences;

    @SerializedName("fee")
    @Expose
    String fee;

    @SerializedName("fee_unit")
    @Expose
    String fee_unit;

    @SerializedName("payment_modes")
    @Expose
    String payment_modes;

    @SerializedName("status")
    @Expose
    String status;

    @SerializedName("speciality_name")
    @Expose
    String speciality_name;

    @SerializedName("city_name")
    @Expose
    String city_name;

    public String getImage_path() {
        return image_path;
    }

    @SerializedName("image_path")
    @Expose
    String image_path;

    public ArrayList<DocExperties> getExperties() {

        return experties;
    }

    @SerializedName("experties")
    @Expose
    ArrayList<DocExperties> experties;

    public String getId() {
        return id;
    }

    public String getCity_id() {
        return city_id;
    }

    public String getSpeciality_id() {
        return speciality_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getPostal_address() {
        return postal_address;
    }

    public String getClinic_address() {
        return clinic_address;
    }

    public String getEmail_address() {
        return email_address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getMedical_post() {
        return medical_post;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getTrainings() {
        return trainings;
    }

    public String getAssociations() {
        return associations;
    }

    public String getWorks_publications() {
        return works_publications;
    }

    public String getSpoken_languages() {
        return spoken_languages;
    }

    public String getExperiences() {
        return experiences;
    }

    public String getFee() {
        return fee;
    }

    public String getFee_unit() {
        return fee_unit;
    }

    public String getPayment_modes() {
        return payment_modes;
    }

    public String getStatus() {
        return status;
    }

    public String getSpeciality_name() {
        return speciality_name;
    }

    public String getCity_name() {
        return city_name;
    }

}
