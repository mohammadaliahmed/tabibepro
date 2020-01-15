package com.tabibe.app.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.tabibe.app.R;
import com.tabibe.app.activity.DoctorProfileActivity;
import com.tabibe.app.adapter.AppointDateAdapter;
import com.tabibe.app.adapter.SelectedAppointedTime;
import com.tabibe.app.model.Doctor;
import com.tabibe.app.model.DoctorSlots;
import com.tabibe.app.model.DoctorSlotsResponse;
import com.tabibe.app.util.Constants;
import com.tabibe.app.util.DialogUtils;
import com.tabibe.app.util.PreferenceConnector;
import com.tabibe.app.util.Utils;
import com.tabibe.app.viewmodel.DoctorSlotsViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppointmentFragmnet extends BaseFragment implements SelectedAppointedTime {


    @BindView(R.id.appointment_date_list)
    RecyclerView appointmentDateRecyclerView;

    AppointDateAdapter appointDateAdapter;

    private Doctor doctor;
    private String cid;
    private String creason;

    private DoctorSlotsViewModel doctorSlotsViewModel;

    public AppointmentFragmnet() {
        // Required empty public constructor
    }



    public static AppointmentFragmnet newInstance(Doctor doctor,String consultationId,String consultationReason) {

        Bundle args = new Bundle();

        AppointmentFragmnet fragment = new AppointmentFragmnet();
        args.putSerializable("doctor",doctor);
        args.putString("consultationId",consultationId);
        args.putString("consultationReason",consultationReason);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        container.removeAllViews();
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);


        ButterKnife.bind(this,view);
        setHasOptionsMenu(true);

        doctor = (Doctor) getArguments().getSerializable("doctor");
        cid = getArguments().getString("consultationId");
        creason = getArguments().getString("consultationReason");

        ((DoctorProfileActivity)getActivity()).toolbar.setNavigationIcon(R.drawable.ic_arrow_back_ios);

        doctorSlotsViewModel = ViewModelProviders.of(AppointmentFragmnet.this).get(DoctorSlotsViewModel.class);
        doctorSlotsViewModel.init();


        setUpRecyclerView();
        fetchSlots();
        return view;

    }

    private void fetchSlots() {

        if(Utils.checkInternetConnection(getActivity())) {
            showProgressDialog("Aller chercher la fente des médecins...");
            doctorSlotsViewModel.fetchSlots(Constants.API_ADMIN, Constants.API_PASSWORD,doctor.getId());
            observeData();
        }else {
            DialogUtils.showDialogMessage(getActivity(),getString(R.string.no_network));
        }

    }

    private void observeData() {

        doctorSlotsViewModel.getSlotsResponse().observe(this, new Observer<DoctorSlotsResponse>() {
            @Override
            public void onChanged(DoctorSlotsResponse doctorSlotsResponse) {

                closeLoadingProgressBar();
                if(doctorSlotsResponse.getStatus()==200){
                    appointDateAdapter.addItems(doctorSlotsResponse.getSlots());
                    appointmentDateRecyclerView.setAdapter(appointDateAdapter);
                }else {
                    DialogUtils.showDialogMessage(getContext(),"Quelque chose s'est mal passé.");
                }
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ((DoctorProfileActivity)getActivity()).removeFragment();
        return super.onOptionsItemSelected(item);
    }


    private void setUpRecyclerView() {

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        ((LinearLayoutManager) mLayoutManager).setOrientation(RecyclerView.VERTICAL);
        appointmentDateRecyclerView.setLayoutManager(mLayoutManager);
        appointDateAdapter = new AppointDateAdapter(new ArrayList<DoctorSlots>(),this);
    }

    @Override
    public void selectedTimeSlot(String time,String slotId) {

        String userData = PreferenceConnector.readString(getContext(),PreferenceConnector.USER_DATA,null);
        if(userData == null){
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, IdentificationFragment.newInstance(doctor,cid,time,slotId,creason)).addToBackStack("identification_frag").commit();
        }else {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, PatientListFragment.newInstance(doctor,cid,time,slotId,creason)).addToBackStack("identification_frag").commit();
        }
    }
}
