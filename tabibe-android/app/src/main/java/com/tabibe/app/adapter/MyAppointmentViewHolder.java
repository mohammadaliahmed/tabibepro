package com.tabibe.app.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tabibe.app.R;
import com.tabibe.app.model.MyAppointment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MyAppointmentViewHolder extends RecyclerView.ViewHolder {

    private View view;

    @BindView(R.id.appointment_date)
    TextView appointmentaDate;

    @BindView(R.id.appointment_time)
    TextView appointmentTime;

    @BindView(R.id.docName)
    TextView docName;

    @BindView(R.id.consultationReason)
    TextView consultationReason;

    @BindView(R.id.patient_name)
    TextView patientName;

    @BindView(R.id.deleteAppointment)
    ImageView delete;


    public MyAppointmentViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        ButterKnife.bind(this,itemView);
    }

    public void onBind(MyAppointment appointment, Context context , MyAppointmentsClicked appointmentClicked){

        appointmentaDate.setText(convertedDate(appointment.getAppointmentDate()));
        appointmentTime.setText(convertedTime(appointment.getSlot_start_time()));
        docName.setText(appointment.getDoctor_first_name()+" "+appointment.getDoctor_last_name());
        patientName.setText(appointment.getPatient_first_name()+" "+appointment.getPatient_last_name());
        consultationReason.setText(appointment.getConsultation_reasons_name());

        delete.setOnClickListener(v ->
        {appointmentClicked.onAppoiintmentClick(appointment);});
    }

    private String convertedDate(String date){
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

    private String convertedTime(String date){
        SimpleDateFormat spf=new SimpleDateFormat("HH:mm:ss");
        Date newDate= null;
        try {
            newDate = spf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf= new SimpleDateFormat("HH:mm");
        date = spf.format(newDate);
        return date;
    }
}
