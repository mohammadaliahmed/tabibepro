package com.tabibe.app.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.tabibe.app.R;
import com.tabibe.app.model.ConsultationChoice;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ConsultationChoisesViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.appointment_name)
    TextView appointmentView;

    private View view;

    public ConsultationChoisesViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        view = itemView;
    }

    public void onBind(ConsultationChoice consultationChoice, Context context , OnAppointmentClicked appointmentClicked){

        appointmentView.setText(consultationChoice.getName());

        view.setOnClickListener(v ->
        {appointmentClicked.onClick(consultationChoice);});
    }
}
