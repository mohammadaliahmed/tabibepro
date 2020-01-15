package com.tabibe.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tabibe.app.R;
import com.tabibe.app.model.DoctorSlotsDetail;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AppointmentTimeAdapter extends RecyclerView.Adapter<AppointmentTimeViewHolder> {

    private List<DoctorSlotsDetail> appointments;
    private Context context;
    private SelectedAppointedTime selectedAppointedTimeListener;

    public AppointmentTimeAdapter(ArrayList<DoctorSlotsDetail> appointmentTimeList , SelectedAppointedTime listener){

        appointments = appointmentTimeList;
        selectedAppointedTimeListener = listener;
    }


    @NonNull
    @Override
    public AppointmentTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        return new AppointmentTimeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_time_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentTimeViewHolder holder, int position) {

        holder.onBind(context,appointments.get(position),selectedAppointedTimeListener);
    }

    public void addItems(List<DoctorSlotsDetail> appointments) {
        this.appointments.addAll(appointments);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return appointments.size();
    }
}
