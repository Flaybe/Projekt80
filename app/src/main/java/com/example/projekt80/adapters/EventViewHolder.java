package com.example.projekt80.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekt80.R;

public class EventViewHolder extends RecyclerView.ViewHolder {

    TextView eventName;
    TextView description;
    TextView creator;

    public EventViewHolder(@NonNull View itemView) {
        super(itemView);
        eventName = itemView.findViewById(R.id.eventName);
        description = itemView.findViewById(R.id.description);
        creator = itemView.findViewById(R.id.creator);

    }
}
