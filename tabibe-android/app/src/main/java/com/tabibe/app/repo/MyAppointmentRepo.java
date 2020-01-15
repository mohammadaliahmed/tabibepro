package com.tabibe.app.repo;

import com.tabibe.app.TabibeApplication;
import com.tabibe.app.model.BaseResponse;
import com.tabibe.app.model.MyAppointmentResponse;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAppointmentRepo {

    private static MyAppointmentRepo myAppointmentRepo;

    public static MyAppointmentRepo getInstance() {
        if (myAppointmentRepo == null) {
            myAppointmentRepo = new MyAppointmentRepo();
        }
        return myAppointmentRepo;
    }

    public MyAppointmentRepo() {

    }

    public MutableLiveData<MyAppointmentResponse> getAppointments(String au , String ap,String id) {
        MutableLiveData<MyAppointmentResponse> newsData = new MutableLiveData<>();
        TabibeApplication.getRestClient().getApiService().fetchAppointments(au,ap,id).enqueue(new Callback<MyAppointmentResponse>() {
            @Override
            public void onResponse(Call<MyAppointmentResponse> call,
                                   Response<MyAppointmentResponse> response) {
                if (response.isSuccessful()) {
                    newsData.setValue(response.body());
                }else if(response.code()==404){
                    MyAppointmentResponse loginResponse = new MyAppointmentResponse();
                    loginResponse.setStatus(response.code());
                    newsData.setValue(loginResponse);
                }
            }

            @Override
            public void onFailure(Call<MyAppointmentResponse> call, Throwable t) {
                MyAppointmentResponse loginResponse = new MyAppointmentResponse();
                loginResponse.setStatus(415);
                newsData.setValue(loginResponse);
            }
        });
        return newsData;
    }

    public MutableLiveData<BaseResponse> deleteAppointments(String au , String ap, String id) {
        MutableLiveData<BaseResponse> newsData = new MutableLiveData<>();
        TabibeApplication.getRestClient().getApiService().deleteAppointment(au,ap,id).enqueue(new Callback<BaseResponse>() {
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
