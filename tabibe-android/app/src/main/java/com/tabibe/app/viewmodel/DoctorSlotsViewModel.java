package com.tabibe.app.viewmodel;

import com.tabibe.app.model.BaseResponse;
import com.tabibe.app.model.DoctorSlotsResponse;
import com.tabibe.app.model.UserResponse;
import com.tabibe.app.repo.SlotsRepo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DoctorSlotsViewModel extends ViewModel {


    private MutableLiveData<DoctorSlotsResponse> mutableLiveData;
    private MutableLiveData<UserResponse> userResponseMutableLiveData;

    private MutableLiveData<BaseResponse> baseResponseMutableLiveData;

    private SlotsRepo slotsRepo;

    public void init() {
        if (mutableLiveData != null || userResponseMutableLiveData!=null || baseResponseMutableLiveData!=null ) {
            return;
        }
        slotsRepo = SlotsRepo.getInstance();
    }

    public void fetchSlots(String au , String ap , String id) {
        mutableLiveData = slotsRepo.fetchSlots(au , ap,id);
    }

    public void fetchUsers(String au , String ap , String id) {
        userResponseMutableLiveData = slotsRepo.fetchUsers(au , ap,id);
    }

    public void makeAppointment(String au , String ap , String did , String pid , String cid , String sid , String at) {
        baseResponseMutableLiveData = slotsRepo.makeAppointMent(au , ap,did,pid,cid,sid,at);
    }

    public LiveData<BaseResponse> getAppointmentResponse() {
        return baseResponseMutableLiveData;
    }

    public LiveData<UserResponse> getUserResponse() {
        return userResponseMutableLiveData;
    }

    public LiveData<DoctorSlotsResponse> getSlotsResponse() {
        return mutableLiveData;
    }
}
