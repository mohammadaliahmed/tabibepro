package com.tabibepro.app.Activities;

import android.app.TimePickerDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.tabibepro.app.Adapters.ScheduleListAdapter;
import com.tabibepro.app.Models.PatientModel;
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
import java.util.Calendar;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MySchedule extends AppCompatActivity {

    RecyclerView recyclerview;
    ScheduleListAdapter adapter;
    private List<ScheduleModel> scheduleList = new ArrayList<>();
    RelativeLayout wholeLayout;
    LinearLayout listLayout;
    TextView startTimeTv, endTimeTv;
    private int mStartHour;
    private int mStartMinute;
    private String start_AM_PM;
    private int mEndHour;
    private int mEndMinute;
    private String end_AM_PM;
    Button update;
    private String slotDuration = "10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_schedule);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.setTitle("Mes horaires");
        wholeLayout = findViewById(R.id.wholeLayout);
        listLayout = findViewById(R.id.listLayout);
        recyclerview = findViewById(R.id.recyclerview);
        startTimeTv = findViewById(R.id.startTime);
        update = findViewById(R.id.update);
        endTimeTv = findViewById(R.id.endTime);

        startTimeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTImePicker(startTimeTv);
            }
        });
        endTimeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEndTImePicker(endTimeTv);
            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStartHour == 0) {
                    CommonUtils.showToast("Select start time");
                } else if (mEndHour == 0) {
                    CommonUtils.showToast("Select end time");
                } else {
                    sendDataToServer();
                }
            }
        });

        recyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        adapter = new ScheduleListAdapter(this, scheduleList, new ScheduleListAdapter.ScheduleListCallbacks() {
            @Override
            public void onDelete(ScheduleModel model, int position) {
                deleteSlot(model, position);
            }
        });
        recyclerview.setAdapter(adapter);

        getDataFromServer();
        setupPatientSpinner();


    }

    private void sendDataToServer() {
        wholeLayout.setVisibility(View.VISIBLE);
        String startingMnute = "" + mStartMinute;
        if (mStartMinute < 10) {
            startingMnute = "0" + startingMnute;
        }
        String endingMinute = "" + mEndMinute;
        if (mStartMinute < 10) {
            endingMinute = "0" + endingMinute;
        }

        String startTime = mStartHour + ":" + startingMnute + " " + start_AM_PM;
        String endTime = mEndHour + ":" + endingMinute + " " + end_AM_PM;
        UserClient getResponse = AppConfig.getRetrofit().create(UserClient.class);
        Call<ResponseBody> call = getResponse.doctor_slots_add(AppConfig.API_USERNAME, AppConfig.API_PASSWORD,
                SharedPrefs.getUserModel().getId(),
                startTime, endTime, slotDuration
        );
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                wholeLayout.setVisibility(View.GONE);
                if (response.code() == 200) {
                    CommonUtils.showToast(response.message());
                    getDataFromServer();
                } else {
                    CommonUtils.showToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                wholeLayout.setVisibility(View.GONE);
                CommonUtils.showToast(t.getMessage());

            }
        });


    }

    public void showTImePicker(final TextView textView) {
        final Calendar c = Calendar.getInstance();
        mStartHour = c.get(Calendar.HOUR_OF_DAY);
        mStartMinute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        mStartHour = hourOfDay;
                        mStartMinute = minute;
                        if (mStartHour > 12) {
                            mStartHour -= 12;
                            start_AM_PM = "PM";
                        } else if (mStartHour == 0) {
                            mStartHour += 12;
                            start_AM_PM = "AM";
                        } else if (mStartHour == 12) {
                            start_AM_PM = "PM";
                        } else {
                            start_AM_PM = "AM";
                        }

                        textView.setText(mStartHour + ":" + mStartMinute + start_AM_PM);
                    }
                }, mStartHour, mStartMinute, false);
        timePickerDialog.show();
    }

    public void showEndTImePicker(final TextView textView) {
        final Calendar c = Calendar.getInstance();
        mEndHour = c.get(Calendar.HOUR_OF_DAY);
        mEndMinute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        mEndHour = hourOfDay;
                        mEndMinute = minute;
                        if (mEndHour > 12) {
                            mEndHour -= 12;
                            end_AM_PM = "PM";
                        } else if (mStartHour == 0) {
                            mEndHour += 12;
                            end_AM_PM = "AM";
                        } else if (mStartHour == 12) {
                            end_AM_PM = "PM";
                        } else {
                            end_AM_PM = "AM";
                        }
                        textView.setText(mEndHour + ":" + mEndMinute + end_AM_PM);
                    }
                }, mEndHour, mEndMinute, false);
        timePickerDialog.show();
    }

    private void setupPatientSpinner() {
        final List<String> list = new ArrayList<String>();
        list.add("10");
        list.add("15");
        list.add("20");
        list.add("25");
        list.add("30");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                slotDuration = list.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

    private void deleteSlot(ScheduleModel model, final int position) {
        wholeLayout.setVisibility(View.VISIBLE);

        UserClient getResponse = AppConfig.getRetrofit().create(UserClient.class);
        Call<ScheduleListResponse> call = getResponse.delete_doctor_slot(AppConfig.API_USERNAME, AppConfig.API_PASSWORD,
                model.getId());
        call.enqueue(new Callback<ScheduleListResponse>() {
            @Override
            public void onResponse(Call<ScheduleListResponse> call, Response<ScheduleListResponse> response) {
                wholeLayout.setVisibility(View.GONE);
                if (response.code() == 200) {
                    scheduleList.remove(position);

                    adapter.setItemList(scheduleList);
                    CommonUtils.showToast("Delete Successful");


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
