package com.example.projekt80.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekt80.R;
import com.example.projekt80.json.Event;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {


    private final ArrayList<Event> events;

    public EventAdapter(ArrayList<Event> events) {
        this.events = events;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_layout, parent, false);

        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {

        TextView name = holder.itemView.findViewById(R.id.eventName);
        TextView description = holder.itemView.findViewById(R.id.description);
        TextView creator = holder.itemView.findViewById(R.id.creator);

        name.setText(events.get(position).getName());
        description.setText(events.get(position).getDescription());
        creator.setText(events.get(position).getCreator());

    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}
