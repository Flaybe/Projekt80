package com.example.projekt80.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekt80.R;

public class FriendViewHolder extends RecyclerView.ViewHolder{

    TextView friendName;
    TextView accept_button;
    TextView dismiss_button;

    public FriendViewHolder(@NonNull View itemView) {
        super(itemView);
        friendName = itemView.findViewById(R.id.friend_name);
        accept_button = itemView.findViewById(R.id.accept_button);
        dismiss_button = itemView.findViewById(R.id.dismiss);
    }


}
