package com.tabibe.app.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.tabibe.app.MainActivity;
import com.tabibe.app.R;
import com.tabibe.app.adapter.OnSpecialityListClicked;
import com.tabibe.app.adapter.SpecialityListAdapter;
import com.tabibe.app.model.Speciality;
import com.tabibe.app.model.SpecialityResponse;
import com.tabibe.app.util.Constants;
import com.tabibe.app.util.DialogUtils;
import com.tabibe.app.util.Utils;
import com.tabibe.app.viewmodel.SpecialityAndCityViewModel;

import java.util.ArrayList;
import java.util.List;

public class SpecialityFragment extends BaseFragment implements OnSpecialityListClicked {

    @BindView(R.id.recyclerView_speciality)
    RecyclerView recyclerView;

    @BindView(R.id.speciality_search)
    EditText searchView;

    SpecialityListAdapter specialityListAdapter;
    SpecialityAndCityViewModel specialityViewModel;

    List<Speciality> specialities=new ArrayList<Speciality>();

    public SpecialityFragment() {
        // Required empty public constructor
    }

    public static SpecialityFragment newInstance() {
        SpecialityFragment fragment = new SpecialityFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_speciality, container, false);
        ButterKnife.bind(this,view);
        setHasOptionsMenu(true);
        setRetainInstance(true);
//
//        ((MainActivity)getActivity()).toolbar.setNavigationIcon(R.drawable.ic_arrow_back_ios);
        ((MainActivity)getActivity()).title.setText("Spécialité");

        specialityViewModel = ViewModelProviders.of(SpecialityFragment.this).get(SpecialityAndCityViewModel.class);
        specialityViewModel.init();

        setUpRecyclerView();
        fetchSpeciality();

        searchSpecialities();

        return view;
    }

    private void searchSpecialities() {

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty()){
                    specialityListAdapter.getFilter().filter(s.toString());
                }else {
                    specialityListAdapter.addItems(specialities);
                    recyclerView.setAdapter(specialityListAdapter);
                }
            }
        });

    }

    private void fetchSpeciality() {

        if(Utils.checkInternetConnection(getActivity())) {
            showProgressDialog("Aller chercher des spécialités...");
            specialityViewModel.fetchSpeciality(Constants.API_ADMIN, Constants.API_PASSWORD);
            observeData();
        }else {
            DialogUtils.showDialogMessage(getActivity(),getString(R.string.no_network));
        }

    }

    private void observeData() {

        specialityViewModel.getLoginResponse().observe(this, new Observer<SpecialityResponse>() {
            @Override
            public void onChanged(SpecialityResponse specialityResponse) {

                closeLoadingProgressBar();
                if(specialityResponse.getStatus()==200){
                    specialities.addAll(specialityResponse.getData());
                    specialityListAdapter.addItems(specialities);
                    recyclerView.setAdapter(specialityListAdapter);
                }
                else {
                    DialogUtils.showDialogMessage(getContext(),"Quelque chose s'est mal passé.");
                }

            }
        });


    }


    private void setUpRecyclerView() {

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        ((LinearLayoutManager) mLayoutManager).setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        specialityListAdapter = new SpecialityListAdapter(new ArrayList<Speciality>(),this);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.action_account).setVisible(false);

        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        ((MainActivity)getActivity()).toolbar.setNavigationIcon(null);
//        ((MainActivity)getActivity()).title.setText(getResources().getString(R.string.app_name));
//        ((MainActivity)getActivity()).removeFragment();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onSpecialityClicked(Speciality speciality) {
        searchView.setText("");
        searchView.setFocusable(false);
        ((MainActivity)getActivity()).setSpecialityId(String.valueOf(speciality.getId()),speciality.getName());
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
