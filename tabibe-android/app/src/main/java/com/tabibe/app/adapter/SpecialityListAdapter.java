package com.tabibe.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.tabibe.app.R;
import com.tabibe.app.model.Speciality;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SpecialityListAdapter extends RecyclerView.Adapter<SpecialityListViewHolder> implements Filterable {

    private List<Speciality> specialities;
    private List<Speciality> specialitiesFiltered;
    private Context context;
    private OnSpecialityListClicked listner;


    public SpecialityListAdapter(ArrayList<Speciality> specialities , OnSpecialityListClicked clicked){
        this.specialities = specialities;
        this.specialitiesFiltered = specialities;
        listner = clicked;
    }


    @NonNull
    @Override
    public SpecialityListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new SpecialityListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.speciality_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SpecialityListViewHolder holder, int position) {

            holder.onBind(specialitiesFiltered.get(position),context,listner);

    }

    public void addItems(List<Speciality> specialityList) {
        this.specialitiesFiltered.clear();
        this.specialitiesFiltered.addAll(specialityList);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return specialitiesFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    specialitiesFiltered = specialities;
                } else {
                    List<Speciality> filteredList = new ArrayList<>();
                    for (Speciality row : specialities) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().startsWith(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    specialitiesFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = specialitiesFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                specialitiesFiltered = (ArrayList<Speciality>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
