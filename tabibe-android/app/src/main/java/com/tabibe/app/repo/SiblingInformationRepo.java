package com.tabibe.app.repo;

import com.tabibe.app.TabibeApplication;
import com.tabibe.app.model.BaseResponse;
import com.tabibe.app.model.UserResponse;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SiblingInformationRepo {

    private static SiblingInformationRepo siblingInformationRepo;

    public static SiblingInformationRepo getInstance() {
        if (siblingInformationRepo == null) {
            siblingInformationRepo = new SiblingInformationRepo();
        }
        return siblingInformationRepo;
    }

    public SiblingInformationRepo() {

    }

    public MutableLiveData<UserResponse> fetchUsers(String au , String ap , String id) {
        MutableLiveData<UserResponse> newsData = new MutableLiveData<>();
        TabibeApplication.getRestClient().getApiService().fetchUsers(au,ap,id).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call,
                                   Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    newsData.setValue(response.body());
                }else if(response.code()==404){
                    UserResponse loginResponse = new UserResponse();
                    loginResponse.setStatus(response.code());
                    newsData.setValue(loginResponse);
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                UserResponse loginResponse = new UserResponse();
                loginResponse.setStatus(415);
                newsData.setValue(loginResponse);
            }
        });
        return newsData;
    }

    public MutableLiveData<BaseResponse> deleteUser(String au , String ap , String id) {
        MutableLiveData<BaseResponse> newsData = new MutableLiveData<>();
        TabibeApplication.getRestClient().getApiService().deleteMember(au,ap,id).enqueue(new Callback<BaseResponse>() {
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
                BaseResponse loginResponse = new BaseResponse();
                loginResponse.setStatus(415);
                newsData.setValue(loginResponse);
            }
        });
        return newsData;
    }

    public MutableLiveData<BaseResponse> addUser(String au , String ap , String id,String fName , String lName) {
        MutableLiveData<BaseResponse> newsData = new MutableLiveData<>();
        TabibeApplication.getRestClient().getApiService().addMember(au,ap,id,fName,lName).enqueue(new Callback<BaseResponse>() {
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
                BaseResponse loginResponse = new BaseResponse();
                loginResponse.setStatus(415);
                newsData.setValue(loginResponse);
            }
        });
        return newsData;
    }

    public MutableLiveData<BaseResponse> updateUser(String au , String ap , String id,String fName , String lName) {
        MutableLiveData<BaseResponse> newsData = new MutableLiveData<>();
        TabibeApplication.getRestClient().getApiService().updateMember(au,ap,id,fName,lName).enqueue(new Callback<BaseResponse>() {
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
                BaseResponse loginResponse = new BaseResponse();
                loginResponse.setStatus(415);
                newsData.setValue(loginResponse);
            }
        });
        return newsData;
    }

}
