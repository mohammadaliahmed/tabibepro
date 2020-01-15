package com.tabibe.app.viewmodel;

import com.tabibe.app.model.BaseResponse;
import com.tabibe.app.model.SignUpResponse;
import com.tabibe.app.model.LoginResponse;
import com.tabibe.app.repo.SignInRepo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SignInViewModel extends ViewModel {


    private MutableLiveData<LoginResponse> mutableLiveData;
    private MutableLiveData<SignUpResponse> mutableSignUpLiveData;
    private MutableLiveData<BaseResponse> muteableForgotPasswordLiveData;

    private SignInRepo loginRepo;

    public void init() {
        if (mutableLiveData != null || mutableSignUpLiveData!=null || muteableForgotPasswordLiveData!=null) {
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

    public void forgotPassword(String adminUser , String adminPassword , String email){
         muteableForgotPasswordLiveData = loginRepo.forgotPassword(adminUser,adminPassword,email);
    }

    public LiveData<BaseResponse> getForgotPassword(){
        return muteableForgotPasswordLiveData;
    }

    public LiveData<SignUpResponse> getSignUpResponse() {
        return mutableSignUpLiveData;
    }

    public LiveData<LoginResponse> getLoginResponse() {
        return mutableLiveData;
    }


}
