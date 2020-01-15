package com.tabibe.app.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.tabibe.app.R;

import com.tabibe.app.model.DoctorSlots;
import com.tabibe.app.model.DoctorSlotsDetail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.GridLayoutManager;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AppointmentDateViewHolder extends RecyclerView.ViewHolder {


    @BindView(R.id.content)
    View content;

    @BindView(R.id.appointment_date)
    TextView textView;

    private View view;

    AppointmentTimeAdapter appointmentTimeAdapter;

    RecyclerView recyclerView;

    public AppointmentDateViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }


    public void onBind(Context context, DoctorSlots appointment, SelectedAppointedTime listener){

        textView.setText(convertedData(appointment.getSelected_date()));

        recyclerView = content.findViewById(R.id.recyclerView_appointment);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context,3);

        recyclerView.setLayoutManager(mLayoutManager);

        appointmentTimeAdapter = new AppointmentTimeAdapter(new ArrayList<DoctorSlotsDetail>(),listener);

        List<DoctorSlotsDetail> slotsDetails = new ArrayList<DoctorSlotsDetail>();
        for(DoctorSlotsDetail doctorSlotsDetail : appointment.getSlotsDetails()){
            doctorSlotsDetail.setAppointmentDate(appointment.getSelected_date());
            if(doctorSlotsDetail.getIs_free().contains("Y"))
            slotsDetails.add(doctorSlotsDetail);
        }

        appointmentTimeAdapter.addItems(slotsDetails);
        recyclerView.setAdapter(appointmentTimeAdapter);


    }

    private String convertedData(String date){
        SimpleDateFormat spf=new SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH);
        Date newDate= null;
        try {
            newDate = spf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf= new SimpleDateFormat("dd MMM yyyy", Locale.FRENCH);
        date = spf.format(newDate);
        return date;
    }


}
