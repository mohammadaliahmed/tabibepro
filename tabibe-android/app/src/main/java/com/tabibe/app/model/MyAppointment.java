package com.tabibe.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyAppointment {


    public String getId() {
        return id;
    }

    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("appointment_date")
    @Expose
    String appointmentDate;

    @SerializedName("doctor_first_name")
    @Expose
    String doctor_first_name;

    @SerializedName("doctor_last_name")
    @Expose
    String doctor_last_name;

    @SerializedName("patient_first_name")
    @Expose
    String patient_first_name;

    @SerializedName("patient_last_name")
    @Expose
    String patient_last_name;

    @SerializedName("consultation_reasons_name")
    @Expose
    String consultation_reasons_name;

    @SerializedName("slot_start_time")
    @Expose
    String slot_start_time;

    @SerializedName("slot_end_time")
    @Expose
    String slot_end_time;

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public String getDoctor_first_name() {
        return doctor_first_name;
    }

    public String getDoctor_last_name() {
        return doctor_last_name;
    }

    public String getPatient_first_name() {
        return patient_first_name;
    }

    public String getPatient_last_name() {
        return patient_last_name;
    }

    public String getConsultation_reasons_name() {
        return consultation_reasons_name;
    }

    public String getSlot_start_time() {
        return slot_start_time;
    }

    public String getSlot_end_time() {
        return slot_end_time;
    }


}
