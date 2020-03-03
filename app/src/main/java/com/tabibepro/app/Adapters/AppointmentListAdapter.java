package com.tabibepro.app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tabibepro.app.Models.AppointmentsModel;
import com.tabibepro.app.Models.ScheduleModel;
import com.tabibepro.app.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AppointmentListAdapter extends RecyclerView.Adapter<AppointmentListAdapter.ViewHolder> {
    Context context;
    List<AppointmentsModel> itemList;
    AppointmentListCallbacks callbacks;

    public AppointmentListAdapter(Context context, List<AppointmentsModel> itemList, AppointmentListCallbacks callbacks) {
        this.context = context;
        this.itemList = itemList;
        this.callbacks = callbacks;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.appointment_item_layout, parent, false);
        AppointmentListAdapter.ViewHolder viewHolder = new AppointmentListAdapter.ViewHolder(view);

        return viewHolder;
    }

    public void setItemList(List<AppointmentsModel> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final AppointmentsModel model = itemList.get(position);

        holder.patientInfo.setText("Name: " + model.getPatientName() + "\nPhone: " + model.getPhone());
        holder.bookingInfo.setText("Date: " + model.getAppointmentDate() + "\nSlot: " + model.getDoctorSlot());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbacks.onDelete(model,position);
            }
        });
        holder.history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbacks.onHistory(model);
            }
        });
        holder.ordinance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbacks.onOrdinance(model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView patientInfo, bookingInfo;
        Button delete, history, ordinance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            patientInfo = itemView.findViewById(R.id.patientInfo);
            bookingInfo = itemView.findViewById(R.id.bookingInfo);
            history = itemView.findViewById(R.id.history);
            ordinance = itemView.findViewById(R.id.ordinance);
            delete = itemView.findViewById(R.id.delete);
        }
    }

    public interface AppointmentListCallbacks {
        public void onHistory(AppointmentsModel model);

        public void onOrdinance(AppointmentsModel model);

        public void onDelete(AppointmentsModel model,int position);
    }
}
