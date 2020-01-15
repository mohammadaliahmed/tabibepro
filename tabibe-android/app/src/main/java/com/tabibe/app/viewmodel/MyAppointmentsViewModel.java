package com.tabibe.app.viewmodel;

import com.tabibe.app.model.BaseResponse;
import com.tabibe.app.model.MyAppointmentResponse;
import com.tabibe.app.repo.MyAppointmentRepo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyAppointmentsViewModel extends ViewModel {

    private MutableLiveData<MyAppointmentResponse> mutableLiveData;
    private MutableLiveData<BaseResponse> mutableDeletedAppointmentLiveData;

    private MyAppointmentRepo appointmentRepo;

    public void init() {
        if (mutableLiveData != null || mutableDeletedAppointmentLiveData !=null) {
            return;
        }
        appointmentRepo = MyAppointmentRepo.getInstance();
    }

    public void fetchAppointments(String au , String ap , String id ) {
        mutableLiveData = appointmentRepo.getAppointments(au , ap,id);
    }

    public void deleteAppointment(String au , String ap , String id){
        mutableDeletedAppointmentLiveData = appointmentRepo.deleteAppointments(au,ap,id);
    }

    public LiveData<BaseResponse> getDeletedAppointmentResponse() {
        return mutableDeletedAppointmentLiveData;
    }

    public LiveData<MyAppointmentResponse> getAppointmentResponse() {
        return mutableLiveData;
    }
}
