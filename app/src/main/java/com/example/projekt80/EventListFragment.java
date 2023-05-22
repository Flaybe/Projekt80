package com.example.projekt80;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projekt80.adapters.EventAdapter;
import com.example.projekt80.databinding.FragmentEventListBinding;
import com.example.projekt80.json.User;
import com.example.projekt80.json.Events;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;


public class EventListFragment extends Fragment {

    private FragmentEventListBinding binding;
    private Gson gson = new Gson();
    private User user;

    public EventListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EventListFragmentArgs args = EventListFragmentArgs.fromBundle(getArguments());
        user = args.getUser();

        binding = FragmentEventListBinding.inflate(inflater, container, false);

        RecyclerView eventList = binding.eventList;
        eventList.setLayoutManager(new LinearLayoutManager(getContext()));

        if (binding.joinedEvents.isChecked()) {
            getJoinedEvents();
        } else {
            getAllEvents();
        }

        binding.joinedEvents.setOnClickListener(new View.OnClickListener() {
            /*
                Hämtar alla events man är med i. Samt kollar om en checkbox med
                 id joinedEvents är iklickad så att den ska veta om den ska displaya alla events
                 eller bara de man är med i.
             */
            @Override
            public void onClick(View v) {
                if (binding.joinedEvents.isChecked()) {
                    getJoinedEvents();
                } else {
                    getAllEvents();
                }
            }
        });

        binding.createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //navigerar till create event fragmentet
                NavHostFragment.findNavController(EventListFragment.this)
                        .navigate(EventListFragmentDirections.actionEventListFragmentToCreateEventFragment(user));
            }
        });

        return binding.getRoot();
    }


    private void getAllEvents(){
        /*
            Skapar en request för att hämta alla events. Skickar med en lista av events till
            eventadaptern.
         */
        RequestQueue queue = Volley.newRequestQueue(getContext());

        String url = LoginFragment.AZURE + "/event/all";
        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    Log.d("Events", response);
                    Events events = gson.fromJson(response, Events.class);
                    binding.eventList.setAdapter(new EventAdapter(events.getEvents(), user));
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

    private void getJoinedEvents(){
        /*
            Skapar ett request för att hämta alla events som en user är med i.
            Skickar med listan av events till eventadaptern.
         */
        RequestQueue queue = Volley.newRequestQueue(getContext());

        String url = LoginFragment.AZURE + "/user/" + user.getName() + "/events";
        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    Log.d("Events", response);
                    Events events = gson.fromJson(response, Events.class);
                    binding.eventList.setAdapter(new EventAdapter(events.getEvents(), user));
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