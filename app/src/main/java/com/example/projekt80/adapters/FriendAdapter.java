package com.example.projekt80.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projekt80.LoginFragment;
import com.example.projekt80.R;
import com.example.projekt80.json.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendAdapter extends RecyclerView.Adapter<FriendViewHolder>{


    private final List<String> friends;
    private boolean requests;
    private User user;

    public FriendAdapter(List<String> friends, boolean requests, User user) {
        this.friends = friends;
        this.requests = requests;
        this.user = user;

    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_layout, parent, false);
        FriendViewHolder holder = new FriendViewHolder(itemView);
        Log.d("requests", String.valueOf(requests));
        if (requests) {
            holder.accept_button.setVisibility(View.VISIBLE);

            holder.accept_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    acceptFriend(holder, v);
                }
            });
        } else {
            holder.accept_button.setVisibility(View.GONE);
            holder.dismiss_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeFriend(holder, v);
                }
            });
        }
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        holder.friendName.setText(friends.get(position));
    }

    @Override
    public int getItemCount() {
        if (friends != null) {
            Log.d("count", String.valueOf(friends.size()));
            return friends.size();
        } else {
            return 0;
        }
    }


    private void removeFriend(FriendViewHolder holder, View v){
        int position = holder.getAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
            String friend = friends.get(position);
            String url = LoginFragment.AZURE + "/user/friends/remove/" + friend;

            RequestQueue queue = Volley.newRequestQueue(v.getContext());

            StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, url,
                    response -> {
                        Log.d("response", response);
                        Toast.makeText(v.getContext(), "Friend removed", Toast.LENGTH_SHORT).show();
                        friends.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, friends.size());
                    }, error -> {
                Log.d("error", error.toString());
            })            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + user.getAccessToken());
                    return headers;
                }
            };
            queue.add(stringRequest);
        }
    }

    private void acceptFriend(FriendViewHolder holder, View v) {
        int position = holder.getAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
            String friend = friends.get(position);
            String url = LoginFragment.AZURE + "/user/friends/accept/" + friend;

            RequestQueue queue = Volley.newRequestQueue(v.getContext());

            StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, url,
                    response -> {
                        Log.d("response", response);
                        Toast.makeText(v.getContext(), "Friend request accepted", Toast.LENGTH_SHORT).show();
                        friends.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, friends.size());
                    }, error -> {
                Log.d("error", error.toString());
            })            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + user.getAccessToken());
                    return headers;
                }
            };
            queue.add(stringRequest);
        }
    }
}
