package com.tabibe.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.tabibe.app.R;
import com.tabibe.app.model.City;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LocationSearchAdapter extends RecyclerView.Adapter<LocationListViewHolder> implements Filterable {

    private List<City> locations;
    private List<City> locationsFiltered;
    private Context context;
    private OnLocationListClicked listner;

    public LocationSearchAdapter(ArrayList<City> locations , OnLocationListClicked onLocationListClicked){

        this.locations = locations;
        this.locationsFiltered = locations;
        listner = onLocationListClicked;
    }


    @NonNull
    @Override
    public LocationListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new LocationListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.location_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull LocationListViewHolder holder, int position) {

            holder.onBind(locationsFiltered.get(position), context, listner);

    }

    @Override
    public int getItemCount() {
        return locationsFiltered.size();
    }

    public void addItems(List<City> specialityList) {
        this.locationsFiltered.clear();
        this.locationsFiltered.addAll(specialityList);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    locationsFiltered = locations;
                } else {
                    List<City> filteredList = new ArrayList<>();
                    for (City row : locations) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().startsWith(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    locationsFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = locationsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                locationsFiltered = (ArrayList<City>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
