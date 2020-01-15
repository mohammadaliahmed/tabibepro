package com.tabibe.app.fragment;


import android.graphics.Paint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.tabibe.app.MainActivity;
import com.tabibe.app.R;
import com.tabibe.app.adapter.MyAppointmentsAdapter;
import com.tabibe.app.adapter.MyAppointmentsClicked;
import com.tabibe.app.model.BaseResponse;
import com.tabibe.app.model.LoginUser;
import com.tabibe.app.model.MyAppointment;
import com.tabibe.app.model.MyAppointmentResponse;
import com.tabibe.app.util.Constants;
import com.tabibe.app.util.DialogUtils;
import com.tabibe.app.util.IEventlistener;
import com.tabibe.app.util.PreferenceConnector;
import com.tabibe.app.util.Utils;
import com.tabibe.app.viewmodel.MyAppointmentsViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyAppointments extends BaseFragment implements MyAppointmentsClicked , IEventlistener {

    @BindView(R.id.my_appointments_recyclerView)
    RecyclerView myAppointmentsRecyclerView;

    MyAppointmentsAdapter appointmentsAdapter;

    MyAppointmentsViewModel myAppointmentsViewModel;

    private String appointmentId = "-1";

    @BindView(R.id.reserve_appointment)
    TextView reserveAppointmentTextview;

    public MyAppointments() {
        // Required empty public constructor
    }


    public static MyAppointments newInstance() {

        Bundle args = new Bundle();

        MyAppointments fragment = new MyAppointments();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_appointments, container, false);
        ButterKnife.bind(this,view);
        setHasOptionsMenu(true);

//        ((MainActivity)getActivity()).toolbar.setNavigationIcon(R.drawable.ic_arrow_back_ios);
        ((MainActivity)getActivity()).title.setText("Mes rendez-vous");

        reserveAppointmentTextview.setPaintFlags(reserveAppointmentTextview.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        myAppointmentsViewModel = ViewModelProviders.of(MyAppointments.this).get(MyAppointmentsViewModel.class);
        myAppointmentsViewModel.init();

        setUpRecyclerView();
        fetchAppointments();

        return view;
    }

    private void fetchAppointments() {
        if(Utils.checkInternetConnection(getContext())) {
            showProgressDialog("Obtenir des rendez-vous...");
            String userData = PreferenceConnector.readString(getContext(),PreferenceConnector.USER_DATA,null);

            Gson gson = new Gson();
            LoginUser loginUser = gson.fromJson(userData, LoginUser.class);
            myAppointmentsViewModel.fetchAppointments(Constants.API_ADMIN, Constants.API_PASSWORD,loginUser.getId());
            observeData();
        }else {
            DialogUtils.showDialogMessage(getContext(),getString(R.string.no_network));
        }
    }

    private void observeData() {

        myAppointmentsViewModel.getAppointmentResponse().observe(this, new Observer<MyAppointmentResponse>() {
            @Override
            public void onChanged(MyAppointmentResponse myAppointmentResponse) {

                closeLoadingProgressBar();
                if(myAppointmentResponse.getStatus()==200){

                    appointmentsAdapter.addItems(myAppointmentResponse.getAppointments());
                    myAppointmentsRecyclerView.setAdapter(appointmentsAdapter);
                }
                else if(myAppointmentResponse.getStatus() == 404){
                    appointmentsAdapter.clearItems();
                    DialogUtils.showDialogMessage(getContext(),
                            "Aucun rendez-vous trouvé");
                }
                else {
                    DialogUtils.showDialogMessage(getContext(),"Quelque chose s'est mal passé.");
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        ((MainActivity)getActivity()).toolbar.setNavigationIcon(null);
//        ((MainActivity)getActivity()).title.setText(getResources().getString(R.string.app_name));
//        ((MainActivity)getActivity()).removeFragment();
        return super.onOptionsItemSelected(item);
    }



    private void setUpRecyclerView() {

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        ((LinearLayoutManager) mLayoutManager).setOrientation(RecyclerView.VERTICAL);
        myAppointmentsRecyclerView.setLayoutManager(mLayoutManager);
        appointmentsAdapter = new MyAppointmentsAdapter(getContext(),new ArrayList<MyAppointment>(),this);
    }


    @Override
    public void onAppoiintmentClick(MyAppointment myAppointment) {

        deleteAppointment(myAppointment.getId());
    }

    private void deleteAppointment(String id) {

        appointmentId=id;
        DialogUtils.showDialogWithCallBack(getContext(),"Supprimer un rendez-vous","Êtes-vous sûr de vouloir supprimer ce rendez-vous?",this,"Oui","Non");
    }

    private void observeDeletedAppointment() {

        myAppointmentsViewModel.getDeletedAppointmentResponse().observe(this, new Observer<BaseResponse>() {
            @Override
            public void onChanged(BaseResponse baseResponse) {


                closeLoadingProgressBar();
                if(baseResponse.getStatus()==200){
                    fetchAppointments();
                }
                else if(baseResponse.getStatus() == 404){
                    DialogUtils.showDialogMessage(getContext(),
                            " No se encontraron citas.");
                }
                else {
                    DialogUtils.showDialogMessage(getContext(),"Quelque chose s'est mal passé.");
                }

            }
        });

    }

    @Override
    public void onSuccess() {

        if(Utils.checkInternetConnection(getActivity())) {
            showProgressDialog("Eliminar cita...");
            myAppointmentsViewModel.deleteAppointment(Constants.API_ADMIN, Constants.API_PASSWORD,appointmentId);
            observeDeletedAppointment();
        }else {
            DialogUtils.showDialogMessage(getActivity(),getString(R.string.no_network));
        }
    }

    @Override
    public void onCancel() {

    }

}
