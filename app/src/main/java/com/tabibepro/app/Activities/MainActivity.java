package com.tabibepro.app.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.tabibepro.app.Activities.UserManagement.Profile;
import com.tabibepro.app.Adapters.AppointmentListAdapter;
import com.tabibepro.app.Models.AppointmentsModel;
import com.tabibepro.app.Models.UserModel;
import com.tabibepro.app.NetworkManager.AppConfig;
import com.tabibepro.app.NetworkManager.UserClient;
import com.tabibepro.app.NetworkResponses.AppointmentsResponse;
import com.tabibepro.app.NetworkResponses.LoginResponse;
import com.tabibepro.app.R;
import com.tabibepro.app.Utils.CommonUtils;
import com.tabibepro.app.Utils.SharedPrefs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    Button addAppointment;
    CalendarView calender;
    RecyclerView recyclerview;
    ProgressBar progress;
    TextView noAppintments;
    AppointmentListAdapter adapter;
    private List<AppointmentsModel> itemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        addAppointment = findViewById(R.id.addAppointment);
        calender = findViewById(R.id.calender);
        noAppintments = findViewById(R.id.noAppintments);
        recyclerview = findViewById(R.id.recyclerview);
        progress = findViewById(R.id.progress);

        addAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddAppointment.class));
            }
        });

        initDrawer();

        adapter = new AppointmentListAdapter(this, itemList, new AppointmentListAdapter.AppointmentListCallbacks() {
            @Override
            public void onHistory(AppointmentsModel model) {

            }

            @Override
            public void onOrdinance(AppointmentsModel model) {
                Intent i =new Intent(MainActivity.this,AddPrescription.class);
                i.putExtra("appointmentId",model.getAppointmentId());
                startActivity(i);
            }

            @Override
            public void onDelete(AppointmentsModel model, int position) {
                showDeleteAlert(model, position);

            }
        });
        recyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerview.setAdapter(adapter);


        getListOfAppointments(CommonUtils.getDate(System.currentTimeMillis()));
        calender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = year + "-" + (month + 1) + "-" + dayOfMonth;
                getListOfAppointments(date);
            }
        });

    }

    private void showDeleteAlert(final AppointmentsModel model, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("Do you want to delete this appointment? ");

        // add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteAppointmentApi(model, position);

            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteAppointmentApi(AppointmentsModel model, final int position) {
        UserClient getResponse = AppConfig.getRetrofit().create(UserClient.class);
        Call<ResponseBody> call = getResponse.delete_appointment(AppConfig.API_USERNAME, AppConfig.API_PASSWORD,
                model.getAppointmentId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    itemList.remove(position);
                    adapter.setItemList(itemList);
                    CommonUtils.showToast("Deleted");
//                    try {
//                        JSONObject jObjError = new JSONObject(response.body().string());
//                        CommonUtils.showToast(jObjError.getString("message"));
//                    } catch (Exception e) {
//
//                    }

                } else {
                    itemList.remove(position);
                    adapter.setItemList(itemList);
                    CommonUtils.showToast("Deleted");
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
//                        CommonUtils.showToast(jObjError.getString("message"));
                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }


    private void getListOfAppointments(String date) {
        itemList.clear();
        adapter.setItemList(itemList);
        progress.setVisibility(View.VISIBLE);
        UserClient getResponse = AppConfig.getRetrofit().create(UserClient.class);
        Call<AppointmentsResponse> call = getResponse.doctor_appointment_by_date(AppConfig.API_USERNAME, AppConfig.API_PASSWORD,
                SharedPrefs.getUserModel().getId(), date);

        call.enqueue(new Callback<AppointmentsResponse>() {
            @Override
            public void onResponse(Call<AppointmentsResponse> call, Response<AppointmentsResponse> response) {
                progress.setVisibility(View.GONE);
                if (response.code() == 401) {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        CommonUtils.showToast(jObjError.getString("message"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else if (response.code() == 404) {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        CommonUtils.showToast(jObjError.getString("message"));
                        noAppintments.setVisibility(View.VISIBLE);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else if (response.code() == 200) {
                    if (response.body() != null && response.body().getData() != null) {
                        itemList = response.body().getData();
                        adapter.setItemList(itemList);
                        noAppintments.setVisibility(View.GONE);

                    }
                }

            }

            @Override
            public void onFailure(Call<AppointmentsResponse> call, Throwable t) {
                CommonUtils.showToast(t.getMessage());
                progress.setVisibility(View.GONE);

            }
        });


//        call.enqueue(new Callback<LoginResponse>() {
//            @Override
//            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
//                progress.setVisibility(View.GONE);
//                if (response.code() == 401) {
//                    try {
//                        JSONObject jObjError = new JSONObject(response.errorBody().string());
//                        CommonUtils.showToast(jObjError.getString("message"));
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                } else if (response.code() == 404) {
//                    try {
//                        JSONObject jObjError = new JSONObject(response.errorBody().string());
//                        CommonUtils.showToast(jObjError.getString("message"));
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                } else if (response.code() == 200) {
//                    if (response.body() != null && response.body().getData() != null) {
//                        if (response.body().getData().size() > 0) {
//                            CommonUtils.showToast(response.body().getMessage());
//                            UserModel model = response.body().getData().get(0);
//                            if (model != null) {
//                                progress.setVisibility(View.GONE);
//                                SharedPrefs.setUserModel(model);
//                                launchHomeScreen();
//                            }
//
//                        } else {
//                            progress.setVisibility(View.GONE);
//                            CommonUtils.showToast("Wrong email or password");
//                        }
//                    }
//                }

//            }


//            @Override
//            public void onFailure(Call call, Throwable t) {
//                CommonUtils.showToast(t.getMessage());
//                wholeLayout.setVisibility(View.GONE);
//
//            }
//        });
    }

    private void initDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);

        CircleImageView image = headerView.findViewById(R.id.imageView);
        TextView navUsername = (TextView) headerView.findViewById(R.id.name_drawer);
        TextView navSubtitle = (TextView) headerView.findViewById(R.id.subtitle_drawer);
        if (SharedPrefs.getUserModel() != null) {
//            if (SharedPrefs.getUserModel().getImage() != null) {
//                Glide.with(MainActivity.this).load(AppConfig.BASE_URL_Image + SharedPrefs.getUserModel().get()).into(image);
//            } else {
            Glide.with(MainActivity.this).load(R.drawable.logo).into(image);
//
//            }

            navUsername.setText("Welcome, " + SharedPrefs.getUserModel().getFirstName());
//            navSubtitle.setText(SharedPrefs.getUserModel().getPhoneNumber());
        }
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(MainActivity.this, Profile.class));

        } else if (id == R.id.nav_availabilities) {
            startActivity(new Intent(MainActivity.this, MyAvailabilities.class));

        } else if (id == R.id.nav_schedule) {
            startActivity(new Intent(MainActivity.this, MySchedule.class));

        } else if (id == R.id.nav_appointments) {


        } else if (id == R.id.nav_signuout) {
            SharedPrefs.logout();
            Intent i = new Intent(MainActivity.this, Splash.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
