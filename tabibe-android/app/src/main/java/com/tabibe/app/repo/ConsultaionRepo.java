package com.tabibe.app.repo;

import com.tabibe.app.TabibeApplication;
import com.tabibe.app.model.ConsultationResponse;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConsultaionRepo {

    private static ConsultaionRepo consultaionRepo;

    public static ConsultaionRepo getInstance() {
        if (consultaionRepo == null) {
            consultaionRepo = new ConsultaionRepo();
        }
        return consultaionRepo;
    }

    public ConsultaionRepo() {

    }

    public MutableLiveData<ConsultationResponse> getConsultations(String au , String ap , String docId) {
        MutableLiveData<ConsultationResponse> newsData = new MutableLiveData<>();
        TabibeApplication.getRestClient().getApiService().fetchConsulations(au,ap,docId).enqueue(new Callback<ConsultationResponse>() {
            @Override
            public void onResponse(Call<ConsultationResponse> call,
                                   Response<ConsultationResponse> response) {
                if (response.isSuccessful()) {
                    newsData.setValue(response.body());
                }else if(response.code()==404){
                    ConsultationResponse loginResponse = new ConsultationResponse();
                    loginResponse.setStatus(response.code());
                    newsData.setValue(loginResponse);
                }
            }

            @Override
            public void onFailure(Call<ConsultationResponse> call, Throwable t) {
                ConsultationResponse loginResponse = new ConsultationResponse();
                loginResponse.setStatus(415);
                newsData.setValue(loginResponse);
            }
        });
        return newsData;
    }
}
