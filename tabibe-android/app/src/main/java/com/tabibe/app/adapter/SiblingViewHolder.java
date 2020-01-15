package com.tabibe.app.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tabibe.app.R;
import com.tabibe.app.model.Patient;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SiblingViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.delete)
    ImageView delete;

    @BindView(R.id.edit)
    ImageView edit;

    @BindView(R.id.user_name)
    TextView userName;



    public SiblingViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void onBind(Patient userResponse , SiblingEditClicked siblingEditClicked){

        if(userResponse.getIs_parent().contains("Y")){
            edit.setVisibility(View.INVISIBLE);
            delete.setVisibility(View.INVISIBLE);
        }
        userName.setText(userResponse.getFirst_name()+" "+userResponse.getLast_name());

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                siblingEditClicked.editSibling(userResponse);

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                siblingEditClicked.deleteSibling(userResponse);

            }
        });

    }
}
