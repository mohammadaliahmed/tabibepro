package com.tabibe.app.viewmodel;

import com.tabibe.app.model.DoctorResponse;
import com.tabibe.app.repo.DoctorRepo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DoctorViewModel extends ViewModel {

    private MutableLiveData<DoctorResponse> mutableLiveData;
    private DoctorRepo doctorRepo;

    public void init() {
        if (mutableLiveData != null ) {
            return;
        }
        doctorRepo = DoctorRepo.getInstance();
    }

    public void fetDoctors(String au , String ap ) {
        mutableLiveData = doctorRepo.getDoctors(au , ap);
    }

    public void fetDoctorsOnBasisOfSpeciality(String au , String ap , String id ) {
        mutableLiveData = doctorRepo.getDoctorsOnBasisOfSpeciality(au , ap,id);
    }

    public void fetDoctorsOnBasisOfCities(String au , String ap , String id) {
        mutableLiveData = doctorRepo.getDoctorsOnBasisOfCities(au , ap,id);
    }

    public void fetDoctorsOnBasisOfCitiesAndSpeciality(String au , String ap , String id,String specialityId) {
        mutableLiveData = doctorRepo.getDoctorsOnBasisOfCitiesAndSpecialities(au , ap,id,specialityId);
    }

    public LiveData<DoctorResponse> getLoginResponse() {
        return mutableLiveData;
    }

}
