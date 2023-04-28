package com.example.projekt80;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projekt80.adapters.FriendAdapter;
import com.example.projekt80.databinding.FragmentFriendsBinding;
import com.example.projekt80.json.Friends;
import com.example.projekt80.json.User;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;


public class FriendsFragment extends Fragment {

    private FragmentFriendsBinding binding;
    private User user;
    private final Gson gson = new Gson();

    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFriendsBinding.inflate(inflater, container, false);
        FriendsFragmentArgs args = FriendsFragmentArgs.fromBundle(getArguments());
        user = args.getUser();

        binding.friendList.setLayoutManager(new LinearLayoutManager(getContext()));

        if(binding.friendRequests.isChecked()){
            getRequests();
        } else {
            getFriends();
        }

        binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.searchbarFriend.getText().toString();
                RequestQueue queue = Volley.newRequestQueue(getContext());
                String url = LoginFragment.AZURE + "/user/friends/request/" + username;

                StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
                    Log.d("Friends", response);
                    Toast.makeText(getContext(), "Friend request sent to: " + username, Toast.LENGTH_SHORT).show();
                }, error -> {
                    Log.e("Friends", error.toString());
                    Toast.makeText(getContext(), "Friend not found", Toast.LENGTH_SHORT).show();
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        Log.d("Friends", user.getAccessToken());
                        headers.put("Authorization", "Bearer " + user.getAccessToken());
                        return headers;
                    }
                };
                queue.add(request);
            }
        });

        binding.friendRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.friendRequests.isChecked()){
                    getRequests();
                } else {
                    getFriends();
                }
            }
        });

        return binding.getRoot();
    }


    private void getFriends() {
        String url = LoginFragment.AZURE + "/user/friends";

        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            Log.d("Friends", response);
            Friends friends = gson.fromJson(response, Friends.class);
            binding.friendList.setAdapter(new FriendAdapter(friends.getFriends()));

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


    private void getRequests(){
        String url = LoginFragment.AZURE + "/user/friends/requests";

        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            Log.d("Requests", response);
            Friends friends = gson.fromJson(response, Friends.class);
            binding.friendList.setAdapter(new FriendAdapter(friends.getFriends()));

        }, error -> {
            Log.e("Friends", error.toString());
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                Log.d("Friends", user.getAccessToken());
                headers.put("Authorization", "Bearer " + user.getAccessToken());
                return headers;
            }
        };
        queue.add(request);
    }

}