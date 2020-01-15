package com.tabibe.app.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {

    public static Boolean checkInternetConnection(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager)ctx.getSystemService("connectivity");
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null ? true : false;
    }
}
