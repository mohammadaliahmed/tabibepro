package com.tabibe.app.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tabibe.app.MainActivity;
import com.tabibe.app.R;
import com.tabibe.app.model.BaseResponse;
import com.tabibe.app.model.Doctor;
import com.tabibe.app.model.LoginUser;
import com.tabibe.app.model.Patient;
import com.tabibe.app.model.UserResponse;
import com.tabibe.app.util.Constants;
import com.tabibe.app.util.DialogUtils;
import com.tabibe.app.util.IEventlistener;
import com.tabibe.app.util.PreferenceConnector;
import com.tabibe.app.util.Utils;
import com.tabibe.app.viewmodel.DoctorSlotsViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PatientListFragment extends BaseFragment implements IEventlistener {

    DoctorSlotsViewModel doctorSlotsViewModel;

    @BindView(R.id.radio_group)
    RadioGroup radioGroup;

    @BindView(R.id.makeAppointment)
    Button appointmentButton;

    private Doctor doctor;
    private String cid;
    private String appointmentDate;
    private String timeSlot;
    private String pid;
    private String username;
    private View view;
    private String consultationReason;

    public PatientListFragment() {
        // Required empty public constructor
    }

    public static PatientListFragment newInstance(Doctor doctor,String consultationId,String appointmentDate , String slot,String cr) {
        
        Bundle args = new Bundle();
        
        PatientListFragment fragment = new PatientListFragment();
        args.putSerializable("doctor",doctor);
        args.putString("consultationId",consultationId);
        args.putSerializable("appointmentDate",appointmentDate);
        args.putString("slot",slot);
        args.putString("consultationReason",cr);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_patient_list, container, false);
        ButterKnife.bind(this,view);

        doctor = (Doctor) getArguments().getSerializable("doctor");
        cid = getArguments().getString("consultationId");
        appointmentDate = getArguments().getString("appointmentDate");
        timeSlot = getArguments().getString("slot");
        consultationReason = getArguments().getString("consultationReason");

        doctorSlotsViewModel = ViewModelProviders.of(PatientListFragment.this).get(DoctorSlotsViewModel.class);
        doctorSlotsViewModel.init();

        fetchUsers();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                appointmentButton.setClickable(true);
                pid = String.valueOf(group.getCheckedRadioButtonId());
                RadioButton radioButton = (RadioButton) view.findViewById(group.getCheckedRadioButtonId());
                username = radioButton.getText().toString();
            }
        });

        appointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pid!=null){
                    if(Utils.checkInternetConnection(getActivity())) {
                        showProgressDialog("Prendre rendez-vous...");
                        doctorSlotsViewModel.makeAppointment(Constants.API_ADMIN, Constants.API_PASSWORD,doctor.getId(),pid,cid,timeSlot,appointmentDate);
                        pid=null;
                        observeAppointmentData();
                    }else {
                        DialogUtils.showDialogMessage(getActivity(),getString(R.string.no_network));
                    }
                }else {
                    DialogUtils.showDialogMessage(getContext(), "S'il vous plaît sélectionner frère");
                }

            }
        });

        return view;

    }

    private void observeAppointmentData() {

        doctorSlotsViewModel.getAppointmentResponse().observe(this, new Observer<BaseResponse>() {
            @Override
            public void onChanged(BaseResponse baseResponse) {
                closeLoadingProgressBar();
                if (baseResponse.getStatus()==200) {
                    showDialogMessage();
                }
                else {
                    DialogUtils.showDialogMessage(getContext(),"Quelque chose s'est mal passé.");
                }
            }
        });
    }


    private void showDialogMessage(){

        String message = "Bonjour "+username+",\n" +
                " \n" +
                "Je vous confirme votre rendez-vous du "+appointmentDate+ " avec le docteur "+doctor.getFirst_name()+" "+doctor.getLast_name()+ " pour une "+consultationReason+" à l’adresse "+doctor.getClinic_address()+" \n" +
                "\n" +
                "\n" +
                "Pour rejoindre le docteur "+doctor.getFirst_name()+" "+doctor.getLast_name()+" : "+doctor.getPhone_number()+". Pour toute annulation de rendez-vous, veuillez faire le nécessaire 48h avant le rendez-vous. \n" +
                "\n" +
                "A bientôt sur Tabibe";

        DialogUtils.showDialogWithCallBack(getContext(),"",message,this,"Oui","");
    }

    private void fetchUsers() {
        if(Utils.checkInternetConnection(getActivity())) {
            showProgressDialog("Aller chercher des frères et soeurs...");
            String userData = PreferenceConnector.readString(getContext(),PreferenceConnector.USER_DATA,null);
            Gson gson = new Gson();
            LoginUser loginUser = gson.fromJson(userData, LoginUser.class);
            doctorSlotsViewModel.fetchUsers(Constants.API_ADMIN, Constants.API_PASSWORD,loginUser.getId());
            observeData();
        }else {
            DialogUtils.showDialogMessage(getActivity(),getString(R.string.no_network));
        }

    }


    private void observeData() {

        doctorSlotsViewModel.getUserResponse().observe(this, new Observer<UserResponse>() {
            @Override
            public void onChanged(UserResponse userResponse) {

                closeLoadingProgressBar();
                if(userResponse.getStatus()==200){
                    setUpRadioButtons(userResponse.getuResponses().get(0).getPatients());
                }
                else {
                    DialogUtils.showDialogMessage(getContext(),"Quelque chose s'est mal passé.");
                }
            }
        });
    }

    private void setUpRadioButtons(ArrayList<Patient> patients) {

        for (int i = 0; i < patients.size(); i++) {
            RadioButton rbn = new RadioButton(getContext());
            rbn.setId(Integer.valueOf(patients.get(i).getId()));
            rbn.setText(patients.get(i).getFirst_name()+" "+patients.get(i).getLast_name());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            params.setMargins(0,15,0,0);
            rbn.setLayoutParams(params);
            radioGroup.addView(rbn);
        }

    }

    @Override
    public void onSuccess() {

        Intent intent = new Intent(getActivity(),MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("myappointments",1);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    @Override
    public void onCancel() {

    }

}
