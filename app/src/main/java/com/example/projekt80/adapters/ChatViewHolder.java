package com.example.projekt80.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekt80.R;

public class ChatViewHolder extends RecyclerView.ViewHolder{

    TextView message;


    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);
        message = itemView.findViewById(R.id.message);
    }
}
