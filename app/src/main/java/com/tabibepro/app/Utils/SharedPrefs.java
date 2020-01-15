package com.tabibepro.app.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tabibepro.app.Models.UserModel;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by AliAh on 20/02/2018.
 */

public class SharedPrefs {


    private SharedPrefs() {

    }




    public static void setHeadNotificationCount(String id, String count) {
        preferenceSetter(id, count);
    }

    public static String getHeadNotificationCount(String id) {
        return preferenceGetter(id);
    }


    public static void setUserModel(UserModel model) {

        Gson gson = new Gson();
        String json = gson.toJson(model);
        preferenceSetter("UserModel", json);
    }

    public static UserModel getUserModel() {
        Gson gson = new Gson();
        UserModel model = gson.fromJson(preferenceGetter("UserModel"), UserModel.class);
        return model;
    }


//    public static void setRejectedOrderList(List<String> itemList) {
//
//        Gson gson = new Gson();
//        String json = gson.toJson(itemList);
//        preferenceSetter("rejectedOrders", json);
//    }
//
//    public static ArrayList getRejectedOrderList() {
//        Gson gson = new Gson();
//        ArrayList<String> playersList = (ArrayList<String>) gson.fromJson(preferenceGetter("rejectedOrders"),
//                new TypeToken<ArrayList<String>>() {
//                }.getType());
//        return playersList;
//    }


    public static void preferenceSetter(String key, String value) {
        SharedPreferences pref = ApplicationClass.getInstance().getApplicationContext().getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String preferenceGetter(String key) {
        SharedPreferences pref;
        String value = "";
        pref = ApplicationClass.getInstance().getApplicationContext().getSharedPreferences("user", MODE_PRIVATE);
        value = pref.getString(key, "");
        return value;
    }


    public static void logout() {
        SharedPreferences pref = ApplicationClass.getInstance().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }


}
