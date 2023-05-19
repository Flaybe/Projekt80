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
import com.example.projekt80.OnlineMembersCallback;
import com.example.projekt80.R;
import com.example.projekt80.databinding.FragmentFriendsBinding;
import com.example.projekt80.json.Friends;
import com.example.projekt80.json.OnlineMembers;
import com.example.projekt80.json.User;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MembersViewHolder> {
    private final List<String> members;
    private List<String> onlineMembersGlobal = new ArrayList<>();
    private Friends friends;
    private final Gson gson = new Gson();
    private final User user;
    private final String event;

    public MembersAdapter(List<String> members, User user, Friends friends, String event) {
        this.members = members;
        this.user = user;
        this.friends = friends;
        this.event = event;
    }

    @NonNull
    @Override
    public MembersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view for each item in the RecyclerView
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_layout, parent, false);
        MembersViewHolder holder = new MembersViewHolder(itemView);
        onlineMembers(itemView, new OnlineMembersCallback() {
            @Override
            public void onSuccess(OnlineMembers onlineMembers) {
                onlineMembersGlobal = onlineMembers.getMembers();
                for (int i = 0;i<getItemCount() ;i++){
                    onBindViewHolder(holder, i);
                }
            }
            @Override
            public void onError(String message) {
                Log.d("error", message);
            }
        });

        return new MembersViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MembersViewHolder holder, int position) {
        // Bind the data to the views in each item
        String member = members.get(position);
        holder.name.setText(member);

        if (friends != null) {
            holder.dismiss_button.setVisibility(View.GONE);
            if (friends.getFriends().contains(member)) {
                holder.accept_button.setVisibility(View.GONE);
            }
            if (member.equals(user.getName())) {
                holder.accept_button.setText("You");
            } else {
                holder.accept_button.setVisibility(View.VISIBLE);
                holder.accept_button.setOnClickListener(v -> sendFriendRequest(v.getContext(), member, holder));
            }if (onlineMembersGlobal != null) {
                if (onlineMembersGlobal.contains(member)) {
                    holder.name.setBackgroundResource(R.drawable.online);
                } else {
                    holder.name.setBackgroundResource(R.drawable.offline);
                }
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
        }) {
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
        TextView dismiss_button;

        public MembersViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.friend_name);
            accept_button = itemView.findViewById(R.id.accept_button);
            dismiss_button = itemView.findViewById(R.id.dismiss);
        }
    }
    public void onlineMembers(View v, OnlineMembersCallback callback){
        RequestQueue queue = Volley.newRequestQueue(v.getContext());
        String url = LoginFragment.AZURE + "/user/members/online/" + event ;
        System.out.println("testing online members");
        StringRequest request = new StringRequest(com.android.volley.Request.Method.GET, url,
                response -> {;
                    OnlineMembers onlineMembers = gson.fromJson(response, OnlineMembers.class);
                    System.out.println(onlineMembers.getMembers());
                    callback.onSuccess(onlineMembers);

                }, error -> {
                    callback.onError(error.getMessage());
        })
        {
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