package com.tabibe.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Patient {

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

    @SerializedName("phone")
    @Expose
    String phone;

    @SerializedName("parent_id")
    @Expose
    String parentId;

    @SerializedName("is_parent")
    @Expose
    String is_parent;

    public String getIs_parent() {
        return is_parent;
    }


    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getParentId() {
        return parentId;
    }

    public String getPhone() {
        return phone;
    }
}
