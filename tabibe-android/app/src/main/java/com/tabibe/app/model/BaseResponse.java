package com.tabibe.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseResponse  {

    @SerializedName("statusCode")
    @Expose
    int statusCode;

    @SerializedName("status")
    @Expose
    int status;

    @SerializedName("message")
    @Expose
    String message;

    public int getStatusCode() {
        return statusCode;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public void setStatus(int status){
        this.status = status;
    }

    public void setMessage(String message){this.message=message;}

}
