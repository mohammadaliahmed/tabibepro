package com.tabibe.app.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tabibe.app.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyInformationFragment extends Fragment {


    public MyInformationFragment() {
        // Required empty public constructor
    }


    public static MyInformationFragment newInstance() {

        Bundle args = new Bundle();

        MyInformationFragment fragment = new MyInformationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_information, container, false);
        ButterKnife.bind(this,view);

        return view;
    }

}
