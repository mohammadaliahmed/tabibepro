package com.tabibe.app.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import com.tabibe.app.GlideApp;
import com.tabibe.app.R;
import com.tabibe.app.model.Doctor;
import com.tabibe.app.util.FontManager;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorListViewHolder extends BaseViewHolder
{
    @BindView(R.id.docName)
    TextView docName;

    @BindView(R.id.speciality)
    TextView speciality;

    @BindView(R.id.location)
    TextView location;

    @BindView(R.id.profile_image)
    CircleImageView profileImage;

    @BindView(R.id.arrow)
    TextView arrow;

    private View viewItem;

    public DoctorListViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        viewItem = itemView;

    }

    @Override
    protected void clear() {

        docName.setText("");
        speciality.setText("");
        location.setText("");
    }

    public void onBind(int position, List<Doctor> doctorList, Context context, OnDoctorListClicked listner){

        super.onBind(position,doctorList,context, listner);

        final Doctor doctor = doctorList.get(position);
        docName.setText(doctor.getFirst_name()+" "+doctor.getLast_name());
        speciality.setText(doctor.getSpeciality_name());
        location.setText(doctor.getClinic_address());
        arrow.setTypeface(getTypeFace(context));
        Random ran = new Random();
        int x = ran.nextInt(5) + 0;


        if(doctor.getImage_path()!=null){
            GlideApp.with(context).load(doctor.getImage_path()).into(profileImage);
        }
        viewItem.setOnClickListener(v -> {
            listner.onClick(doctor,x);
        });
    }

    private Typeface getTypeFace(Context context){
        return FontManager.getTypeface(context, FontManager.FONTAWESOME);
    }
}
