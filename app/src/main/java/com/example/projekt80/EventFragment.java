package com.example.projekt80;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
import com.example.projekt80.databinding.FragmentEventBinding;
import com.example.projekt80.json.Event;
import com.example.projekt80.json.Events;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class EventFragment extends Fragment {

    private FragmentEventBinding binding;
    private Gson gson = new Gson();

    public EventFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentEventBinding.inflate(inflater, container, false);

        RecyclerView eventList = binding.eventList;
        eventList.setLayoutManager(new LinearLayoutManager(getContext()));
        this.getEvents(eventList);

        return binding.getRoot();
    }


    private void getEvents(RecyclerView recyclerView){
        EventFragmentArgs args = EventFragmentArgs.fromBundle(getArguments());
        User user = args.getUser();

        RequestQueue queue = Volley.newRequestQueue(getContext());

        String url = LoginFragment.AZURE + "/event/all";
        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    Log.d("Events", response);
                    Events events = gson.fromJson(response, Events.class);
                    binding.eventList.setAdapter(new EventAdapter(events.getEvents()));
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