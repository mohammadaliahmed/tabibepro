package com.tabibe.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.tabibe.app.R;
import com.tabibe.app.model.Doctor;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DocListAdapter extends RecyclerView.Adapter<BaseViewHolder> implements Filterable {

    private List<Doctor> doctorList;
    private List<Doctor> doctorListFiltered;
    private Context context;
    private OnDoctorListClicked listner;

    public DocListAdapter(ArrayList<Doctor> docList, OnDoctorListClicked onItemClick){
        doctorList = docList;
        doctorListFiltered = docList;
        listner = onItemClick;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        return new DoctorListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.doc_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position,doctorListFiltered,context,listner);
    }

    @Override
    public int getItemCount() {
        return doctorListFiltered.size();
    }

    public void addItems(List<Doctor> doctorList) {
        this.doctorListFiltered.clear();
        this.doctorListFiltered.addAll(doctorList);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    doctorListFiltered = doctorList;
                } else {
                    List<Doctor> filteredList = new ArrayList<>();
                    for (Doctor row : doctorList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getFirst_name().toLowerCase().startsWith(charString.toLowerCase()) || row.getLast_name().toLowerCase().startsWith(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    doctorListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = doctorListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                doctorListFiltered = (ArrayList<Doctor>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
