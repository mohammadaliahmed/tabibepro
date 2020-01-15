package com.tabibepro.app.NetworkResponses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tabibepro.app.Models.AvailableDaysModel;
import com.tabibepro.app.Models.UserModel;

import java.util.List;

public class AvailableDaysResponse {


    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<AvailableDaysModel> data = null;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<AvailableDaysModel> getData() {
        return data;
    }

    public void setData(List<AvailableDaysModel> data) {
        this.data = data;
    }
}
