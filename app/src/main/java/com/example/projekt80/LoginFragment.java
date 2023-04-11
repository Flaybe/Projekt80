package com.example.projekt80;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.projekt80.json.User;
import com.example.projekt80.databinding.FragmentLoginBinding;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    public final static String AZURE = "http://192.168.0.30:5000/";
    private Gson gson = new Gson();

    public LoginFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(LoginFragment.this)
                        .navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ska göra url request
                String url = AZURE + "/user/login";

                RequestQueue queue = Volley.newRequestQueue(getContext());

                JSONObject json = new JSONObject();
                try {
                    json.put("name", binding.TextPersonName.getText().toString());
                    json.put("password", binding.TextPassword.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json,
                        response -> {
                            Log.d("response", response.toString());
                            try {
                                String AccessToken = response.get("access_token").toString();
                                User user = gson.fromJson(json.toString(), User.class);
                                user.setAccessToken(AccessToken);

                                Toast.makeText(getContext(), "Welcome " + user.getName(), Toast.LENGTH_SHORT).show();

                                LoginFragmentDirections.ActionLoginFragmentToHomeFragment action =
                                        LoginFragmentDirections.actionLoginFragmentToHomeFragment(user);

                                NavHostFragment.findNavController(LoginFragment.this)
                                        .navigate(action);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        error -> {
                            Log.d("error", error.toString());
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Error");
                            builder.setMessage("Wrong username or password");
                            builder.setPositiveButton("OK", null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        });
                queue.add(jsonObjectRequest);
            }
        });
        return binding.getRoot();
    }
}