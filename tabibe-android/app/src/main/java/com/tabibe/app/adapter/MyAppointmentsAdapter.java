package com.tabibe.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tabibe.app.R;
import com.tabibe.app.model.MyAppointment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAppointmentsAdapter extends RecyclerView.Adapter<MyAppointmentViewHolder> {

    private List<MyAppointment> appointmentList;
    private Context mContext;
    private MyAppointmentsClicked myAppointmentsClicked;

    public MyAppointmentsAdapter(Context context, List<MyAppointment> appointments , MyAppointmentsClicked appointmentsClicked){

        appointmentList = appointments;
        mContext = context;
        myAppointmentsClicked = appointmentsClicked;
    }


    @NonNull
    @Override
    public MyAppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new MyAppointmentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_appointments,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyAppointmentViewHolder holder, int position) {

        holder.onBind(appointmentList.get(position),mContext,myAppointmentsClicked);
    }

    public void addItems(List<MyAppointment> appointments) {
        this.appointmentList.clear();
        this.appointmentList.addAll(appointments);
        notifyDataSetChanged();
    }

    public void clearItems() {
        this.appointmentList.clear();
        notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
        return appointmentList.size();
    }
}
