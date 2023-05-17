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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projekt80.LoginFragment;
import com.example.projekt80.R;
import com.example.projekt80.databinding.FragmentFriendsBinding;
import com.example.projekt80.json.Friends;
import com.example.projekt80.json.User;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MembersViewHolder> {

    private final List<String> members;
    private Friends friends;
    private final Gson gson = new Gson();
    private final User user;

    public MembersAdapter(List<String> members, User user) {
        this.members = members;
        this.user = user;
    }

    @NonNull
    @Override
    public MembersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view for each item in the RecyclerView
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_layout, parent, false);
        Log.d("Boolean", String.valueOf(friends != null));
        getFriends(parent.getContext());
        return new MembersViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MembersViewHolder holder, int position) {
        // Bind the data to the views in each item
        String member = members.get(position);
        holder.name.setText(member);

        if(friends != null) {
            if (friends.getFriends().contains(member)) {
                holder.accept_button.setVisibility(View.GONE);
            }if(member.equals(user.getName())){
                holder.accept_button.setText("You");
            }
            else{
                holder.accept_button.setVisibility(View.VISIBLE);
                holder.accept_button.setOnClickListener(v -> sendFriendRequest(v.getContext(), member, holder));
            }
        }
    }

    private void sendFriendRequest(Context context, String member, MembersViewHolder holder) {
                String url = LoginFragment.AZURE + "/user/friends/request/" + member;

                RequestQueue queue = Volley.newRequestQueue(context);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        response -> {
                            Log.d("response", response);
                            Toast.makeText(context, "Friend request sent", Toast.LENGTH_SHORT).show();
                            holder.accept_button.setText("Sent");
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

    @Override
    public int getItemCount() {
        // Return the size of the data list
        return members.size();
    }

    // ViewHolder class to hold references to the views in each item
    public static class MembersViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView accept_button;

        public MembersViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.friend_name);
            accept_button = itemView.findViewById(R.id.accept_button);
        }
    }

    private void getFriends(Context context) {

        String url = LoginFragment.AZURE + "/user/friends";

        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            Log.d("Friends", response);
            friends = gson.fromJson(response, Friends.class);

        }, error -> {
            Log.e("Friends", error.toString());
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + user.getAccessToken());
                return headers;
            }
        };
        queue.add(request);
    }
}
