package com.example.projekt80;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.projekt80.json.User;
import com.example.projekt80.databinding.FragmentCreateEventBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class CreateEventFragment extends Fragment {

    private FragmentCreateEventBinding binding;
    private User user;

    public CreateEventFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        CreateEventFragmentArgs args = CreateEventFragmentArgs.fromBundle(getArguments());
        user = args.getUser();

        binding = FragmentCreateEventBinding.inflate(inflater, container, false);

        binding.createButton.setOnClickListener(view -> {
            RequestQueue queue = Volley.newRequestQueue(getContext());
            String url = LoginFragment.AZURE + "/event/create";

            JSONObject json = new JSONObject();
            try {
                json.put("name", binding.editName.getText().toString());
                json.put("description", binding.editDescription.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json, response -> {
                Log.i("Response", response.toString());
                Toast.makeText(getContext(), "Event created", Toast.LENGTH_SHORT).show();

                // detta gör så man inte kommer tillbaka hit när man trycker back
                NavHostFragment.findNavController(CreateEventFragment.this)
                        .popBackStack(R.id.eventListFragment, false);

            }, error -> {
                Log.e("Error", error.toString());
            })
            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + user.getAccessToken());
                    return headers;
                }
            };
            queue.add(jsonObjectRequest);
        });

        return binding.getRoot();
    }
}