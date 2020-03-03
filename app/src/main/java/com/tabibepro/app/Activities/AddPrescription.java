package com.tabibepro.app.Activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.tabibepro.app.Activities.UserManagement.Login;
import com.tabibepro.app.Adapters.PrescriptionListAdapter;
import com.tabibepro.app.Models.PrescriptionModel;
import com.tabibepro.app.NetworkManager.AppConfig;
import com.tabibepro.app.NetworkManager.UserClient;
import com.tabibepro.app.NetworkResponses.DoctorDaysResponse;
import com.tabibepro.app.NetworkResponses.PrescriptionResponse;
import com.tabibepro.app.R;
import com.tabibepro.app.Utils.CommonUtils;
import com.tabibepro.app.Utils.CompressImage;
import com.tabibepro.app.Utils.DownloadFile;
import com.tabibepro.app.Utils.GifSizeFilter;
import com.tabibepro.app.Utils.Glide4Engine;
import com.tabibepro.app.Utils.SharedPrefs;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.filter.Filter;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPrescription extends AppCompatActivity {


    ImageView pickImage;
    Button submit;
    EditText description;
    RecyclerView recyclerview;
    PrescriptionListAdapter adapter;
    private List<PrescriptionModel> itemList = new ArrayList<>();
    String appointmentId;
    private static final int REQUEST_CODE_CHOOSE = 23;
    private List<Uri> mSelected = new ArrayList<>();
    private String encodedImage;

    RelativeLayout wholeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_prescription);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.setTitle("Add prescription");
        getPermissions();

        appointmentId = getIntent().getStringExtra("appointmentId");
        pickImage = findViewById(R.id.pickImage);
        wholeLayout = findViewById(R.id.wholeLayout);
        submit = findViewById(R.id.submit);
        description = findViewById(R.id.description);
        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        adapter = new PrescriptionListAdapter(this, itemList, new PrescriptionListAdapter.PrescriptionListCallbacks() {
            @Override
            public void onDelete(PrescriptionModel model, int position) {
                showAlert(model, position);
            }

            @Override
            public void onDownload(PrescriptionModel model, int position) {
                DownloadFile.fromUrl(model.getImageUrl(), System.currentTimeMillis() + ".jpg");
            }
        });
        recyclerview.setAdapter(adapter);

        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initMatisse();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (description.getText().length() == 0) {
                    description.setError("Enter description");
                } else {
                    uploadPrescription();
                }
            }
        });

        getDataFromServer();
    }

    private void showAlert(final PrescriptionModel model, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("Do you want to delete this prescription? ");

        // add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deletePrescription(model, position);

            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deletePrescription(PrescriptionModel model, final int position) {
        UserClient getResponse = AppConfig.getRetrofit().create(UserClient.class);
        Call<PrescriptionResponse> call = getResponse.remove_prescription(AppConfig.API_USERNAME, AppConfig.API_PASSWORD,
                model.getId());
        call.enqueue(new Callback<PrescriptionResponse>() {
            @Override
            public void onResponse(Call<PrescriptionResponse> call, Response<PrescriptionResponse> response) {
                if (response.code() == 200) {
                    CommonUtils.showToast("Prescription removed");
                    itemList.remove(position);
                    adapter.setItemList(itemList);
                } else {
                    CommonUtils.showToast("Error");
                }
            }

            @Override
            public void onFailure(Call<PrescriptionResponse> call, Throwable t) {

            }
        });

    }

    private void uploadPrescription() {
        wholeLayout.setVisibility(View.VISIBLE);
        UserClient getResponse = AppConfig.getRetrofit().create(UserClient.class);
        Call<PrescriptionResponse> call = getResponse.add_prescription(AppConfig.API_USERNAME, AppConfig.API_PASSWORD,
                appointmentId, description.getText().toString(), encodedImage);
        call.enqueue(new Callback<PrescriptionResponse>() {
            @Override
            public void onResponse(Call<PrescriptionResponse> call, Response<PrescriptionResponse> response) {
                wholeLayout.setVisibility(View.GONE);
                if (response.code() == 200) {
                    description.setText("");
                    Glide.with(AddPrescription.this).load(R.drawable.ic_menu_gallery).into(pickImage);
                    CommonUtils.showToast("Prescription added");
                    getDataFromServer();
                } else {
                    CommonUtils.showToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<PrescriptionResponse> call, Throwable t) {
                wholeLayout.setVisibility(View.GONE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);

            mSelected = Matisse.obtainResult(data);
            Glide.with(AddPrescription.this).load(mSelected.get(0)).into(pickImage);
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

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    private void initMatisse() {
        Matisse.from(this)
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

    private void getDataFromServer() {
        wholeLayout.setVisibility(View.VISIBLE);
        UserClient getResponse = AppConfig.getRetrofit().create(UserClient.class);
        Call<PrescriptionResponse> call = getResponse.get_prescriotion_by_appointment(AppConfig.API_USERNAME, AppConfig.API_PASSWORD,
                appointmentId);

        call.enqueue(new Callback<PrescriptionResponse>() {
            @Override
            public void onResponse(Call<PrescriptionResponse> call, Response<PrescriptionResponse> response) {
                wholeLayout.setVisibility(View.GONE);
                if (response.code() == 200) {
                    itemList = response.body().getPrescriptionList();
                    if (itemList != null && itemList.size() > 0) {
                        adapter.setItemList(itemList);
                    }
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        CommonUtils.showToast(jObjError.getString("message"));
                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onFailure(Call<PrescriptionResponse> call, Throwable t) {
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

    private void getPermissions() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,


        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        } else {
        }
    }


    public boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                } else {

                }
            }
        }
        return true;
    }
}
