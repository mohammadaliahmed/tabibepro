package com.tabibe.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DocExperties implements Serializable {

    @SerializedName("specialities_experty_id")
    @Expose
    String id;

    public String getId() {
        return id;
    }

    public String getExpertiesName() {
        return expertiesName;
    }

    @SerializedName("specialities_experty_name")
    @Expose
    String expertiesName;


}
