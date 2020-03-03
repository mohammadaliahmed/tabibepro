
package com.tabibepro.app.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SelectedConsultationReason {

    @SerializedName("doctor_id")
    @Expose
    private String doctorId;
    @SerializedName("consultation_reason_id")
    @Expose
    private String consultationReasonId;

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getConsultationReasonId() {
        return consultationReasonId;
    }

    public void setConsultationReasonId(String consultationReasonId) {
        this.consultationReasonId = consultationReasonId;
    }

}
