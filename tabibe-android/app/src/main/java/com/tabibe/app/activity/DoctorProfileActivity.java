package com.tabibe.app.activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;

import com.tabibe.app.BaseActivity;
import com.tabibe.app.GlideApp;
import com.tabibe.app.R;
import com.tabibe.app.adapter.ConsultationChoisesListAdapter;
import com.tabibe.app.adapter.OnAppointmentClicked;
import com.tabibe.app.fragment.AppointmentFragmnet;
import com.tabibe.app.model.ConsultationChoice;
import com.tabibe.app.model.ConsultationResponse;
import com.tabibe.app.model.Doctor;
import com.tabibe.app.util.Constants;
import com.tabibe.app.util.DialogUtils;
import com.tabibe.app.util.Utils;
import com.tabibe.app.viewmodel.ConsultationViewModel;

import java.util.ArrayList;

public class DoctorProfileActivity extends BaseActivity implements OnAppointmentClicked  {

    @BindView(R.id.profile_toolbar)
    public Toolbar toolbar;

    private int imageNumber;

    @BindView(R.id.profile_toolbar_image)
    CircleImageView circleImageView;

    @BindView(R.id.appointmentListView)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar_title)
    public TextView title;

    @BindView(R.id.docName)
            TextView docName;

    @BindView(R.id.speciality_name)
    TextView specialityName;

    ConsultationChoisesListAdapter consultationChoisesListAdapter;

    private Doctor doctor;

    private ConsultationViewModel consultationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);
        ButterKnife.bind(this);

        imageNumber = getIntent().getIntExtra("imageNumber",0);

        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_ios);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        doctor = (Doctor) getIntent().getSerializableExtra("doctorDetail");
        if(!doctor.getFirst_name().isEmpty())
        {
            docName.setText(doctor.getFirst_name()+" "+doctor.getLast_name());
        }
        if(doctor.getSpeciality_name()!=null){
            specialityName.setText(doctor.getSpeciality_name());
        }
        consultationViewModel = ViewModelProviders.of(DoctorProfileActivity.this).get(ConsultationViewModel.class);
        consultationViewModel.init();
        setImage();
        setUpRecyclerView();
        fetchConsultations();

    }

    private void fetchConsultations() {

        if(Utils.checkInternetConnection(this)) {
            showProgressDialog("Aller chercher des consultations...");
            consultationViewModel.fetchConsultation(Constants.API_ADMIN, Constants.API_PASSWORD,doctor.getId());
            observeData();
        }else {
            DialogUtils.showDialogMessage(this,getString(R.string.no_network));
        }
    }

    private void observeData() {

        consultationViewModel.getConsultationResponse().observe(this, new Observer<ConsultationResponse>() {
            @Override
            public void onChanged(ConsultationResponse consultationResponse) {

                closeLoadingProgressBar();
                if(consultationResponse.getStatus()==200){

                    consultationChoisesListAdapter.addItems(consultationResponse.getConsultations());
                    recyclerView.setAdapter(consultationChoisesListAdapter);
                }
                else {
                    DialogUtils.showDialogMessage(DoctorProfileActivity.this,"Quelque chose s'est mal passé.");
                }
            }
        });
    }


    private void setImage() {

        if(doctor.getImage_path()!=null){
            GlideApp.with(this).load(doctor.getImage_path()).into(circleImageView);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(getSupportFragmentManager().getBackStackEntryCount()>0) {

            getSupportFragmentManager().popBackStack();
        }
        else
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void setUpRecyclerView() {

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) mLayoutManager).setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        consultationChoisesListAdapter = new ConsultationChoisesListAdapter(new ArrayList<ConsultationChoice>(),this);
    }

    @Override
    public void onClick(ConsultationChoice consultationChoice) {

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, AppointmentFragmnet.newInstance(doctor,String.valueOf(consultationChoice.getId()),consultationChoice.getName())).addToBackStack("appointment_fragment").commit();

    }


    @Override
    public void onBackPressed() {

        if(getSupportFragmentManager().getBackStackEntryCount()>0) {

            toolbar.setNavigationIcon(null);
            getSupportFragmentManager().popBackStack();
        }
        else
            super.onBackPressed();
    }

    public void removeFragment(){
        getSupportFragmentManager().popBackStack();
    }
}
