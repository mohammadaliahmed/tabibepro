package com.tabibe.app.adapter;

import android.content.Context;
import android.view.View;

import com.tabibe.app.model.Doctor;
import com.tabibe.app.model.Speciality;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    private int mCurrentPosition;

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    protected abstract void clear();

    public void onBind(int position, List<Doctor> doctorList, Context context, OnDoctorListClicked listner) {
        mCurrentPosition = position;
        clear();
    }

    public void onBind(int position, List<Speciality> specialityList, OnDoctorListClicked listner) {
        mCurrentPosition = position;
        clear();
    }



    public int getCurrentPosition() {
        return mCurrentPosition;
    }
}
