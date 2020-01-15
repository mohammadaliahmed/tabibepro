package com.tabibepro.app.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("city_id")
    @Expose
    private String cityId;
    @SerializedName("speciality_id")
    @Expose
    private String specialityId;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("postal_address")
    @Expose
    private String postalAddress;
    @SerializedName("clinic_address")
    @Expose
    private String clinicAddress;
    @SerializedName("email_address")
    @Expose
    private String emailAddress;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("medical_post")
    @Expose
    private String medicalPost;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("trainings")
    @Expose
    private String trainings;
    @SerializedName("associations")
    @Expose
    private String associations;
    @SerializedName("works_publications")
    @Expose
    private String worksPublications;
    @SerializedName("spoken_languages")
    @Expose
    private String spokenLanguages;
    @SerializedName("experiences")
    @Expose
    private String experiences;
    @SerializedName("fee")
    @Expose
    private String fee;
    @SerializedName("fee_unit")
    @Expose
    private String feeUnit;
    @SerializedName("payment_modes")
    @Expose
    private String paymentModes;
    @SerializedName("image_path")
    @Expose
    private String imagePath;
    @SerializedName("created_datetime")
    @Expose
    private String createdDatetime;
    @SerializedName("updated_datetime")
    @Expose
    private String updatedDatetime;
    @SerializedName("status")
    @Expose
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getSpecialityId() {
        return specialityId;
    }

    public void setSpecialityId(String specialityId) {
        this.specialityId = specialityId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public String getClinicAddress() {
        return clinicAddress;
    }

    public void setClinicAddress(String clinicAddress) {
        this.clinicAddress = clinicAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMedicalPost() {
        return medicalPost;
    }

    public void setMedicalPost(String medicalPost) {
        this.medicalPost = medicalPost;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getTrainings() {
        return trainings;
    }

    public void setTrainings(String trainings) {
        this.trainings = trainings;
    }

    public String getAssociations() {
        return associations;
    }

    public void setAssociations(String associations) {
        this.associations = associations;
    }

    public String getWorksPublications() {
        return worksPublications;
    }

    public void setWorksPublications(String worksPublications) {
        this.worksPublications = worksPublications;
    }

    public String getSpokenLanguages() {
        return spokenLanguages;
    }

    public void setSpokenLanguages(String spokenLanguages) {
        this.spokenLanguages = spokenLanguages;
    }

    public String getExperiences() {
        return experiences;
    }

    public void setExperiences(String experiences) {
        this.experiences = experiences;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getFeeUnit() {
        return feeUnit;
    }

    public void setFeeUnit(String feeUnit) {
        this.feeUnit = feeUnit;
    }

    public String getPaymentModes() {
        return paymentModes;
    }

    public void setPaymentModes(String paymentModes) {
        this.paymentModes = paymentModes;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getCreatedDatetime() {
        return createdDatetime;
    }

    public void setCreatedDatetime(String createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    public String getUpdatedDatetime() {
        return updatedDatetime;
    }

    public void setUpdatedDatetime(String updatedDatetime) {
        this.updatedDatetime = updatedDatetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
