package com.tabibepro.app.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConsultationReasonModel {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("consultation_reason_id")
    @Expose
    private String consultationReasonId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConsultationReasonId() {
        return consultationReasonId;
    }

    public void setConsultationReasonId(String consultationReasonId) {
        this.consultationReasonId = consultationReasonId;
    }


}
