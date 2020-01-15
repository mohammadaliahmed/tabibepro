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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tabibepro.app.Adapters.ScheduleListAdapter;
import com.tabibepro.app.Models.ScheduleModel;
import com.tabibepro.app.Models.UserModel;
import com.tabibepro.app.NetworkManager.AppConfig;
import com.tabibepro.app.NetworkManager.UserClient;
import com.tabibepro.app.NetworkResponses.LoginResponse;
import com.tabibepro.app.NetworkResponses.ScheduleListResponse;
import com.tabibepro.app.R;
import com.tabibepro.app.Utils.CommonUtils;
import com.tabibepro.app.Utils.SharedPrefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MySchedule extends AppCompatActivity {

    RecyclerView recyclerview;
    ScheduleListAdapter adapter;
    private List<ScheduleModel> scheduleList = new ArrayList<>();
    RelativeLayout wholeLayout;
    LinearLayout listLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_schedule);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.setTitle("My Schedule");
        wholeLayout = findViewById(R.id.wholeLayout);
        listLayout = findViewById(R.id.listLayout);
        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        adapter = new ScheduleListAdapter(this, scheduleList, new ScheduleListAdapter.ScheduleListCallbacks() {
            @Override
            public void onDelete(ScheduleModel model) {

            }
        });
        recyclerview.setAdapter(adapter);

        getDataFromServer();
    }

    private void getDataFromServer() {
        wholeLayout.setVisibility(View.VISIBLE);
        UserClient getResponse = AppConfig.getRetrofit().create(UserClient.class);
        Call<ScheduleListResponse> call = getResponse.doctor_slots_listing(AppConfig.API_USERNAME, AppConfig.API_PASSWORD,
                SharedPrefs.getUserModel().getId());
        call.enqueue(new Callback<ScheduleListResponse>() {
            @Override
            public void onResponse(Call<ScheduleListResponse> call, Response<ScheduleListResponse> response) {
                wholeLayout.setVisibility(View.GONE);
                if (response.code() == 200) {
                    if (response.body().getData() != null) {
                        List<ScheduleModel> list = response.body().getData();
                        if (list.size() > 0) {
                            listLayout.setVisibility(View.VISIBLE);
                            scheduleList = list;
                            adapter.setItemList(list);
                        }
                    } else {
                    }
                } else {
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
            public void onFailure(Call<ScheduleListResponse> call, Throwable t) {

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
