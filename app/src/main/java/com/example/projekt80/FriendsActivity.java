package com.example.projekt80;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projekt80.adapters.EventAdapter;
import com.example.projekt80.adapters.FriendsEventAdapter;
import com.example.projekt80.databinding.FragmentEventListBinding;
import com.example.projekt80.databinding.FragmentFriendsActivityBinding;
import com.example.projekt80.json.Events;
import com.example.projekt80.json.User;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class FriendsActivity extends Fragment {

    private FragmentFriendsActivityBinding binding;
    private String friendName;
    private User user;
    private Gson gson = new Gson();

    public FriendsActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FriendsActivityArgs args = FriendsActivityArgs.fromBundle(getArguments());
        friendName = args.getFriend();
        user = args.getUser();

        binding = FragmentFriendsActivityBinding.inflate(inflater, container, false);
        binding.name.setText(friendName);

        RecyclerView friendsEventList = binding.FriendEvents;
        friendsEventList.setLayoutManager(new LinearLayoutManager(getContext()));

        getFriendsEvents();


        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    private void getFriendsEvents() {
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = LoginFragment.AZURE + "/user/" + friendName + "/events";

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            Events events = gson.fromJson(response, Events.class);
            binding.FriendEvents.setAdapter(new FriendsEventAdapter(events.getEvents(), user));
            Log.d("Events", events.getEvents().toString());

        }, error -> {
            error.printStackTrace();
        }){
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + user.getAccessToken());
            return headers;
        }};
        queue.add(request);
    }
}