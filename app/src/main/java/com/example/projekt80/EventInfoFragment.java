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
import com.example.projekt80.json.OnlineMembers;
import com.example.projekt80.json.User;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class EventInfoFragment extends Fragment {
    /**
        Skapar eventInfofragmentet som innehåller en lista med medlemmarna i eventet.
        Sätter också de till röd/grön backgrund beroende på om de är online eller inte.
        Använder sig av callbacks för att veta när nätverksanropen är klara. 
        Sätter onClickListeners på Join/leave-event samt likeknappen.
    */
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
            //skapar en ny instans av ett interface vi har för hantera när nätverksanropet är klart
            //i och med att det tar lite tid. Den hämtar onlineFriends. Kör sedan en ny
            //callback som hämtar onlineMembers och skickar med den till FriendsAdapter.
            @Override
            public void onSuccess(Friends friends) {
                getOnlineMembers(getContext(), new OnlineMembersCallback() {
                    @Override
                    public void onSuccess(OnlineMembers onlineMembers) {
                        binding.members.setLayoutManager(new LinearLayoutManager(getContext()));
                        binding.members.setAdapter(new MembersAdapter(event.getMembers(), user, friends, event.getName(), onlineMembers));
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                    }
                });
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
                    leaveEvent();
                } else {
                    joinEvent();
                }

            }
        });

        binding.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                skapar ett request för att skicka en post request till servern
                för att likea eventet. Samt kör getLikes för att hämta antalet likes.
                 */
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
        /*
        skapar ett request för att hämta antalet likes för ett event.
        Samt sätter texten i likesText till antalet likes.
         */
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
        /*
        skapar ett request för att hämta användarens vänner. Använder sig av ett interface
        för att veta när nätverksanropet är klart. Använder sig av Gson för att konvertera json
        till java objekt.
         */
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

    public void getOnlineMembers(Context context, OnlineMembersCallback callback){
        /*
        skapar ett request för att hämta vilka medlemmar som är online i ett event.
         Använder sig av ett interface för att veta när anropet är klart. Använder sig av Gson för
         att konvertera json till java objekt.
         */
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = LoginFragment.AZURE + "/user/members/online/" + event.getName();
        StringRequest request = new StringRequest(com.android.volley.Request.Method.GET, url,
                response -> {;
                    OnlineMembers onlineMembers = gson.fromJson(response, OnlineMembers.class);
                    callback.onSuccess(onlineMembers);

                }, error -> callback.onError(error.getMessage())) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + user.getAccessToken());
                return headers;
            }
        };
        queue.add(request);
    }


    private void joinEvent(){
        /*
        skapar ett request för att skicka en post request som lägger till användaren till ett event
        samt navigerar tillbaka till eventfragmentet. Lägger också till användaren i eventet lokalt.
         */
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = LoginFragment.AZURE + "/event/join/" + event.getName();

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("join", response);
                    NavHostFragment.findNavController(EventInfoFragment.this)
                            .navigate(EventInfoFragmentDirections.actionEventInfoFragmentToEventFragment(event, user));
                    Toast.makeText(requireContext(), "Joined event", Toast.LENGTH_SHORT).show();
                    event.addMember(user.getName());
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
    private void leaveEvent(){
        /*
        skapar ett request för att lämna ett event. Navigerar tillbaka till senaste fragmentet.
         */
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = LoginFragment.AZURE + "/event/leave/" + event.getName();

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("leave", response);
                    NavController navController = Navigation.findNavController(EventInfoFragment.this.requireView());
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

