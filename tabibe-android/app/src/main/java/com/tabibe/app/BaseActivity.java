package com.tabibe.app;


import android.app.ProgressDialog;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    ProgressDialog progressDialog;

    public void showProgressDialog(String message) {
        progressDialog = new ProgressDialog(this, R.style.customDialog);
        progressDialog.setMessage(message);
        progressDialog.setTitle(null);
        progressDialog.show();
        if (progressDialog != null) {
            progressDialog.setCancelable(true);
        }
    }

    public void closeLoadingProgressBar() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }

    }
}
