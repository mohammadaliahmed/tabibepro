package com.tabibe.app.viewmodel;

import com.tabibe.app.model.ConsultationResponse;
import com.tabibe.app.repo.ConsultaionRepo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ConsultationViewModel extends ViewModel {

    private MutableLiveData<ConsultationResponse> mutableLiveData;
    private ConsultaionRepo consultaionRepo;

    public void init() {
        if (mutableLiveData != null ) {
            return;
        }
        consultaionRepo = ConsultaionRepo.getInstance();
    }

    public void fetchConsultation(String au , String ap , String id ) {
        mutableLiveData = consultaionRepo.getConsultations(au , ap,id);
    }

    public LiveData<ConsultationResponse> getConsultationResponse() {
        return mutableLiveData;
    }

}
