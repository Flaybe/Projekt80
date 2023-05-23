package com.example.projekt80.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projekt80.EventListFragmentDirections;
import com.example.projekt80.R;
import com.example.projekt80.json.Event;
import com.example.projekt80.json.User;
import java.util.ArrayList;


/**
    * Hanterar klick på event i eventlistfragment samt klick på info
 * och skickar oss till rätt info sida
 */

public class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {


    private final ArrayList<Event> events;
    private User user;


    public EventAdapter(ArrayList<Event> events, User user) {
        this.events = events;
        this.user = user;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_layout, parent, false);
        EventViewHolder holder = new EventViewHolder(itemView);



        holder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event here
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    // Get the event object at the clicked position
                    Event event = events.get(position);
                    // Navigate to the event fragment
                    EventListFragmentDirections.ActionEventListFragmentToEventInfoFragment action =
                            EventListFragmentDirections.actionEventListFragmentToEventInfoFragment(event, user);
                    NavController navController = Navigation.findNavController(v);
                    navController.navigate(action);
                }
            }
        });

        // Set onClickListener to the whole border
        holder.eventName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event here
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    // Get the event object at the clicked position
                    Event event = events.get(position);
                    // Navigate to the event fragment
                    EventListFragmentDirections.ActionEventListFragmentToEventFragment action =
                            EventListFragmentDirections.actionEventListFragmentToEventFragment(event, user);
                    NavController navController = Navigation.findNavController(v);
                    navController.navigate(action);
                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {

        TextView name = holder.itemView.findViewById(R.id.eventName);
        name.setText(events.get(position).getName());

    }

    @Override
    public int getItemCount() {
        if (events == null) {
            return 0;
        }
        return events.size();
    }
}
