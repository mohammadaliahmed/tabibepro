package com.tabibe.app.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
import com.tabibe.app.adapter.LocationSearchAdapter;
import com.tabibe.app.adapter.OnLocationListClicked;
import com.tabibe.app.model.City;
import com.tabibe.app.model.CityResponse;
import com.tabibe.app.util.Constants;
import com.tabibe.app.util.DialogUtils;
import com.tabibe.app.util.Utils;
import com.tabibe.app.viewmodel.SpecialityAndCityViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocationSearchFragment extends BaseFragment implements OnLocationListClicked  {


    @BindView(R.id.recyclerView_location)
    RecyclerView recyclerView;

    @BindView(R.id.location_search)
    EditText searchView;

    LocationSearchAdapter locationSearchAdapter;

    SpecialityAndCityViewModel specialityViewModel;

    List<City> citiesList = new ArrayList<City>();

    public static LocationSearchFragment newInstance() {
        LocationSearchFragment fragment = new LocationSearchFragment();
        return fragment;
    }

    public LocationSearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_location_search, container, false);
        ButterKnife.bind(this,view);
        setHasOptionsMenu(true);

//        ((MainActivity)getActivity()).toolbar.setNavigationIcon(R.drawable.ic_arrow_back_ios);
        ((MainActivity)getActivity()).title.setText("Emplacement");

        specialityViewModel = ViewModelProviders.of(LocationSearchFragment.this).get(SpecialityAndCityViewModel.class);
        specialityViewModel.init();


        setUpRecyclerView();
        fetchCities();
        searchCities();

        return view;
    }

    private void searchCities() {

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
                    locationSearchAdapter.getFilter().filter(s.toString());
                }else {
                    locationSearchAdapter.addItems(citiesList);
                    recyclerView.setAdapter(locationSearchAdapter);
                }
            }
        });

    }



    private void fetchCities() {

        if(Utils.checkInternetConnection(getActivity())) {
            showProgressDialog("Aller chercher des villes...");
            specialityViewModel.fetchCities(Constants.API_ADMIN, Constants.API_PASSWORD);
            observeData();
        }else {
            DialogUtils.showDialogMessage(getActivity(),getString(R.string.no_network));
        }

    }

    private void observeData() {

        specialityViewModel.getCitiesResponse().observe(this, new Observer<CityResponse>() {
            @Override
            public void onChanged(CityResponse specialityResponse) {

                closeLoadingProgressBar();
                if(specialityResponse.getStatus()==200){
                    citiesList = specialityResponse.getData();
                    locationSearchAdapter.addItems(citiesList);
                    recyclerView.setAdapter(locationSearchAdapter);
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
        locationSearchAdapter = new LocationSearchAdapter(new ArrayList<City>(),this);
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
    public void locationClicked(City location) {
        searchView.setText("");
        searchView.setFocusable(false);
        ((MainActivity)getActivity()).setCityId(String.valueOf(location.getId()),location.getName());
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
