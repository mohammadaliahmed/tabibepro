package com.tabibe.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ConsultationResponse extends BaseResponse {

    public ArrayList<ConsultationChoice> getConsultations() {
        return consultations;
    }

    @SerializedName("data")
    @Expose
    ArrayList<ConsultationChoice> consultations;
}
