package com.tabibe.app.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tabibe.app.MainActivity;
import com.tabibe.app.R;
import com.tabibe.app.activity.DoctorDetailActivity;
import com.tabibe.app.adapter.DocListAdapter;
import com.tabibe.app.adapter.OnDoctorListClicked;
import com.tabibe.app.model.Doctor;
import com.tabibe.app.model.DoctorResponse;
import com.tabibe.app.util.Constants;
import com.tabibe.app.util.DialogUtils;
import com.tabibe.app.util.Utils;
import com.tabibe.app.viewmodel.DoctorViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends BaseFragment implements OnDoctorListClicked {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.search_edit_text)
    EditText searchView;

    private DocListAdapter docListAdapter;

    private List<Doctor> doctorList = new ArrayList<>();

    DoctorViewModel doctorViewModel;

    String cityId;
    String specialityId;
    String screenName;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(String screenName , String specialityId , String cityId) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();

        args.putString("specialityId",specialityId);
        args.putString("cityId",cityId);

        if(!cityId.equals("-1") && !specialityId.equals("-1")){
            args.putString("screen",Constants.CITY_SPECIALITY);
        }
        else if(!cityId.equals("-1") && specialityId.equals("-1")){
            args.putString("screen",Constants.CITY_SCREEN);

        }else if(cityId.equals("-1") && !specialityId.equals("-1")){
            args.putString("screen",Constants.SPECIALITY_SCREEN);

        }else if(cityId.equals("-1") && specialityId.equals("-1")){
            args.putString("screen",Constants.MAIN_SCREEN);
        }

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this,view);
        setHasOptionsMenu(true);

        screenName = getArguments().getString("screen");
        cityId = getArguments().getString("cityId");
        specialityId = getArguments().getString("specialityId");

//        ((MainActivity)getActivity()).toolbar.setNavigationIcon(R.drawable.ic_arrow_back_ios);
        ((MainActivity)getActivity()).title.setText("Rechercher");

        doctorViewModel = ViewModelProviders.of(SearchFragment.this).get(DoctorViewModel.class);
        doctorViewModel.init();

        setUpRecyclerView();
        fetchDoctors();
        searchCities();
        return view;
    }

    private void fetchDoctors() {

        switch (screenName){

            case Constants.MAIN_SCREEN:
                fetchAllDoctors();
                break;

            case Constants.SPECIALITY_SCREEN:
                fetchAllDoctorsOnBasisOfSpeciality();
                break;

            case  Constants.CITY_SCREEN:
                fetchAllDoctorsOnBasisOfCity();
                break;

            case Constants.CITY_SPECIALITY:
                fetchAllDoctorsOnBasisOfCityAndSpeciality();
                break;
        }


    }

    private void fetchAllDoctors(){
        if(Utils.checkInternetConnection(getActivity())) {
            showProgressDialog("Aller chercher des médecins...");
            doctorViewModel.fetDoctors(Constants.API_ADMIN, Constants.API_PASSWORD);
            observeData();
        }else {
            DialogUtils.showDialogMessage(getActivity(),getString(R.string.no_network));
        }
    }

    private void fetchAllDoctorsOnBasisOfSpeciality(){
        if(Utils.checkInternetConnection(getActivity())) {
            showProgressDialog("Aller chercher des médecins par spécialité...");
            doctorViewModel.fetDoctorsOnBasisOfSpeciality(Constants.API_ADMIN, Constants.API_PASSWORD,specialityId);
            observeData();
        }else {
            DialogUtils.showDialogMessage(getActivity(),getString(R.string.no_network));
        }
    }

    private void fetchAllDoctorsOnBasisOfCity(){
        if(Utils.checkInternetConnection(getActivity())) {
            showProgressDialog("Aller chercher des médecins sur la base des villes...");
            doctorViewModel.fetDoctorsOnBasisOfCities(Constants.API_ADMIN, Constants.API_PASSWORD,cityId);
            observeData();
        }else {
            DialogUtils.showDialogMessage(getActivity(),getString(R.string.no_network));
        }
    }

    private void fetchAllDoctorsOnBasisOfCityAndSpeciality(){
        if(Utils.checkInternetConnection(getActivity())) {
            showProgressDialog("Aller chercher des médecins sur la base des villes...");
            doctorViewModel.fetDoctorsOnBasisOfCitiesAndSpeciality(Constants.API_ADMIN, Constants.API_PASSWORD,cityId,specialityId);
            observeData();
        }else {
            DialogUtils.showDialogMessage(getActivity(),getString(R.string.no_network));
        }
    }

    private void observeData() {

        doctorViewModel.getLoginResponse().observe(this, new Observer<DoctorResponse>() {
            @Override
            public void onChanged(DoctorResponse doctorResponse) {
                closeLoadingProgressBar();
                if(doctorResponse.getStatus()==200){
                    doctorList.addAll(doctorResponse.getDoctors());
                    docListAdapter.addItems(doctorList);
                    recyclerView.setAdapter(docListAdapter);
                }
                else if(doctorResponse.getStatus() == 404){
                    DialogUtils.showDialogMessage(getContext(),
                            "Aucun médecin trouvé.");
                }
                else {
                    DialogUtils.showDialogMessage(getContext(),"Quelque chose s'est mal passé.");
                }

            }
        });

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
                    docListAdapter.getFilter().filter(s.toString());
                }else {
                    docListAdapter.addItems(doctorList);
                    recyclerView.setAdapter(docListAdapter);
                }
            }
        });

    }


    private void setUpRecyclerView() {

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        ((LinearLayoutManager) mLayoutManager).setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        docListAdapter = new DocListAdapter(new ArrayList<Doctor>(),this);
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
    public void onClick(Doctor doctor,int x) {

        Intent intent = new Intent(getContext(), DoctorDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("imageNumber",x);
        bundle.putSerializable("doctor",doctor);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
