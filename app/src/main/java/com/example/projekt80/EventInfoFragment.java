package com.example.projekt80;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projekt80.adapters.EventAdapter;
import com.example.projekt80.adapters.MembersAdapter;
import com.example.projekt80.databinding.FragmentEventInfoBinding;
import com.example.projekt80.json.Event;
import com.example.projekt80.json.Events;
import com.example.projekt80.json.User;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EventInfoFragment extends Fragment {

    private FragmentEventInfoBinding binding;

    private Event event;

    private User user;

    private StringBuilder info = new StringBuilder();

    private final Gson gson = new Gson();

    public EventInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentEventInfoBinding.inflate(inflater, container, false);
        EventInfoFragmentArgs args = EventInfoFragmentArgs.fromBundle(getArguments());
        event = args.getEvent();
        user = args.getUser();


        info.delete(0, info.length());
        info.append("Name: ").append(event.getName()).append("\n\n");
        info.append("Description: ").append(event.getDescription()).append("\n\n");
        info.append("Creator: ").append(event.getCreator());
        getLikes(event.getName());

        binding.members.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.members.setAdapter(new MembersAdapter(event.getMembers(), user));
        binding.infoText.setText(info.toString());

        binding.joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(EventInfoFragment.this)
                        .navigate(EventInfoFragmentDirections.actionEventInfoFragmentToEventFragment(event, user));

            }
        });

        binding.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(getContext());

                String url = LoginFragment.AZURE + "/event/like/" + event.getName();
                StringRequest request = new StringRequest(Request.Method.POST, url,
                        response -> {
                            Log.d("like", response);
                            getLikes(event.getName());
                        },
                        error -> {
                            error.printStackTrace();
                        }
                ) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Authorization", "Bearer " + user.getAccessToken());
                        return headers;
                    }
                };
                queue.add(request);
            }
        });

        return binding.getRoot();
    }


    private void getLikes(String name) {

        RequestQueue queue = Volley.newRequestQueue(getContext());

        String url = LoginFragment.AZURE + "/event/likes/" + name;
        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    Log.d("likes", response);
                    Event event2 = gson.fromJson(response, Event.class);
                    Log.d("likes", event2.getLikes().toString());
                    binding.likesText.setText("Likes: " + event2.getLikes().size());
                },
                error -> {
                    error.printStackTrace();
                }
        ) {
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