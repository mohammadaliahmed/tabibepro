package com.tabibepro.app.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DoctorDataModel {
    @SerializedName("selected_consultation_reasons")
    @Expose
    private List<SelectedConsultationReason> selectedConsultationReasons = null;
    @SerializedName("selected_expertise")
    @Expose
    private List<Object> selectedExpertise = null;
    @SerializedName("all_consultation_reasons")
    @Expose
    private List<AllConsultationReason> allConsultationReasons = null;
    @SerializedName("all_expertise")
    @Expose
    private List<Object> allExpertise = null;
    @SerializedName("all_cities")
    @Expose
    private List<AllCity> allCities = null;
    @SerializedName("all_specialities")
    @Expose
    private List<AllSpeciality> allSpecialities = null;
    @SerializedName("doctor")
    @Expose
    private UserModel doctor = null;

    public UserModel getDoctor() {
        return doctor;
    }

    public void setDoctor(UserModel doctor) {
        this.doctor = doctor;
    }

    public List<SelectedConsultationReason> getSelectedConsultationReasons() {
        return selectedConsultationReasons;
    }

    public void setSelectedConsultationReasons(List<SelectedConsultationReason> selectedConsultationReasons) {
        this.selectedConsultationReasons = selectedConsultationReasons;
    }

    public List<Object> getSelectedExpertise() {
        return selectedExpertise;
    }

    public void setSelectedExpertise(List<Object> selectedExpertise) {
        this.selectedExpertise = selectedExpertise;
    }

    public List<AllConsultationReason> getAllConsultationReasons() {
        return allConsultationReasons;
    }

    public void setAllConsultationReasons(List<AllConsultationReason> allConsultationReasons) {
        this.allConsultationReasons = allConsultationReasons;
    }

    public List<Object> getAllExpertise() {
        return allExpertise;
    }

    public void setAllExpertise(List<Object> allExpertise) {
        this.allExpertise = allExpertise;
    }

    public List<AllCity> getAllCities() {
        return allCities;
    }

    public void setAllCities(List<AllCity> allCities) {
        this.allCities = allCities;
    }

    public List<AllSpeciality> getAllSpecialities() {
        return allSpecialities;
    }

    public void setAllSpecialities(List<AllSpeciality> allSpecialities) {
        this.allSpecialities = allSpecialities;
    }
}
