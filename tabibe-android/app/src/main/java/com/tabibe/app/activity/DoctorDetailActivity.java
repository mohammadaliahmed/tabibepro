package com.tabibe.app.activity;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tabibe.app.GlideApp;
import com.tabibe.app.R;
import com.tabibe.app.model.Doctor;

public class DoctorDetailActivity extends AppCompatActivity {


    @BindView(R.id.feeLayout)
    LinearLayout feeLayout;

    @BindView(R.id.expertiesLayout)
    LinearLayout expertiesLayout;

    @BindView(R.id.experienceLayout)
    LinearLayout experienceLayout;

    @BindView(R.id.publicationView)
    LinearLayout publicationLayout;

    @BindView(R.id.languageView)
    LinearLayout languageLayout;

    @BindView(R.id.experties)
    TextView expertiesText;


    @BindView(R.id.location)
    TextView locationText;

    @BindView(R.id.mode_of_payment)
    TextView mode_of_payment;

    @BindView(R.id.docFee)
    TextView docFee;

    @BindView(R.id.experience)
    TextView experience;

    @BindView(R.id.publications)
    TextView publications;

    @BindView(R.id.spokenLanguage)
    TextView spokenLanguage;

    @BindView(R.id.docName)
    TextView docName;

    @BindView(R.id.backButton)
    ImageView backButton;

    @BindView(R.id.profileButton)
    Button profileButton;

    @BindView(R.id.profile_image)
    CircleImageView circleImageView;

    @BindView(R.id.speciality)
    TextView speciality;

    int imageNumber;

    Doctor doctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_detail);
        ButterKnife.bind(this);

        imageNumber = getIntent().getIntExtra("imageNumber",0);
        doctor = (Doctor) getIntent().getSerializableExtra("doctor");

        setText();
        handleClick();
        setImage();
    }

    private void handleClick() {

        backButton.setOnClickListener(v -> {
            this.finish();
        });

        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(this,DoctorProfileActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("imageNumber",imageNumber);
            bundle.putSerializable("doctorDetail",doctor);

            intent.putExtras(bundle);
            this.startActivity(intent);
        });

    }

    private void setImage() {

        if(doctor.getImage_path()!=null){
            GlideApp.with(this).load(doctor.getImage_path()).into(circleImageView);
        }
    }


    private void setText(){

        docName.setText(doctor.getFirst_name()+" "+doctor.getLast_name());
        speciality.setText(doctor.getSpeciality_name());
        locationText.setText(doctor.getClinic_address());
        mode_of_payment.setText(doctor.getPayment_modes());
        if(!doctor.getExperties().isEmpty()){
            String experties =getExperties();
            expertiesLayout.setVisibility(View.VISIBLE);
            expertiesText.setText(experties);
        }


        if(doctor.getFee()!=null && !doctor.getFee().isEmpty()){
            feeLayout.setVisibility(View.VISIBLE);
            docFee.setText(doctor.getFee()+" "+doctor.getFee_unit());
        }

        if(doctor.getExperiences()!=null && !doctor.getExperiences().isEmpty()){
            experienceLayout.setVisibility(View.VISIBLE);
            experience.setText(doctor.getExperiences());
        }

        if(doctor.getWorks_publications()!=null && !doctor.getWorks_publications().isEmpty()){
            publicationLayout.setVisibility(View.VISIBLE);
            publications.setText(doctor.getWorks_publications());
        }

        if(doctor.getSpoken_languages()!=null && !doctor.getSpoken_languages().isEmpty()){
            languageLayout.setVisibility(View.VISIBLE);
            spokenLanguage.setText(doctor.getSpoken_languages());
        }

    }

    private String getExperties() {

        String experties = doctor.getExperties().get(0).getExpertiesName();

        for(int i=1 ; i < doctor.getExperties().size() ; i++){
            experties = ","+doctor.getExperties().get(i).getExpertiesName();

        }
        return experties;

    }

}
