package com.tabibepro.app.Adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tabibepro.app.Models.PrescriptionModel;
import com.tabibepro.app.Models.ScheduleModel;
import com.tabibepro.app.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PrescriptionListAdapter extends RecyclerView.Adapter<PrescriptionListAdapter.ViewHolder> {
    Context context;
    List<PrescriptionModel> itemList;
    PrescriptionListCallbacks callbacks;

    public PrescriptionListAdapter(Context context, List<PrescriptionModel> itemList, PrescriptionListCallbacks callbacks) {
        this.context = context;
        this.itemList = itemList;
        this.callbacks = callbacks;
    }

    public void setItemList(List<PrescriptionModel> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.prescription_item_layout, parent, false);
        PrescriptionListAdapter.ViewHolder viewHolder = new PrescriptionListAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final PrescriptionModel model = itemList.get(position);

        String sourceString = "<b>Description: </b> " + model.getDescription();
        holder.description.setText(Html.fromHtml(sourceString));
        Glide.with(context).load(model.getImageUrl()).into(holder.image);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbacks.onDelete(model, position);
            }
        });
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbacks.onDownload(model, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView description;
        ImageView image;
        Button download, delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.description);
            image = itemView.findViewById(R.id.image);
            download = itemView.findViewById(R.id.download);
            delete = itemView.findViewById(R.id.delete);
        }
    }

    public interface PrescriptionListCallbacks {
        public void onDelete(PrescriptionModel model, int position);

        public void onDownload(PrescriptionModel model, int position);
    }
}
