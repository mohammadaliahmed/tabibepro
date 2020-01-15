package com.tabibe.app.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.tabibe.app.R;
import com.tabibe.app.model.City;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LocationListViewHolder extends RecyclerView.ViewHolder
{
    @BindView(R.id.location_name)
    TextView locationView;

    private View view;

    public LocationListViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        view = itemView;
    }

    public void onBind(City location , Context context , OnLocationListClicked locationListClicked){

        locationView.setText(location.getName());

        view.setOnClickListener(v ->
        {locationListClicked.locationClicked(location);});
    }
}
