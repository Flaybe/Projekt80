package com.example.projekt80;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

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
import com.example.projekt80.json.Friends;
import com.example.projekt80.json.User;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class EventInfoFragment extends Fragment {

    private FragmentEventInfoBinding binding;

    private Event event;

    private User user;

    private final StringBuilder info = new StringBuilder();

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

        getFriends(getContext(), new FriendsCallback() {
            @Override
            public void onSuccess(Friends friends) {
                binding.members.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.members.setAdapter(new MembersAdapter(event.getMembers(), user, friends));
            }
            @Override
            public void onError(String message) {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        });

        info.delete(0, info.length());
        info.append("Name: ").append(event.getName()).append("\n\n");
        info.append("Description: ").append(event.getDescription()).append("\n\n");
        info.append("Creator: ").append(event.getCreator());
        getLikes(event.getName());


        binding.infoText.setText(info.toString());

        if (event.getMembers().contains(user.getName())) {
            binding.joinButton.setText("Leave Event");
        }

        binding.joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(event.getMembers().contains(user.getName())){
                    leaveEvent(event.getName(), binding.getRoot());
                } else {
                    joinEvent(event.getName());
                }

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


    private void getFriends(Context context, FriendsCallback callback) {

        String url = LoginFragment.AZURE + "/user/friends";

        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            Log.d("Friends", response);
            Friends friends = gson.fromJson(response, Friends.class);
            callback.onSuccess(friends);

        }, error -> {
            Log.e("Friends", error.toString());
            callback.onError(error.toString());
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


    private void joinEvent(String eventName){
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = LoginFragment.AZURE + "event/join/" + eventName;

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("join", response);
                    NavHostFragment.findNavController(EventInfoFragment.this)
                            .navigate(EventInfoFragmentDirections.actionEventInfoFragmentToEventFragment(event, user));
                    Toast.makeText(requireContext(), "Joined event", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(requireContext(), "Error joining event", Toast.LENGTH_SHORT).show();
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + user.getAccessToken());
                return headers;
            }
        };
        queue.add(request);

    }
    private void leaveEvent(String eventName, View view){
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = LoginFragment.AZURE + "event/leave/" + eventName;

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("leave", response);
                    NavController navController = Navigation.findNavController(view);
                    navController.popBackStack();
                    Toast.makeText(requireContext(), "Left event", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(requireContext(), "Error leaving event", Toast.LENGTH_SHORT).show();
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + user.getAccessToken());
                return headers;
            }
        };
        queue.add(request);
    }

}

