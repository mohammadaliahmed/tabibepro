package com.tabibepro.app.NetworkManager;


import com.tabibepro.app.NetworkResponses.AppointmentsResponse;
import com.tabibepro.app.NetworkResponses.AvailableDaysResponse;
import com.tabibepro.app.NetworkResponses.ConsultationReasonResponse;
import com.tabibepro.app.NetworkResponses.DoctorDaysResponse;
import com.tabibepro.app.NetworkResponses.DoctorProfileResponse;
import com.tabibepro.app.NetworkResponses.LoginResponse;
import com.tabibepro.app.NetworkResponses.PatientsListResponse;
import com.tabibepro.app.NetworkResponses.PrescriptionResponse;
import com.tabibepro.app.NetworkResponses.ScheduleListResponse;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UserClient {


    @POST("doctor_api/login_doctor")
    @FormUrlEncoded
    Call<LoginResponse> loginUser(
            @Field("api_username") String apiUsername,
            @Field("api_password") String apiPassword,
            @Field("doctor_email") String email,
            @Field("doctor_password") String password

    );

    @POST("doctor_api/signup_doctor")
    @FormUrlEncoded
    Call<LoginResponse> registerUser(
            @Field("api_username") String apiUsername,
            @Field("api_password") String apiPassword,
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("gender") String gender,
            @Field("doctor_email") String email,
            @Field("doctor_password") String password


    );

    @POST("doctor_api/doc_available_days")
    @FormUrlEncoded
    Call<AvailableDaysResponse> doc_available_days(
            @Field("api_username") String apiUsername,
            @Field("api_password") String apiPassword,
            @Field("doctor_id") String first_name
    );

    @POST("doctor_api/doctor_appointment_by_date")
    @FormUrlEncoded
    Call<AppointmentsResponse> doctor_appointment_by_date(
            @Field("api_username") String apiUsername,
            @Field("api_password") String apiPassword,
            @Field("doctor_id") String first_name,
            @Field("date") String date
    );

    @POST("doctor_api/delete_appointment")
    @FormUrlEncoded
    Call<ResponseBody> delete_appointment(
            @Field("api_username") String apiUsername,
            @Field("api_password") String apiPassword,
            @Field("appointment_id") String appointment_id
    );

    @POST("doctor_api/consultation_reason")
    @FormUrlEncoded
    Call<ConsultationReasonResponse> consultation_reason(
            @Field("api_username") String apiUsername,
            @Field("api_password") String apiPassword,
            @Field("doctor_id") String first_name
    );

    @POST("doctor_api/doctor_slots_listing")
    @FormUrlEncoded
    Call<ScheduleListResponse> doctor_slots_listing(
            @Field("api_username") String apiUsername,
            @Field("api_password") String apiPassword,
            @Field("doctor_id") String first_name
    );

    @POST("doctor_api/doctor_date_shots")
    @FormUrlEncoded
    Call<ScheduleListResponse> doctor_date_shots(
            @Field("api_username") String apiUsername,
            @Field("api_password") String apiPassword,
            @Field("doctor_id") String id,
            @Field("date") String date
    );

    @POST("doctor_api/delete_doctor_slot")
    @FormUrlEncoded
    Call<ScheduleListResponse> delete_doctor_slot(
            @Field("api_username") String apiUsername,
            @Field("api_password") String apiPassword,
            @Field("slot_id") String slot_id
    );

    @POST("doctor_api/doctor_days")
    @FormUrlEncoded
    Call<DoctorDaysResponse> doctor_days(
            @Field("api_username") String apiUsername,
            @Field("api_password") String apiPassword,
            @Field("doctor_id") String doctor_id
    );

    @POST("doctor_api/get_prescriotion_by_appointment")
    @FormUrlEncoded
    Call<PrescriptionResponse> get_prescriotion_by_appointment(
            @Field("api_username") String apiUsername,
            @Field("api_password") String apiPassword,
            @Field("appointment_id") String doctor_id
    );

    @POST("doctor_api/add_prescription")
    @FormUrlEncoded
    Call<PrescriptionResponse> add_prescription(
            @Field("api_username") String apiUsername,
            @Field("api_password") String apiPassword,
            @Field("appointment_id") String doctor_id,
            @Field("description") String description,
            @Field("image_blob") String image_blob
    );

    @POST("doctor_api/remove_prescription")
    @FormUrlEncoded
    Call<PrescriptionResponse> remove_prescription(
            @Field("api_username") String apiUsername,
            @Field("api_password") String apiPassword,
            @Field("prescription_id") String prescription_id
    );

    @POST("doctor_api/get_doctor_update_profile_data")
    @FormUrlEncoded
    Call<DoctorProfileResponse> get_doctor_update_profile_data(
            @Field("api_username") String apiUsername,
            @Field("api_password") String apiPassword,
            @Field("doctor_id") String doctor_id
    );

    @POST("doctor_api/update_doc_profile")
    @FormUrlEncoded
    Call<DoctorProfileResponse> update_doc_profile(
            @Field("api_username") String apiUsername,
            @Field("api_password") String apiPassword,
            @Field("doctor_id") String doctor_id,
            @Field("consultation_id") String consultation_id,
            @Field("city_id") String city_id,
            @Field("speciality_id") String speciality_id,
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("email_address") String email_address,
            @Field("password") String password,
            @Field("postal_address") String postal_address,
            @Field("clinic_address") String clinic_address,
            @Field("phone_number") String phone_number,
            @Field("gender") String gender,
            @Field("medical_post") String medical_post,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("trainings") String trainings,
            @Field("associations") String associations,
            @Field("works_publications") String works_publications,
            @Field("spoken_languages") String spoken_languages,
            @Field("experiences") String experiences,
            @Field("fee") String fee,
            @Field("fee_unit") String fee_unit,
            @Field("payment_modes") String payment_modes,
            @Field("image_blob") String image_blob,
            @Field("expertise_id")
                    String expertise_id
    );

    @POST("doctor_api/patient_listing")
    @FormUrlEncoded
    Call<PatientsListResponse> patient_listing(
            @Field("api_username") String apiUsername,
            @Field("api_password") String apiPassword
    );

    @POST("doctor_api/add_appointment")
    @FormUrlEncoded
    Call<PatientsListResponse> add_appointment(
            @Field("api_username") String apiUsername,
            @Field("api_password") String apiPassword,
            @Field("description") String description,
            @Field("doctor_id") String doctor_id,
            @Field("patient_id") String patient_id,
            @Field("consultation_id") String consultation_id,
            @Field("appointment_date") String appointment_date,
            @Field("slot_id") String slot_id
    );

    @POST("doctor_api/add_appointment")
    @FormUrlEncoded
    Call<PatientsListResponse> add_appointment_new_patient(
            @Field("api_username") String apiUsername,
            @Field("api_password") String apiPassword,
            @Field("description") String description,
            @Field("doctor_id") String doctor_id,
            @Field("consultation_id") String consultation_id,
            @Field("appointment_date") String appointment_date,
            @Field("slot_id") String slot_id,
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("phone") String phone,
            @Field("status") String status
    );

    @POST("doctor_api/doctor_slots_add")
    @FormUrlEncoded
    Call<ResponseBody> doctor_slots_add(
            @Field("api_username") String apiUsername,
            @Field("api_password") String apiPassword,
            @Field("doctor_id") String doctor_id,
            @Field("start_time") String start_time,
            @Field("end_time") String end_time,
            @Field("slot_duration") String slot_duration
    );

    @POST("doctor_api/update_doc_available_days")
    @FormUrlEncoded
    Call<ResponseBody> update_doc_available_days(
            @Field("api_username") String apiUsername,
            @Field("api_password") String apiPassword,
            @Field("doctor_id") String doctor_id,
            @Field("monday") String monday,
            @Field("tuesday") String tuesday,
            @Field("wednesday") String wednesday,
            @Field("thursday") String thursday,
            @Field("friday") String friday,
            @Field("saturday") String saturday,
            @Field("sunday") String sunday
    );

//


}
