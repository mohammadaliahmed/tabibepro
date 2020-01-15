package com.tabibepro.app.NetworkManager;


import com.tabibepro.app.NetworkResponses.AvailableDaysResponse;
import com.tabibepro.app.NetworkResponses.LoginResponse;
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

    @POST("doctor_api/doctor_slots_listing")
    @FormUrlEncoded
    Call<ScheduleListResponse> doctor_slots_listing(
            @Field("api_username") String apiUsername,
            @Field("api_password") String apiPassword,
            @Field("doctor_id") String first_name
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
