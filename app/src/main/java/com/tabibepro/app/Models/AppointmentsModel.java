package com.tabibepro.app.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppointmentsModel {
    @SerializedName("user_name")
    @Expose
    private Object userName;
    @SerializedName("user_email")
    @Expose
    private Object userEmail;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("patients_id")
    @Expose
    private String patientsId;
    @SerializedName("consultation_reason_name")
    @Expose
    private String consultationReasonName;
    @SerializedName("patient_name")
    @Expose
    private String patientName;
    @SerializedName("doctor_name")
    @Expose
    private String doctorName;
    @SerializedName("doctor_id")
    @Expose
    private String doctorId;
    @SerializedName("appointment_date")
    @Expose
    private String appointmentDate;
    @SerializedName("appointment_id")
    @Expose
    private String appointmentId;
    @SerializedName("doctor_slot")
    @Expose
    private String doctorSlot;

    public Object getUserName() {
        return userName;
    }

    public void setUserName(Object userName) {
        this.userName = userName;
    }

    public Object getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(Object userEmail) {
        this.userEmail = userEmail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPatientsId() {
        return patientsId;
    }

    public void setPatientsId(String patientsId) {
        this.patientsId = patientsId;
    }

    public String getConsultationReasonName() {
        return consultationReasonName;
    }

    public void setConsultationReasonName(String consultationReasonName) {
        this.consultationReasonName = consultationReasonName;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getDoctorSlot() {
        return doctorSlot;
    }

    public void setDoctorSlot(String doctorSlot) {
        this.doctorSlot = doctorSlot;
    }
}
