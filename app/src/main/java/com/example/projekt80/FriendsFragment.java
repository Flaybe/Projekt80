package com.example.projekt80;

import android.content.Context;
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
import com.example.projekt80.json.OnlineFriends;
import com.example.projekt80.json.User;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;


public class FriendsFragment extends Fragment {
    /**
     * Displayar antingen alla vänner en användare har, eller alla friend requests som användaren
     * har fått. Hämtar all data med nätverksanrop. Displayar den igenom att skicka med den till
     * en adapter.
     */
    private FragmentFriendsBinding binding;
    private User user;
    private final Gson gson = new Gson();
    private boolean requests = false;
    private OnlineFriends onlineFriends;

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

        getOnlineFriends(getContext(), new OnlineFriendsCallback() {
            /*
            * Callback till när vi har fått svar från requesten i getOnlineFriends
             */
            @Override
            public void onSuccess(OnlineFriends onlineFriends) {
                if(binding.friendRequests.isChecked()){
                    requests = true;
                    getRequests(onlineFriends);
                } else {
                    requests = false;
                    getFriends(onlineFriends);
                }
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });



        binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                 * Skapar en request till servern för att skicka ett friend request till en användare
                 * Om användaren inte finns så kommer vi få en Toast som säger att användaren inte finns.
                 */
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
            /*
            * Om vi klickar på friendrequest checkboxen så kommer vi displaya antingen
            * friend requests eller friends beroende på om den är iklickad eller inte.
             */
            @Override
            public void onClick(View v) {
                if(binding.friendRequests.isChecked()){
                    requests = true;
                    getRequests(onlineFriends);
                } else {
                    requests = false;
                    getFriends(onlineFriends);
                }
            }
        });

        return binding.getRoot();
    }


    private void getFriends(OnlineFriends onlineFriends) {
        /*
        * skapar ett server request för att hämta alla vänner som användaren har. Använder sig av
        * Gson för att konvertera json till java objekt.
        * Skapar en adapter för att kunna visa alla vänner i en lista.
         */
        binding.foundFriendsText.setText("Friends");

        String url = LoginFragment.AZURE + "/user/friends";

        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            Log.d("Friends", response);
            Friends friends = gson.fromJson(response, Friends.class);
            binding.friendList.setAdapter(new FriendAdapter(friends.getFriends(), requests, user, onlineFriends));

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


    private void getRequests(OnlineFriends onlineFriends){
        /*
        * skapar ett server request för att hämta alla friendrequests en viss användare har,
        * sätter sedan adaptern med all hämtad data och vilka användare som är online som den
        * har fått som argument
         */
        binding.foundFriendsText.setText("Friend Requests");

        String url = LoginFragment.AZURE + "/user/friends/requests";

        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            Friends friends = gson.fromJson(response, Friends.class);
            Log.d("Requests", friends.getFriends().toString());
            binding.friendList.setAdapter(new FriendAdapter(friends.getFriends(), requests, user, onlineFriends));

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

    public void getOnlineFriends(Context context, OnlineFriendsCallback callback){
        /*
        Hämtar alla vänner som är online och sätter den globala varabeln onlinefriends till
        de värdet och kallar på sin onSuccess function så vår för att fortsätta hämta annan data

         */
        System.out.println("testing onlineFriends");
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = LoginFragment.AZURE + "/user/friends/online";
        StringRequest request = new StringRequest(com.android.volley.Request.Method.GET, url,
                response -> {
                    Gson gson = new Gson();
                    onlineFriends = gson.fromJson(response, OnlineFriends.class);
                    callback.onSuccess(onlineFriends);
                }, error -> {
            callback.onError(error.toString());
        })            {
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