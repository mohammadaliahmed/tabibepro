package com.tabibe.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tabibe.app.R;
import com.tabibe.app.model.DoctorSlots;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AppointDateAdapter extends RecyclerView.Adapter<AppointmentDateViewHolder> {

    private List<DoctorSlots> appointments;

    private Context context;

    private SelectedAppointedTime listener;

    public AppointDateAdapter(List<DoctorSlots> appointmentList,SelectedAppointedTime listener){

        appointments = appointmentList;
        this.listener = listener;
    }


    @NonNull
    @Override
    public AppointmentDateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        return new AppointmentDateViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_date_parent_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentDateViewHolder holder, int position) {

        holder.onBind(context,appointments.get(position),listener);
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public void addItems(List<DoctorSlots> appointments) {
        this.appointments.addAll(appointments);
        notifyDataSetChanged();
    }
}
