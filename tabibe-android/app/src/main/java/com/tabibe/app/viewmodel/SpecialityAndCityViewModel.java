package com.tabibe.app.viewmodel;

import com.tabibe.app.model.CityResponse;
import com.tabibe.app.model.SpecialityResponse;
import com.tabibe.app.repo.SpecialityAndCityRepo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SpecialityAndCityViewModel extends ViewModel {

    private MutableLiveData<SpecialityResponse> mutableLiveData;
    private MutableLiveData<CityResponse> mutableCityLiveData;
    private SpecialityAndCityRepo specialityRepo;

    public void init() {
        if (mutableLiveData != null || mutableCityLiveData!=null) {
            return;
        }
        specialityRepo = SpecialityAndCityRepo.getInstance();

    }

    public void fetchSpeciality(String au , String ap ) {
        mutableLiveData = specialityRepo.getSpecialties(au , ap);
    }

    public LiveData<SpecialityResponse> getLoginResponse() {
        return mutableLiveData;
    }

    public void fetchCities(String au , String ap ) {
        mutableCityLiveData = specialityRepo.getCities(au , ap);
    }

    public LiveData<CityResponse> getCitiesResponse() {
        return mutableCityLiveData;
    }

}
