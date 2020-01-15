package com.tabibe.app.util;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tabibe.app.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import butterknife.ButterKnife;

public class AddUpdateFragment extends DialogFragment {


    public AddUpdateFragment(){

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.add_udpate_members, container, false);
        ButterKnife.bind(this,view);
        return view;
    }
}
