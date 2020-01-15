package com.tabibe.app.repo;

import com.tabibe.app.TabibeApplication;
import com.tabibe.app.model.BaseResponse;
import com.tabibe.app.model.SignUpResponse;
import com.tabibe.app.model.LoginResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tabibe.app.util.ConstantValues;
import com.tabibe.app.util.Constants;

import androidx.lifecycle.MutableLiveData;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInRepo {

    private static SignInRepo signInRepo;

    public static SignInRepo getInstance() {
        if (signInRepo == null) {
            signInRepo = new SignInRepo();
        }
        return signInRepo;
    }

    public SignInRepo() {

    }


    public MutableLiveData<LoginResponse> login(String au , String ap , String e , String p) {
        MutableLiveData<LoginResponse> newsData = new MutableLiveData<>();
        TabibeApplication.getRestClient().getApiService().login(au,ap,e,p).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call,
                                   Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    newsData.setValue(response.body());
                }else if(response.code()==404){
                    LoginResponse loginResponse = new LoginResponse();
                    loginResponse.setStatus(response.code());
                    newsData.setValue(loginResponse);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                LoginResponse loginResponse = new LoginResponse();
                loginResponse.setStatus(415);
                newsData.setValue(loginResponse);
            }
        });
        return newsData;
    }



    public MutableLiveData<SignUpResponse> signUp(String adminUser , String adminPassword , String firstName , String lastName , String email , String password , String phone ) {
        MutableLiveData<SignUpResponse> newsData = new MutableLiveData<>();
        TabibeApplication.getRestClient().getApiService().signUp(adminUser,adminPassword,firstName,password,email,lastName,phone,ConstantValues.SOCIAL_REGISTER).enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call,
                                   Response<SignUpResponse> response) {
                if (response.isSuccessful()) {
                    newsData.setValue(response.body());
                }else if(response.code()==404){
                    Gson gson = new GsonBuilder().create();
                    BaseResponse baseResponse = new BaseResponse();
                    try {
                        baseResponse = gson.fromJson(response.errorBody().string(), BaseResponse.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    SignUpResponse loginResponse = new SignUpResponse();
                    loginResponse.setStatus(response.code());
                    loginResponse.setMessage(baseResponse.getMessage());
                    newsData.setValue(loginResponse);
                }
            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                SignUpResponse loginResponse = new SignUpResponse();
                loginResponse.setStatus(415);
                newsData.setValue(loginResponse);
            }
        });
        return newsData;
    }

    public MutableLiveData<BaseResponse> forgotPassword(String adminUser , String adminPassword , String email) {
        MutableLiveData<BaseResponse> newsData = new MutableLiveData<>();
        TabibeApplication.getRestClient().getApiService().forgotPassword(adminUser,adminPassword,email).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call,
                                   Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    newsData.setValue(response.body());
                }else if(response.code()==404){
                    BaseResponse loginResponse = new BaseResponse();
                    loginResponse.setStatus(response.code());
                    newsData.setValue(loginResponse);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                SignUpResponse loginResponse = new SignUpResponse();
                loginResponse.setStatus(415);
                newsData.setValue(loginResponse);
            }
        });
        return newsData;
    }
}
