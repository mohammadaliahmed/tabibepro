package com.tabibe.app.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.tabibe.app.R;
import com.tabibe.app.model.Speciality;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SpecialityListViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.speciality_name)
    TextView specialityName;

    private View viewItem;

    public SpecialityListViewHolder(@NonNull View itemView) {

        super(itemView);
        ButterKnife.bind(this,itemView);
        this.viewItem = itemView;
    }

    public void onBind(Speciality speciality, Context context , OnSpecialityListClicked specialityListClicked){

        specialityName.setText(speciality.getName());

        viewItem.setOnClickListener(v -> {
            specialityListClicked.onSpecialityClicked(speciality);
        });

    }
}
