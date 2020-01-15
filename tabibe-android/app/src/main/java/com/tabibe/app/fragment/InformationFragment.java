package com.tabibe.app.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.tabibe.app.R;
import com.tabibe.app.adapter.PagerAdapter;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class InformationFragment extends Fragment {

    @BindView(R.id.pager)
    ViewPager viewPager;

    @BindView(R.id.tablayout)
    TabLayout tabLayout;

    public InformationFragment() {
        // Required empty public constructor
    }


    public static InformationFragment newInstance() {

        Bundle args = new Bundle();

        InformationFragment fragment = new InformationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_information, container, false);
        ButterKnife.bind(this,view);

        setUpViewPager();
        return view;
    }


    private void setUpViewPager() {

        PagerAdapter myPagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(myPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

}
