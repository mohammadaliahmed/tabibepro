package com.tabibe.app.api;

import com.tabibe.app.model.BaseResponse;
import com.tabibe.app.model.CityResponse;
import com.tabibe.app.model.ConsultationResponse;
import com.tabibe.app.model.DoctorResponse;
import com.tabibe.app.model.DoctorSlotsResponse;
import com.tabibe.app.model.MyAppointmentResponse;
import com.tabibe.app.model.SignUpResponse;
import com.tabibe.app.model.SpecialityResponse;
import com.tabibe.app.model.LoginResponse;
import com.tabibe.app.model.UserResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {


    @FormUrlEncoded
    @POST("api/login_user")
    Call<LoginResponse> login(@Field("api_username") String api_username , @Field("api_password") String api_password, @Field("user_email")  String email , @Field("user_password") String password);

    @FormUrlEncoded
    @POST("api/signup_user")
    Call<SignUpResponse> signUp(@Field("api_username") String api_username , @Field("api_password") String api_password, @Field("first_name")  String firstName , @Field("password") String password , @Field("email") String email , @Field("last_name") String lastName , @Field("phone") String phone, @Field("is_activated") String is_activated);

    @FormUrlEncoded
    @POST("api/list_specialities")
    Call<SpecialityResponse> fetchSpecialities(@Field("api_username") String api_username , @Field("api_password") String api_password);

    @FormUrlEncoded
    @POST("api/list_cities")
    Call<CityResponse> fetchCities(@Field("api_username") String api_username , @Field("api_password") String api_password);

    @FormUrlEncoded
    @POST("api/list_doctors")
    Call<DoctorResponse> fetchAllDocs(@Field("api_username") String api_username , @Field("api_password") String api_password);

    @FormUrlEncoded
    @POST("api/list_doctors")
    Call<DoctorResponse> fetchAllDocsOnBasisOfSpeciality(@Field("api_username") String api_username , @Field("api_password") String api_password , @Field("speciality_id") String id);

    @FormUrlEncoded
    @POST("api/list_doctors")
    Call<DoctorResponse> fetchAllDocsOnBasisOfCities(@Field("api_username") String api_username , @Field("api_password") String api_password , @Field("city_id") String id);

    @FormUrlEncoded
    @POST("api/list_doctors")
    Call<DoctorResponse> fetchAllDocsOnBasisOfCitiesAndSpecialityId(@Field("api_username") String api_username , @Field("api_password") String api_password , @Field("city_id") String id,@Field("speciality_id") String specialityId);

    @FormUrlEncoded
    @POST("api/docotors_consultation")
    Call<ConsultationResponse> fetchConsulations(@Field("api_username") String api_username , @Field("api_password") String api_password , @Field("doctor_id") String id);

    @FormUrlEncoded
    @POST("api/doctor_slots")
    Call<DoctorSlotsResponse> fetchSlots(@Field("api_username") String api_username , @Field("api_password") String api_password , @Field("doctor_id") String id);

    @FormUrlEncoded
    @POST("api/list_users")
    Call<UserResponse> fetchUsers(@Field("api_username") String api_username , @Field("api_password") String api_password , @Field("user_id") String id);

    @FormUrlEncoded
    @POST("api/add_appointment")
    Call<BaseResponse> makeAppointment(@Field("api_username") String api_username , @Field("api_password") String api_password , @Field("doctor_id") String id , @Field("patient_id") String pid , @Field("consultation_id") String cid, @Field("slot_id") String sid, @Field("appointment_date") String ad , @Field("description") String des);

    @FormUrlEncoded
    @POST("api/list_appointments")
    Call<MyAppointmentResponse> fetchAppointments(@Field("api_username") String api_username , @Field("api_password") String api_password , @Field("user_id") String id);

    @FormUrlEncoded
    @POST("api/delete_member")
    Call<BaseResponse> deleteMember(@Field("api_username") String api_username , @Field("api_password") String api_password , @Field("member_id") String id);

    @FormUrlEncoded
    @POST("api/add_member")
    Call<BaseResponse> addMember(@Field("api_username") String api_username , @Field("api_password") String api_password , @Field("user_id") String id,@Field("first_name") String fName , @Field("last_name") String lName);

    @FormUrlEncoded
    @POST("api/update_member")
    Call<BaseResponse> updateMember(@Field("api_username") String api_username , @Field("api_password") String api_password , @Field("member_id") String id,@Field("first_name") String fName , @Field("last_name") String lName);

    @FormUrlEncoded
    @POST("api/forget_password")
    Call<BaseResponse> forgotPassword(@Field("api_username") String api_username , @Field("api_password") String api_password , @Field("email") String email);

    @FormUrlEncoded
    @POST("api/delete_appointment")
    Call<BaseResponse> deleteAppointment(@Field("api_username") String api_username , @Field("api_password") String api_password , @Field("appointment_id") String id);

}
