package com.example.projekt80;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projekt80.json.User;
import com.example.projekt80.databinding.FragmentHomeBinding;

import java.util.HashMap;
import java.util.Map;


public class HomeFragment extends Fragment {

    /**
     * Fragment som visar användarens namn och har knappar för att navigera till event och friends
     * Gör nätverksanrop för att logga ut användaren
     */

    private FragmentHomeBinding binding;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        HomeFragmentArgs args = HomeFragmentArgs.fromBundle(getArguments());
        User user = args.getUser();
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        // Sätter användarens användarnam som "titel" på skärmen
        binding.username.setText(user.getName());

        // EVENTS BUTTON_____________________________________________________________________________________
        binding.events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(HomeFragmentDirections.actionHomeFragmentToEventFragment(user));
            }
        });

        // FRIENDS BUTTON_____________________________________________________________________________________
        binding.friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(HomeFragmentDirections.actionHomeFragmentToFriendsFragment(user));
            }
        });



        // SIGNOUT BUTTON_____________________________________________________________________________________
        binding.signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut(user);
            }
        });

        return binding.getRoot();
    }



    private void signOut(User user){
        /*
        Loggar ut oss genom att skicka en request och tar oss tillbaka till loginfragment
         */
        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, LoginFragment.AZURE + "/user/logout",
                response -> {
                    Log.d("Response", response);
                }, error -> Log.d("Error", error.toString())
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + user.getAccessToken());
                return headers;
            }
        };

        NavHostFragment.findNavController(HomeFragment.this)
                .navigate(R.id.loginFragment);

        queue.add(stringRequest);
    }
}