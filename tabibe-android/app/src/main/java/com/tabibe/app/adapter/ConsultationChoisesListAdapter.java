package com.tabibe.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tabibe.app.R;
import com.tabibe.app.model.ConsultationChoice;

import java.util.ArrayList;
import java.util.List;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ConsultationChoisesListAdapter extends RecyclerView.Adapter<ConsultationChoisesViewHolder> {

    private List<ConsultationChoice> consultationChoices;
    private Context context;
    private OnAppointmentClicked listner;

    public ConsultationChoisesListAdapter(ArrayList<ConsultationChoice> consultationChoices, OnAppointmentClicked listner){
        this.consultationChoices = consultationChoices;
        this.listner = listner;
    }


    @NonNull
    @Override
    public ConsultationChoisesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ConsultationChoisesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.consultation_choice_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ConsultationChoisesViewHolder holder, int position) {

        holder.onBind(consultationChoices.get(position),context,listner);
    }


    public void addItems(List<ConsultationChoice> consultationChoiceList) {
        this.consultationChoices.addAll(consultationChoiceList);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return consultationChoices.size();
    }
}
