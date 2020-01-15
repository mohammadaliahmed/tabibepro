package com.tabibe.app;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.tabibe.app.retrofit.RestClient;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class TabibeApplication extends Application {

    private static RestClient restClient;

    @Override
    public void onCreate() {
        super.onCreate();
        Iconify.with(new FontAwesomeModule());
        initializeRestClient();
        FacebookSdk.sdkInitialize(getApplicationContext());

    }

    private void initializeRestClient() {
        restClient = new RestClient();
    }

    public static RestClient getRestClient() {
        return restClient;
    }

}
