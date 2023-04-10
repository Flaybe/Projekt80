package com.example.projekt80;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projekt80.databinding.FragmentHomeBinding;


public class HomeFragment extends Fragment {

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
                RequestQueue queue = Volley.newRequestQueue(getContext());

                StringRequest stringRequest = new StringRequest(Request.Method.POST, LoginFragment.AZURE + "/user/logout",
                        response -> {
                            Log.d("Response", response);
                        }, error -> Log.d("Error", error.toString()));

                NavHostFragment.findNavController(HomeFragment.this)
                        .popBackStack(R.id.loginFragment, false);
            }
        });

        return binding.getRoot();
    }
}