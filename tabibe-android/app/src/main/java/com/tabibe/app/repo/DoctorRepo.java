package com.tabibe.app.repo;

import com.tabibe.app.TabibeApplication;
import com.tabibe.app.model.DoctorResponse;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorRepo {

    private static DoctorRepo doctorRepo;

    public static DoctorRepo getInstance() {
        if (doctorRepo == null) {
            doctorRepo = new DoctorRepo();
        }
        return doctorRepo;
    }

    public DoctorRepo() {

    }

    public MutableLiveData<DoctorResponse> getDoctors(String au , String ap) {
        MutableLiveData<DoctorResponse> newsData = new MutableLiveData<>();
        TabibeApplication.getRestClient().getApiService().fetchAllDocs(au,ap).enqueue(new Callback<DoctorResponse>() {
            @Override
            public void onResponse(Call<DoctorResponse> call,
                                   Response<DoctorResponse> response) {
                if (response.isSuccessful()) {
                    newsData.setValue(response.body());
                }else if(response.code()==404){
                    DoctorResponse loginResponse = new DoctorResponse();
                    loginResponse.setStatus(response.code());
                    newsData.setValue(loginResponse);
                }
            }

            @Override
            public void onFailure(Call<DoctorResponse> call, Throwable t) {
                DoctorResponse loginResponse = new DoctorResponse();
                loginResponse.setStatus(415);
                newsData.setValue(loginResponse);
            }
        });
        return newsData;
    }

    public MutableLiveData<DoctorResponse> getDoctorsOnBasisOfSpeciality(String au , String ap , String id) {
        MutableLiveData<DoctorResponse> newsData = new MutableLiveData<>();
        TabibeApplication.getRestClient().getApiService().fetchAllDocsOnBasisOfSpeciality(au,ap,id).enqueue(new Callback<DoctorResponse>() {
            @Override
            public void onResponse(Call<DoctorResponse> call,
                                   Response<DoctorResponse> response) {
                if (response.isSuccessful()) {
                    newsData.setValue(response.body());
                }else if(response.code()==404){
                    DoctorResponse loginResponse = new DoctorResponse();
                    loginResponse.setStatus(response.code());
                    newsData.setValue(loginResponse);
                }
            }

            @Override
            public void onFailure(Call<DoctorResponse> call, Throwable t) {
                DoctorResponse loginResponse = new DoctorResponse();
                loginResponse.setStatus(415);
                newsData.setValue(loginResponse);
            }
        });
        return newsData;
    }

    public MutableLiveData<DoctorResponse> getDoctorsOnBasisOfCities(String au , String ap,String id) {
        MutableLiveData<DoctorResponse> newsData = new MutableLiveData<>();
        TabibeApplication.getRestClient().getApiService().fetchAllDocsOnBasisOfCities(au,ap,id).enqueue(new Callback<DoctorResponse>() {
            @Override
            public void onResponse(Call<DoctorResponse> call,
                                   Response<DoctorResponse> response) {
                if (response.isSuccessful()) {
                    newsData.setValue(response.body());
                }else if(response.code()==404){
                    DoctorResponse loginResponse = new DoctorResponse();
                    loginResponse.setStatus(response.code());
                    newsData.setValue(loginResponse);
                }
            }

            @Override
            public void onFailure(Call<DoctorResponse> call, Throwable t) {
                DoctorResponse loginResponse = new DoctorResponse();
                loginResponse.setStatus(415);
                newsData.setValue(loginResponse);
            }
        });
        return newsData;
    }


    public MutableLiveData<DoctorResponse> getDoctorsOnBasisOfCitiesAndSpecialities(String au , String ap,String id,String specialityId) {
        MutableLiveData<DoctorResponse> newsData = new MutableLiveData<>();
        TabibeApplication.getRestClient().getApiService().fetchAllDocsOnBasisOfCitiesAndSpecialityId(au,ap,id,specialityId).enqueue(new Callback<DoctorResponse>() {
            @Override
            public void onResponse(Call<DoctorResponse> call,
                                   Response<DoctorResponse> response) {
                if (response.isSuccessful()) {
                    newsData.setValue(response.body());
                }else if(response.code()==404){
                    DoctorResponse loginResponse = new DoctorResponse();
                    loginResponse.setStatus(response.code());
                    newsData.setValue(loginResponse);
                }
            }

            @Override
            public void onFailure(Call<DoctorResponse> call, Throwable t) {
                DoctorResponse loginResponse = new DoctorResponse();
                loginResponse.setStatus(415);
                newsData.setValue(loginResponse);
            }
        });
        return newsData;
    }




}
