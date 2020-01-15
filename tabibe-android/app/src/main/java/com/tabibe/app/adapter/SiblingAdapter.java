package com.tabibe.app.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tabibe.app.R;
import com.tabibe.app.model.Patient;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SiblingAdapter extends RecyclerView.Adapter<SiblingViewHolder> {

    private List<Patient> uResponseList;
    private SiblingEditClicked listner;

    public SiblingAdapter(ArrayList<Patient> uResponseList,SiblingEditClicked siblingEditClicked){
        this.uResponseList = uResponseList;
        listner = siblingEditClicked;
    }

    @NonNull
    @Override
    public SiblingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         return new SiblingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.sibling_info,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SiblingViewHolder holder, int position) {

        holder.onBind(uResponseList.get(position),listner);
    }

    @Override
    public int getItemCount() {
        return uResponseList.size();
    }

    public void addItems(List<Patient> specialityList) {
        this.uResponseList.clear();
        this.uResponseList.addAll(specialityList);
        notifyDataSetChanged();
    }
}
