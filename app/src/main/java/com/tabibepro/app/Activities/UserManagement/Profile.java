package com.tabibepro.app.Activities.UserManagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tabibepro.app.Activities.AddPrescription;
import com.tabibepro.app.Models.AllCity;
import com.tabibepro.app.Models.AllConsultationReason;
import com.tabibepro.app.Models.AllSpeciality;
import com.tabibepro.app.Models.DoctorDataModel;
import com.tabibepro.app.Models.SelectedConsultationReason;
import com.tabibepro.app.Models.UserModel;
import com.tabibepro.app.NetworkManager.AppConfig;
import com.tabibepro.app.NetworkManager.UserClient;
import com.tabibepro.app.NetworkResponses.DoctorProfileResponse;
import com.tabibepro.app.R;
import com.tabibepro.app.Utils.CommonUtils;
import com.tabibepro.app.Utils.GifSizeFilter;
import com.tabibepro.app.Utils.Glide4Engine;
import com.tabibepro.app.Utils.SharedPrefs;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.filter.Filter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile extends AppCompatActivity {


    TextView specialitiesSpinner, consultationSpinner;
    EditText firstName, lastName, email, password, confirmPassword, phone, fee, feeUnit, method,
            clinicAddress, medicalPost, trainings, associations, experiences, work, spokenLanguage;
    RadioButton male, female;
    String gender = "Male";
    private List<AllSpeciality> specialitiesList = new ArrayList<>();
    private HashMap<String, String> specialitiesListMao = new HashMap<>();
    private HashMap<String, String> consulListMao = new HashMap<>();
    private String specialityId;
    private List<AllCity> citiesList = new ArrayList<>();
    private List<AllConsultationReason> consultationList = new ArrayList<>();
    private String cityId;
    private String consultationId;
    UserModel model;
    private HashMap<String, String> consultationSelectedMap = new HashMap<>();
    //    private HashMap<String, String> specialitiesSelectedMap = new HashMap<>();
    Button update;

    private static final int REQUEST_CODE_CHOOSE = 23;
    private List<Uri> mSelected = new ArrayList<>();
    private String encodedImage;
    CircleImageView profilePicture;
    RelativeLayout wholeLayout;
    private int speciWhich;
    private ArrayList<String> selectedConsultation = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.setTitle("Mon profil");
        initUi();
        getDataFromServer();
    }

    private void initUi() {
        wholeLayout = findViewById(R.id.wholeLayout);
        profilePicture = findViewById(R.id.profilePicture);
        update = findViewById(R.id.update);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        phone = findViewById(R.id.phone);
        fee = findViewById(R.id.fee);
        feeUnit = findViewById(R.id.feeUnit);
        method = findViewById(R.id.method);
        clinicAddress = findViewById(R.id.clinicAddress);
        medicalPost = findViewById(R.id.medicalPost);
        trainings = findViewById(R.id.trainings);
        associations = findViewById(R.id.associations);
        experiences = findViewById(R.id.experiences);
        work = findViewById(R.id.work);
        spokenLanguage = findViewById(R.id.spokenLanguage);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);

        consultationSpinner = findViewById(R.id.consultationSpinner);
        specialitiesSpinner = findViewById(R.id.specialitiesSpinner);


        consultationSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupConsultationSpinner();
            }
        });
        specialitiesSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSpecialitiesSpinner();
            }
        });


        initradio();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getText().length() > 0 && password.getText().length() < 7) {
                    password.setError("Enter 7 or more characters");
                } else {

                    if (!confirmPassword.getText().toString().equals(password.getText().toString())) {
                        confirmPassword.setError("Password Do not match");
                    } else {

                        callApi();

                    }
                    callApi();
                }
            }
        });

        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matisse.from(Profile.this)
                        .choose(MimeType.ofImage())
                        .countable(true)
                        .maxSelectable(1)
                        .showSingleMediaType(true)
                        .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new Glide4Engine())
                        .forResult(REQUEST_CODE_CHOOSE);
            }
        });

    }

    private void callApi() {
        wholeLayout.setVisibility(View.VISIBLE);
        List<String> consulationList = new ArrayList<>(consultationSelectedMap.keySet());
        String conslist = CommonUtils.commaSeparated(consulationList);


        UserClient getResponse = AppConfig.getRetrofit().create(UserClient.class);
        Call<DoctorProfileResponse> call = getResponse.update_doc_profile(
                AppConfig.API_USERNAME,
                AppConfig.API_PASSWORD,
                SharedPrefs.getUserModel().getId(),
                conslist,
                cityId,
                specialityId,
                firstName.getText().toString(),
                lastName.getText().toString(),
                email.getText().toString(),
                password.getText().toString().equals("") ? null : password.getText().toString(),
                "",
                clinicAddress.getText().toString(),
                phone.getText().toString(),
                gender,
                medicalPost.getText().toString(),
                "0.000",
                "0.000",
                trainings.getText().toString(),
                associations.getText().toString(),
                work.getText().toString(),
                spokenLanguage.getText().toString(),
                experiences.getText().toString(),
                fee.getText().toString(),
                feeUnit.getText().toString(),
                method.getText().toString(),
                encodedImage,
                "1"

        );
        call.enqueue(new Callback<DoctorProfileResponse>() {
            @Override
            public void onResponse(Call<DoctorProfileResponse> call, Response<DoctorProfileResponse> response) {
                wholeLayout.setVisibility(View.GONE);
                if (response.code() == 200) {
                    CommonUtils.showToast("Profile updated");
                    finish();
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
            public void onFailure(Call<DoctorProfileResponse> call, Throwable t) {
                wholeLayout.setVisibility(View.GONE);
                CommonUtils.showToast(t.getMessage());
            }
        });

    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    private void initradio() {
        male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    if (isChecked) {
                        gender = "Male";
                    }
                }
            }
        });
        female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    if (isChecked) {
                        gender = "Female";
                    }
                }
            }
        });
    }

    private void getDataFromServer() {
        wholeLayout.setVisibility(View.VISIBLE);
        UserClient getResponse = AppConfig.getRetrofit().create(UserClient.class);
        Call<DoctorProfileResponse> call = getResponse.get_doctor_update_profile_data(AppConfig.API_USERNAME, AppConfig.API_PASSWORD,
                SharedPrefs.getUserModel().getId());
        call.enqueue(new Callback<DoctorProfileResponse>() {
            @Override
            public void onResponse(Call<DoctorProfileResponse> call, Response<DoctorProfileResponse> response) {
                wholeLayout.setVisibility(View.GONE);
                if (response.code() == 200) {
                    DoctorDataModel object = response.body().getDoctorData();
                    if (object.getAllSpecialities() != null) {
                        specialitiesList = object.getAllSpecialities();
                        for (AllSpeciality abc : specialitiesList) {
                            specialitiesListMao.put(abc.getId(), abc.getName());
                        }
//                        setSpecialitiesSpinner();
                    }


                    if (object.getAllConsultationReasons() != null) {
                        consultationList = object.getAllConsultationReasons();

//                        setupConsultationSpinner();
                        for (AllConsultationReason item : consultationList) {
                            consulListMao.put(item.getId(), item.getName());

                        }
                        for (SelectedConsultationReason item : object.getSelectedConsultationReasons()) {
                            selectedConsultation.add(item.getConsultationReasonId());
                            consultationSelectedMap.put(item.getConsultationReasonId(), consulListMao.get(item.getConsultationReasonId()));


                        }

                    }
                    model = object.getDoctor();
                    if (model != null) {
                        if (object.getAllCities() != null) {
                            citiesList = object.getAllCities();
                            setupCitiesSpinner();
                        }
                        firstName.setText(model.getFirstName());
                        lastName.setText(model.getLastName());
                        email.setText(model.getEmailAddress());
                        phone.setText(model.getPhoneNumber());

                        cityId = model.getCityId();
                        specialityId = model.getSpecialityId();
                        specialitiesSpinner.setText(specialitiesListMao.get(specialityId));
                        List<String> cons = new ArrayList<>();

                        for (String abc : selectedConsultation) {
                            cons.add(consulListMao.get(abc));
                        }
                        consultationSpinner.setText("Consultation: " + CommonUtils.commaSeparated(cons));
                        if (model.getGender().equalsIgnoreCase("Male")) {
                            male.setChecked(true);
                        } else {
                            female.setChecked(true);
                        }
                        fee.setText(model.getFee());
                        feeUnit.setText(model.getFeeUnit());
                        method.setText(model.getPaymentModes());
                        clinicAddress.setText(model.getClinicAddress());
                        medicalPost.setText(model.getMedicalPost());
                        trainings.setText(model.getTrainings());
                        associations.setText(model.getAssociations());
                        experiences.setText(model.getExperiences());
                        work.setText(model.getWorksPublications());
                        spokenLanguage.setText(model.getSpokenLanguages());
                        try {
                            Glide.with(Profile.this).load(AppConfig.BASE_URL_Image + model.getImagePath()).into(profilePicture);

                        } catch (Exception e) {

                        }
                    }


                } else {
                    CommonUtils.showToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<DoctorProfileResponse> call, Throwable t) {
                CommonUtils.showToast(t.getMessage());
                wholeLayout.setVisibility(View.GONE);
            }
        });

    }

    private void setupConsultationSpinner() {
        final List<String> list = new ArrayList<String>();
        for (AllConsultationReason model : consultationList) {
            list.add(model.getName());

        }
        final CharSequence[] dialogList = list.toArray(new CharSequence[list.size()]);
        final android.app.AlertDialog.Builder builderDialog = new android.app.AlertDialog.Builder(this);
        builderDialog.setTitle("Select Consultation");
        int count = dialogList.length;
        boolean[] is_checked = new boolean[count];

        int abc = 0;

        for (AllConsultationReason model : consultationList) {
            if (selectedConsultation.contains(model.getId())) {
                is_checked[abc] = true;
            } else {
                is_checked[abc] = false;
            }
            abc++;

        }


        // Creating multiple selection by using setMutliChoiceItem method
        builderDialog.setMultiChoiceItems(dialogList, is_checked,
                new DialogInterface.OnMultiChoiceClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton, boolean isChecked) {
                        if (isChecked) {
                            consultationSelectedMap.put(consultationList.get(whichButton).getId(), consultationList.get(whichButton).getName());
                        } else {
                            consultationSelectedMap.remove(consultationList.get(whichButton).getId());
                        }
                    }
                });

        builderDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //ListView has boolean array like {1=true, 3=true}, that shows checked items
                        List<String> asda = new ArrayList<>(consultationSelectedMap.values());
                        consultationSpinner.setText(CommonUtils.commaSeparated(asda));
                    }
                });

        builderDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        android.app.AlertDialog alert = builderDialog.create();
        alert.show();
    }

    private void setupCitiesSpinner() {
        final List<String> list = new ArrayList<String>();
        list.add("Select City");
        int count = 1;
        boolean found = false;
        for (AllCity cmodel : citiesList) {
            list.add(cmodel.getName());

            if (model.getCityId().equalsIgnoreCase(cmodel.getId())) {
                found = true;
            } else {
                if (!found) {
                    count++;
                }

            }
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = findViewById(R.id.citySpinner);
        spinner.setAdapter(dataAdapter);
        if (found) {
            spinner.setSelection(count);
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {

                } else {
                    cityId = citiesList.get(position - 1).getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setSpecialitiesSpinner() {
        final List<String> list = new ArrayList<String>();
        int count = 0;
        boolean found = false;
        for (AllSpeciality smodel : specialitiesList) {
            list.add(smodel.getName());
            if (model.getSpecialityId().equalsIgnoreCase(smodel.getId())) {
                found = true;
            } else {
                if (!found) {
                    count++;
                }
            }
        }
        final CharSequence[] dialogList = list.toArray(new CharSequence[list.size()]);
        final android.app.AlertDialog.Builder builderDialog = new android.app.AlertDialog.Builder(this);
        builderDialog.setTitle("Select Speciality");

        // Creating multiple selection by using setMutliChoiceItem method
        builderDialog.setSingleChoiceItems(dialogList, count, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                speciWhich = which;
                specialityId = specialitiesList.get(which).getId();
//                specialitiesSelectedMap.put(specialitiesList.get(which).getId(),
//                        specialitiesList.get(which).getName());
            }
        });
        builderDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ListView list = ((android.app.AlertDialog) dialog).getListView();
                        specialitiesSpinner.setText(specialitiesList.get(speciWhich).getName());
                        //ListView has boolean array like {1=true, 3=true}, that shows checked items
                    }
                });

        builderDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        android.app.AlertDialog alert = builderDialog.create();
        alert.show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);

            mSelected = Matisse.obtainResult(data);
            Glide.with(Profile.this).load(mSelected.get(0)).into(profilePicture);
            final InputStream imageStream;
            try {
                imageStream = getContentResolver().openInputStream(mSelected.get(0));

                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                encodedImage = encodeImage(selectedImage);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
