package com.tabibe.app.repo;

import com.tabibe.app.TabibeApplication;
import com.tabibe.app.model.BaseResponse;
import com.tabibe.app.model.DoctorSlotsResponse;
import com.tabibe.app.model.UserResponse;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SlotsRepo {

    private static SlotsRepo slotsRepo;

    public static SlotsRepo getInstance() {
        if (slotsRepo == null) {
            slotsRepo = new SlotsRepo();
        }
        return slotsRepo;
    }

    public SlotsRepo() {

    }

    public MutableLiveData<DoctorSlotsResponse> fetchSlots(String au , String ap , String id) {
        MutableLiveData<DoctorSlotsResponse> newsData = new MutableLiveData<>();
        TabibeApplication.getRestClient().getApiService().fetchSlots(au,ap,id).enqueue(new Callback<DoctorSlotsResponse>() {
            @Override
            public void onResponse(Call<DoctorSlotsResponse> call,
                                   Response<DoctorSlotsResponse> response) {
                if (response.isSuccessful()) {
                    newsData.setValue(response.body());
                }else if(response.code()==404){
                    DoctorSlotsResponse loginResponse = new DoctorSlotsResponse();
                    loginResponse.setStatus(response.code());
                    newsData.setValue(loginResponse);
                }
            }

            @Override
            public void onFailure(Call<DoctorSlotsResponse> call, Throwable t) {
                DoctorSlotsResponse loginResponse = new DoctorSlotsResponse();
                loginResponse.setStatus(415);
                newsData.setValue(loginResponse);
            }
        });
        return newsData;
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


    public MutableLiveData<BaseResponse> makeAppointMent(String au , String ap , String did , String pid , String cid , String sid , String at) {
        MutableLiveData<BaseResponse> newsData = new MutableLiveData<>();
        TabibeApplication.getRestClient().getApiService().makeAppointment(au,ap,did,pid,cid,sid,at,"descripton").enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call,
                                   Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    newsData.setValue(response.body());
                }else if(response.code()==404){
                    UserResponse loginResponse = new UserResponse();
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
