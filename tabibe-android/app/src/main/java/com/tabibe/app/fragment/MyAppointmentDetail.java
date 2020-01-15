package com.tabibe.app.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tabibe.app.MainActivity;
import com.tabibe.app.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyAppointmentDetail extends Fragment {


    public static MyAppointmentDetail newInstance() {

        Bundle args = new Bundle();

        MyAppointmentDetail fragment = new MyAppointmentDetail();
        fragment.setArguments(args);
        return fragment;
    }


    public MyAppointmentDetail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_appointment_detail, container, false);
        ButterKnife.bind(this,view);
        setHasOptionsMenu(true);

        ((MainActivity)getActivity()).toolbar.setNavigationIcon(R.drawable.ic_arrow_back_ios);
        ((MainActivity)getActivity()).title.setText("Informations du rendez-vous");

        return view;
    }

}
