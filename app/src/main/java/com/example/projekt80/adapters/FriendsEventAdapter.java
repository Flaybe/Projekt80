package com.example.projekt80.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekt80.FriendsActivity;
import com.example.projekt80.FriendsActivityDirections;
import com.example.projekt80.R;
import com.example.projekt80.json.Event;
import com.example.projekt80.json.User;

import java.util.List;

public class FriendsEventAdapter extends RecyclerView.Adapter<FriendsEventAdapter.FriendsEventViewHolder> {

    private List<Event> events;
    private User user;

    public FriendsEventAdapter(List<Event> events, User user) {
        this.events = events;
        this.user = user;
    }

    @NonNull
    @Override
    public FriendsEventAdapter.FriendsEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_layout, parent, false);
        FriendsEventViewHolder holder = new FriendsEventViewHolder(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsEventAdapter.FriendsEventViewHolder holder, int position) {
        Event event = events.get(position);
        holder.eventName.setText(event.getName());
        holder.info.setOnClickListener(v ->
            navToEventInfo(v, holder)
        );
        holder.eventName.setOnClickListener(v ->
            navToEventChat(v, holder)
        );
    }

    @Override
    public int getItemCount() {
        if (events == null)
            return 0;
        else
            return events.size();
    }

    public static class FriendsEventViewHolder extends RecyclerView.ViewHolder {
        TextView eventName;
        TextView info;

        public FriendsEventViewHolder(@NonNull View parent) {
            super(parent);
            eventName = parent.findViewById(R.id.eventName);
            info = parent.findViewById(R.id.info);
        }
    }
    public void navToEventChat(View view, FriendsEventViewHolder holder) {
        int position = holder.getAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
            Event event = events.get(position);
            FriendsActivityDirections.ActionFriendsActivityToEventFragment action =
                    FriendsActivityDirections.actionFriendsActivityToEventFragment(event, user);

            NavController navController = Navigation.findNavController(view);
            navController.navigate(action);
        }

    }
    public void navToEventInfo(View view, FriendsEventViewHolder holder){
        int position = holder.getAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
            Event event = events.get(position);
            FriendsActivityDirections.ActionFriendsActivityToEventInfoFragment action =
                    FriendsActivityDirections.actionFriendsActivityToEventInfoFragment(event, user);

            NavController navController = Navigation.findNavController(view);
            navController.navigate(action);
        }
    }

}

