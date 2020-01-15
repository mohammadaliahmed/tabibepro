package com.tabibe.app.repo;

import com.tabibe.app.TabibeApplication;
import com.tabibe.app.model.CityResponse;
import com.tabibe.app.model.SpecialityResponse;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpecialityAndCityRepo {

    private static SpecialityAndCityRepo specialityRepo;

    public static SpecialityAndCityRepo getInstance() {
        if (specialityRepo == null) {
            specialityRepo = new SpecialityAndCityRepo();
        }
        return specialityRepo;
    }

    public SpecialityAndCityRepo() {

    }


    public MutableLiveData<SpecialityResponse> getSpecialties(String au , String ap) {
        MutableLiveData<SpecialityResponse> newsData = new MutableLiveData<>();
        TabibeApplication.getRestClient().getApiService().fetchSpecialities(au,ap).enqueue(new Callback<SpecialityResponse>() {
            @Override
            public void onResponse(Call<SpecialityResponse> call,
                                   Response<SpecialityResponse> response) {
                if (response.isSuccessful()) {
                    newsData.setValue(response.body());
                }else if(response.code()==404){
                    SpecialityResponse loginResponse = new SpecialityResponse();
                    loginResponse.setStatus(response.code());
                    newsData.setValue(loginResponse);
                }
            }

            @Override
            public void onFailure(Call<SpecialityResponse> call, Throwable t) {
                SpecialityResponse loginResponse = new SpecialityResponse();
                loginResponse.setStatus(415);
                newsData.setValue(loginResponse);
            }
        });
        return newsData;
    }

    public MutableLiveData<CityResponse> getCities(String au , String ap) {
        MutableLiveData<CityResponse> newsData = new MutableLiveData<>();
        TabibeApplication.getRestClient().getApiService().fetchCities(au,ap).enqueue(new Callback<CityResponse>() {
            @Override
            public void onResponse(Call<CityResponse> call,
                                   Response<CityResponse> response) {
                if (response.isSuccessful()) {
                    newsData.setValue(response.body());
                }else if(response.code()==404){
                    CityResponse loginResponse = new CityResponse();
                    loginResponse.setStatus(response.code());
                    newsData.setValue(loginResponse);
                }
            }

            @Override
            public void onFailure(Call<CityResponse> call, Throwable t) {
                CityResponse loginResponse = new CityResponse();
                loginResponse.setStatus(415);
                newsData.setValue(loginResponse);
            }
        });
        return newsData;
    }


}
