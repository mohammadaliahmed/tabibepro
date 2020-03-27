package com.tabibepro.app.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.tabibepro.app.Models.ConsultationReasonModel;
import com.tabibepro.app.Models.DoctorDaysModel;
import com.tabibepro.app.Models.PatientModel;
import com.tabibepro.app.Models.ScheduleModel;
import com.tabibepro.app.NetworkManager.AppConfig;
import com.tabibepro.app.NetworkManager.UserClient;
import com.tabibepro.app.NetworkResponses.ConsultationReasonResponse;
import com.tabibepro.app.NetworkResponses.DoctorDaysResponse;
import com.tabibepro.app.NetworkResponses.PatientsListResponse;
import com.tabibepro.app.NetworkResponses.ScheduleListResponse;
import com.tabibepro.app.R;
import com.tabibepro.app.Utils.CommonUtils;
import com.tabibepro.app.Utils.SharedPrefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAppointment extends AppCompatActivity {


    List<PatientModel> patientList = new ArrayList<>();
    private List<DoctorDaysModel> daysList = new ArrayList<>();
    private List<ScheduleModel> slotsList = new ArrayList<>();
    private List<ConsultationReasonModel> consultationList = new ArrayList<>();
    CheckBox patientCheckBox;
    LinearLayout addPatientLayout, alreadyPatientLayout;
    EditText firstName, lastName, phone;
    Button addAppointment;
    EditText description;
    private PatientModel patient;
    private ConsultationReasonModel consultationReasonModel;
    private String slotId;
    private String dateSeleced;
    boolean newPatient;
    RelativeLayout wholeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.setTitle("Add Appointment");
        this.setTitle("Ajouter un rendez-vous");


        initUi();

        getPatientsDataFromServer();
        getDoctorsDaysFromServer();
        getConsultationsFromServer();


    }

    private void initUi() {
        wholeLayout = findViewById(R.id.wholeLayout);
        patientCheckBox = findViewById(R.id.patientCheckBox);
        addPatientLayout = findViewById(R.id.addPatientLayout);
        alreadyPatientLayout = findViewById(R.id.alreadyPatientLayout);
        firstName = findViewById(R.id.firstName);
        addAppointment = findViewById(R.id.addAppointment);
        lastName = findViewById(R.id.lastName);
        phone = findViewById(R.id.phone);
        description = findViewById(R.id.description);

        firstName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    lastName.requestFocus();
                    return true;
                }
                return false;
            }
        });
        lastName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    phone.requestFocus();
                    return true;
                }
                return false;
            }
        });


        patientCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    if (isChecked) {
                        newPatient = true;
                        addPatientLayout.setVisibility(View.VISIBLE);
                        alreadyPatientLayout.setVisibility(View.GONE);
                    } else {
                        newPatient = false;
                        addPatientLayout.setVisibility(View.GONE);
                        alreadyPatientLayout.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        addAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dateSeleced == null) {
                    CommonUtils.showToast("Please select date");
                } else if (slotId == null) {
                    CommonUtils.showToast("Please select slot id");
                } else {
                    if (newPatient) {
                        if (firstName.getText().length() == 0) {
                            firstName.setError("Enter first name");
                        } else if (lastName.getText().length() == 0) {
                            lastName.setError("Enter last name");
                        } else if (phone.getText().length() == 0) {
                            phone.setError("Enter phone");
                        } else {
                            submitAppointment();
                        }
                    } else {
                        submitAppointment();

                    }
                }
            }
        });
    }

    private void submitAppointment() {
        wholeLayout.setVisibility(View.VISIBLE);
        UserClient getResponse = AppConfig.getRetrofit().create(UserClient.class);

        Call<PatientsListResponse> call;
        if (newPatient) {
            call = getResponse.add_appointment_new_patient(
                    AppConfig.API_USERNAME,
                    AppConfig.API_PASSWORD,
                    description.getText().toString(),
                    SharedPrefs.getUserModel().getId(),
                    consultationReasonModel.getConsultationReasonId(),
                    dateSeleced,
                    slotId,
                    firstName.getText().toString(),
                    lastName.getText().toString(),
                    phone.getText().toString(),
                    "Y"


            );
        } else {

            call = getResponse.add_appointment(
                    AppConfig.API_USERNAME,
                    AppConfig.API_PASSWORD,
                    description.getText().toString(),
                    SharedPrefs.getUserModel().getId(),
                    patient.getId(),
                    consultationReasonModel.getConsultationReasonId(),
                    dateSeleced
                    , slotId


            );
        }

        call.enqueue(new Callback<PatientsListResponse>() {
            @Override
            public void onResponse(Call<PatientsListResponse> call, Response<PatientsListResponse> response) {
                wholeLayout.setVisibility(View.GONE);
                if (response.code() == 200) {
//                    CommonUtils.showToast(response.message());
                    CommonUtils.showToast("Appointment Added");
                    finish();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        CommonUtils.showToast(jObjError.getString("message"));
                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onFailure(Call<PatientsListResponse> call, Throwable t) {
                CommonUtils.showToast(t.getMessage());
                wholeLayout.setVisibility(View.GONE);
            }
        });
    }


    private void getPatientsDataFromServer() {
        UserClient getResponse = AppConfig.getRetrofit().create(UserClient.class);
        Call<PatientsListResponse> call = getResponse.patient_listing(AppConfig.API_USERNAME, AppConfig.API_PASSWORD);
        call.enqueue(new Callback<PatientsListResponse>() {
            @Override
            public void onResponse(Call<PatientsListResponse> call, Response<PatientsListResponse> response) {
                if (response.code() == 200) {
                    PatientsListResponse object = response.body();
                    if (object != null && object.getData() != null && object.getData().size() > 0) {
                        patientList = object.getData();
                        Collections.sort(patientList, new Comparator<PatientModel>() {
                            @Override
                            public int compare(PatientModel listData, PatientModel t1) {
                                String ob1 = listData.getFirstName();
                                String ob2 = t1.getFirstName();
                                return ob2.compareTo(ob1);

                            }
                        });

                        setupPatientSpinner();
                    }
                }
            }

            @Override
            public void onFailure(Call<PatientsListResponse> call, Throwable t) {

            }
        });

    }

    private void setupDaysSpinner() {
        final List<String> list = new ArrayList<String>();
        list.add("Select Day");
        for (DoctorDaysModel model : daysList) {
            list.add(model.getSelectedDate());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = findViewById(R.id.days);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {

                } else {
                    dateSeleced = daysList.get(position - 1).getSelectedDate();
                    getDoctorsDaysSlotsFromServer(daysList.get(position - 1).getSelectedDate());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getDoctorsDaysFromServer() {
        UserClient getResponse = AppConfig.getRetrofit().create(UserClient.class);
        Call<DoctorDaysResponse> call = getResponse.doctor_days(AppConfig.API_USERNAME, AppConfig.API_PASSWORD, SharedPrefs.getUserModel().getId());
        call.enqueue(new Callback<DoctorDaysResponse>() {
            @Override
            public void onResponse(Call<DoctorDaysResponse> call, Response<DoctorDaysResponse> response) {
                if (response.code() == 200) {
                    DoctorDaysResponse object = response.body();
                    if (object != null && object.getData() != null && object.getData().size() > 0) {
                        daysList = object.getData();
                        setupDaysSpinner();
                    }
                }
            }

            @Override
            public void onFailure(Call<DoctorDaysResponse> call, Throwable t) {

            }
        });

    }

    private void setupPatientSpinner() {
        final List<String> list = new ArrayList<String>();
        list.add("Select Patient");
        for (PatientModel model : patientList) {
            list.add(model.getFirstName() + " " + model.getLastName());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = findViewById(R.id.patientSpinner);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {

                } else {
                    patient = patientList.get(position - 1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getDoctorsDaysSlotsFromServer(String date) {
        UserClient getResponse = AppConfig.getRetrofit().create(UserClient.class);
        Call<ScheduleListResponse> call = getResponse.doctor_date_shots(AppConfig.API_USERNAME, AppConfig.API_PASSWORD,
                SharedPrefs.getUserModel().getId(), date);
        call.enqueue(new Callback<ScheduleListResponse>() {
            @Override
            public void onResponse(Call<ScheduleListResponse> call, Response<ScheduleListResponse> response) {
                if (response.code() == 200) {
                    ScheduleListResponse object = response.body();
                    if (object != null && object.getData() != null && object.getData().size() > 0) {
//                        slotsList = object.getData();
                        slotsList.clear();
                        List<ScheduleModel> list = object.getData();
                        for(ScheduleModel model:list){
                            if(model.getIs_free().equalsIgnoreCase("y")){
                                slotsList.add(model);
                            }
                        }
                        setupDOctorSlotsSpinner();
                    }
                }
            }

            @Override
            public void onFailure(Call<ScheduleListResponse> call, Throwable t) {

            }
        });

    }

    private void setupDOctorSlotsSpinner() {
        final List<String> list = new ArrayList<String>();
        list.add("Select Slot");
        for (ScheduleModel model : slotsList) {
            list.add(model.getStartTime() + " " + model.getEndTime());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = findViewById(R.id.slotSpinner);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {

                } else {
                    slotId = slotsList.get(position - 1).getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getConsultationsFromServer() {
        UserClient getResponse = AppConfig.getRetrofit().create(UserClient.class);
        Call<ConsultationReasonResponse> call = getResponse.consultation_reason(AppConfig.API_USERNAME, AppConfig.API_PASSWORD,
                SharedPrefs.getUserModel().getId());
        call.enqueue(new Callback<ConsultationReasonResponse>() {
            @Override
            public void onResponse(Call<ConsultationReasonResponse> call, Response<ConsultationReasonResponse> response) {
                if (response.code() == 200) {
                    ConsultationReasonResponse object = response.body();
                    if (object != null && object.getData() != null && object.getData().size() > 0) {
                        consultationList = object.getData();
                        setupConsultationSpinner();

                    }
                }
            }

            @Override
            public void onFailure(Call<ConsultationReasonResponse> call, Throwable t) {

            }
        });

    }

    private void setupConsultationSpinner() {
        final List<String> list = new ArrayList<String>();
        list.add("Select Reason");
        for (ConsultationReasonModel model : consultationList) {
            list.add(model.getName() + " " + model.getName());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = findViewById(R.id.consultationSpinner);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {

                } else {
                    consultationReasonModel = consultationList.get(position - 1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
