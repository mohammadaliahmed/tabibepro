package com.tabibe.app.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tabibe.app.model.LoginResponse;
import com.tabibe.app.model.SignUpResponse;
import com.tabibe.app.repo.SignInRepo;

public class MainViewModel extends AndroidViewModel {

    private MutableLiveData<LoginResponse> mutableLiveData;
    private MutableLiveData<SignUpResponse> mutableSignUpLiveData;

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    private SignInRepo loginRepo;

    public void init() {
        if (mutableLiveData != null || mutableSignUpLiveData!=null) {
            return;
        }
        loginRepo = SignInRepo.getInstance();

    }

    public void login(String au , String ap , String e , String p) {
        mutableLiveData = loginRepo.login(au , ap , e , p);
    }

    public void signUp(String adminUser , String adminPassword , String firstName , String lastName , String email , String password , String phone ){

        mutableSignUpLiveData = loginRepo.signUp(adminUser,adminPassword,firstName , lastName,email,password,phone);
    }

    public LiveData<SignUpResponse> getSignUpResponse() {
        return mutableSignUpLiveData;
    }

    public LiveData<LoginResponse> getLoginResponse() {
        return mutableLiveData;
    }
}
