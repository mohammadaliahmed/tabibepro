package com.tabibe.app.adapter;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.tabibe.app.R;
import com.tabibe.app.model.DoctorSlotsDetail;
import com.tabibe.app.util.DialogUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AppointmentTimeViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.appointment_time)
    public TextView appontmentTime;
    private boolean stateChanged=true;
    Context context;
    private SelectedAppointedTime listener;
    private DoctorSlotsDetail slotsDetail;

    public AppointmentTimeViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void onBind(Context context , DoctorSlotsDetail slotsDetail, SelectedAppointedTime selectedAppointedTime){
        this.slotsDetail = slotsDetail;
        if(slotsDetail.getIs_free().contains("Y")){
            appontmentTime.setText(convertedData(slotsDetail.getStartTime()));
        }

        //selectedtime = appointmentTime.getAppointmentTime();
        this.context = context;
        listener = selectedAppointedTime;

    }

    private String convertedData(String date){
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


    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick(R.id.appointment_time)
    public void onClicK(View view){

        if(slotsDetail.getIs_free().contains("Y")){

            if(stateChanged) {
                // reset background to default;
                appontmentTime.setTextColor(context.getColor(R.color.white));
                appontmentTime.setBackground(context.getDrawable(R.drawable.rounded_selected_textview));
                listener.selectedTimeSlot(slotsDetail.getAppointment_date(),slotsDetail.getId());

            } else {
                appontmentTime.setBackground(context.getDrawable(R.drawable.rounded_textview));
            }
            stateChanged = !stateChanged;
        }else {
            DialogUtils.showDialogMessage(context,
                    "Slot déjà réservé. Sélectionnez un autre emplacement.");
        }

    }

}
