package com.tabibe.app.retrofit;

import com.tabibe.app.api.ApiService;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    ApiService apiService;

    public RestClient() {



        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.SECONDS).addInterceptor((new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();

                        Response response = chain.proceed(request);
                        return response;
                    }
                })).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://tabibe.ovh/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        apiService = retrofit.create(ApiService.class);
    }



    public ApiService getApiService() {
        return apiService;
    }
}
