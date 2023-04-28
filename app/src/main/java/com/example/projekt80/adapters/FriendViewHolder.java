package com.example.projekt80.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekt80.R;

public class FriendViewHolder extends RecyclerView.ViewHolder{

    TextView name;

    public FriendViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.name);
    }


}
