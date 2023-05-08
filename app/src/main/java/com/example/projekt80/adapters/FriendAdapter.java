package com.example.projekt80.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekt80.R;
import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendViewHolder>{


    private final List<String> friends;

    public FriendAdapter(List<String> friends) {
        this.friends = friends;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_layout, parent, false);
        return new FriendViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        holder.name.setText(friends.get(position));
    }

    @Override
    public int getItemCount() {
        if (friends != null) {
            return friends.size();
        } else {
            return 0;
        }
    }
}
