package com.tabibe.app.fragment;


import android.app.ProgressDialog;

import androidx.fragment.app.Fragment;

import com.tabibe.app.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {


    ProgressDialog progressDialog;

    public void showProgressDialog(String message) {
        progressDialog = new ProgressDialog(getActivity(), R.style.customDialog);
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
