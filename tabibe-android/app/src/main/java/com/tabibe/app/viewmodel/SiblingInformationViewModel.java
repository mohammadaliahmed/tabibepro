package com.tabibe.app.viewmodel;

import com.tabibe.app.model.BaseResponse;
import com.tabibe.app.model.UserResponse;
import com.tabibe.app.repo.SiblingInformationRepo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SiblingInformationViewModel extends ViewModel {

    private MutableLiveData<UserResponse> userResponseMutableLiveData;
    private MutableLiveData<BaseResponse> baseResponseMutableLiveData;
    private SiblingInformationRepo siblingInformationRepo;

    public void init() {
        if (userResponseMutableLiveData!=null || baseResponseMutableLiveData!=null) {
            return;
        }
        siblingInformationRepo = SiblingInformationRepo.getInstance();
    }

    public void fetchUsers(String au , String ap , String id) {
        userResponseMutableLiveData = siblingInformationRepo.fetchUsers(au , ap,id);
    }

    public void addMember(String au , String ap , String id,String fName , String lName){
        baseResponseMutableLiveData = siblingInformationRepo.addUser(au,ap,id,fName,lName);
    }

    public void updateMember(String au , String ap , String id,String fName , String lName){
        baseResponseMutableLiveData = siblingInformationRepo.updateUser(au,ap,id,fName,lName);
    }

    public void deleteMember(String au , String ap , String id) {
        baseResponseMutableLiveData = siblingInformationRepo.deleteUser(au , ap,id);
    }

    public LiveData<BaseResponse> getDeletedUserResponse(){
        return baseResponseMutableLiveData;
    }

    public LiveData<UserResponse> getUserResponse() {
        return userResponseMutableLiveData;
    }
}
