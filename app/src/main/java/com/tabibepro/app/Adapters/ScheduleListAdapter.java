package com.tabibepro.app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tabibepro.app.Models.ScheduleModel;
import com.tabibepro.app.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.ViewHolder> {
    Context context;
    List<ScheduleModel> itemList;
    ScheduleListCallbacks callbacks;

    public ScheduleListAdapter(Context context, List<ScheduleModel> itemList, ScheduleListCallbacks callbacks) {
        this.context = context;
        this.itemList = itemList;
        this.callbacks = callbacks;
    }

    public void setItemList(List<ScheduleModel> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.schedule_item_layout, parent, false);
        ScheduleListAdapter.ViewHolder viewHolder = new ScheduleListAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final ScheduleModel model = itemList.get(position);
        holder.time.setText(model.getStartTime() + " - " + model.getEndTime());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbacks.onDelete(model,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView time;
        RelativeLayout delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time=itemView.findViewById(R.id.time);
            delete=itemView.findViewById(R.id.delete);
        }
    }

    public interface ScheduleListCallbacks {
        public void onDelete(ScheduleModel model,int position);
    }
}
