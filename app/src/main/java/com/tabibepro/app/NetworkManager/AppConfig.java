package com.tabibepro.app.NetworkManager;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppConfig {
    static String BASE_URL = "https://tabibe.ovh";
    public static String API_USERNAME = "tabibe_api_admin";
   public  static String API_PASSWORD = "M,(}^dV]q$Z~r2?r";
    public static String BASE_URL_Image = "https://tabibe.ovh";


    public static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
