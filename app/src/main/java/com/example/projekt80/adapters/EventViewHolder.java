package com.example.projekt80.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekt80.R;

public class EventViewHolder extends RecyclerView.ViewHolder {

    public TextView eventName;
    public TextView info;

    public EventViewHolder(@NonNull View itemView) {
        super(itemView);
        eventName = itemView.findViewById(R.id.eventName);
        info = itemView.findViewById(R.id.info);

    }
}
