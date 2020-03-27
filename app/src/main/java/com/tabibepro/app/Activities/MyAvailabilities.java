package com.tabibepro.app.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import com.tabibepro.app.Models.AvailableDaysModel;
import com.tabibepro.app.Models.UserModel;
import com.tabibepro.app.NetworkManager.AppConfig;
import com.tabibepro.app.NetworkManager.UserClient;
import com.tabibepro.app.NetworkResponses.AvailableDaysResponse;
import com.tabibepro.app.NetworkResponses.LoginResponse;
import com.tabibepro.app.R;
import com.tabibepro.app.Utils.CommonUtils;
import com.tabibepro.app.Utils.SharedPrefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAvailabilities extends AppCompatActivity {

    RelativeLayout wholeLayout;
    HashMap<String, String> checkMap = new HashMap<>();
    HashMap<String, String> alreadyMap = new HashMap<>();

    CheckBox monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_availabilities);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.setTitle("Mes disponibilités");

        wholeLayout = findViewById(R.id.wholeLayout);

        monday = findViewById(R.id.monday);
        tuesday = findViewById(R.id.tuesday);
        wednesday = findViewById(R.id.wednesday);
        thursday = findViewById(R.id.thursday);
        friday = findViewById(R.id.friday);
        saturday = findViewById(R.id.saturday);
        sunday = findViewById(R.id.sunday);
        update = findViewById(R.id.update);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadDatatToServe();
            }
        });

        checkListneres();


        getDataFromServer();

    }

    private void uploadDatatToServe() {
        wholeLayout.setVisibility(View.VISIBLE);
        UserClient getResponse = AppConfig.getRetrofit().create(UserClient.class);
        Call<ResponseBody> call = getResponse.update_doc_available_days(AppConfig.API_USERNAME, AppConfig.API_PASSWORD,
                SharedPrefs.getUserModel().getId(),
                checkMap.get("Monday"),
                checkMap.get("Tuesday"),
                checkMap.get("Wednesday"),
                checkMap.get("Thursday"),
                checkMap.get("Friday"),
                checkMap.get("Saturday"),
                checkMap.get("Sunday")
        );
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                wholeLayout.setVisibility(View.GONE);
                if (response.code() == 200) {
                    try {
                        JSONObject jObjError = new JSONObject(response.body().string());
                        CommonUtils.showToast(jObjError.getString("message"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == 404) {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        CommonUtils.showToast(jObjError.getString("message"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void checkListneres() {
        checkMap.put("Monday", "N");
        checkMap.put("Tuesday", "N");
        checkMap.put("Wednesday", "N");
        checkMap.put("Thursday", "N");
        checkMap.put("Friday", "N");
        checkMap.put("Saturday", "N");
        checkMap.put("Sunday", "N");
        monday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkMap.put("Monday", isChecked ? "Y" : "N");

            }
        });
        tuesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkMap.put("Tuesday", isChecked ? "Y" : "N");

            }
        });
        wednesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkMap.put("Wednesday", isChecked ? "Y" : "N");

            }
        });
        thursday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkMap.put("Thursday", isChecked ? "Y" : "N");

            }
        });
        friday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkMap.put("Friday", isChecked ? "Y" : "N");

            }
        });
        saturday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkMap.put("Saturday", isChecked ? "Y" : "N");

            }
        });
        sunday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkMap.put("Sunday", isChecked ? "Y" : "N");

            }
        });
    }

    private void getDataFromServer() {
        wholeLayout.setVisibility(View.VISIBLE);
        UserClient getResponse = AppConfig.getRetrofit().create(UserClient.class);
        Call<AvailableDaysResponse> call = getResponse.doc_available_days(AppConfig.API_USERNAME, AppConfig.API_PASSWORD,
                SharedPrefs.getUserModel().getId()
        );
        call.enqueue(new Callback<AvailableDaysResponse>() {
            @Override
            public void onResponse(Call<AvailableDaysResponse> call, Response<AvailableDaysResponse> response) {
                wholeLayout.setVisibility(View.GONE);
                if (response.code() == 200) {
                    AvailableDaysResponse daysResponse = response.body();
                    if (daysResponse.getData() != null && daysResponse.getData().size() > 0) {
                        List<AvailableDaysModel> list = daysResponse.getData();
                        for (AvailableDaysModel item : list) {
                            if (item.getWeekday().equalsIgnoreCase("Monday") && item.getStatus().equalsIgnoreCase("Y")) {
                                alreadyMap.put("Monday", "Y");
                            }
                            if (item.getWeekday().equalsIgnoreCase("Tuesday") && item.getStatus().equalsIgnoreCase("Y")) {
                                alreadyMap.put("Tuesday", "Y");
                            }
                            if (item.getWeekday().equalsIgnoreCase("Wednesday") && item.getStatus().equalsIgnoreCase("Y")) {
                                alreadyMap.put("Wednesday", "Y");

                            }
                            if (item.getWeekday().equalsIgnoreCase("Thursday") && item.getStatus().equalsIgnoreCase("Y")) {
                                alreadyMap.put("Thursday", "Y");

                            }
                            if (item.getWeekday().equalsIgnoreCase("Friday") && item.getStatus().equalsIgnoreCase("Y")) {
                                alreadyMap.put("Friday", "Y");

                            }
                            if (item.getWeekday().equalsIgnoreCase("Saturday") && item.getStatus().equalsIgnoreCase("Y")) {
                                alreadyMap.put("Saturday", "Y");

                            }
                            if (item.getWeekday().equalsIgnoreCase("Sunday") && item.getStatus().equalsIgnoreCase("Y")) {
                                alreadyMap.put("Sunday", "Y");

                            }

                        }
                        monday.setChecked(alreadyMap.containsKey("Monday") ? true : false);
                        tuesday.setChecked(alreadyMap.containsKey("Tuesday") ? true : false);
                        wednesday.setChecked(alreadyMap.containsKey("Wednesday") ? true : false);
                        thursday.setChecked(alreadyMap.containsKey("Thursday") ? true : false);
                        friday.setChecked(alreadyMap.containsKey("Friday") ? true : false);
                        saturday.setChecked(alreadyMap.containsKey("Saturday") ? true : false);
                        sunday.setChecked(alreadyMap.containsKey("Sunday") ? true : false);


                    }
                }

            }


            @Override
            public void onFailure(Call<AvailableDaysResponse> call, Throwable t) {
                CommonUtils.showToast(t.getMessage());
                wholeLayout.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {


            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}
