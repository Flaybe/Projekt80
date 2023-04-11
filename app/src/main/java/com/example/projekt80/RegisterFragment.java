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
import com.example.projekt80.databinding.FragmentRegisterBinding;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;


public class RegisterFragment extends Fragment {

    FragmentRegisterBinding binding;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding = FragmentRegisterBinding.inflate(inflater, container, false);
         binding.haveAccount.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                NavHostFragment.findNavController(RegisterFragment.this)
                          .popBackStack(R.id.loginFragment, false);
              }
         });
         binding.registerButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  String url = LoginFragment.AZURE + "/user/register";
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

                              Toast.makeText(getContext(), "User created", Toast.LENGTH_SHORT).show();

                              NavHostFragment.findNavController(RegisterFragment.this)
                                      .navigate(R.id.action_registerFragment_to_loginFragment);
                          },
                          error -> {
                              Log.d("error", error.toString());
                              AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                              builder.setTitle("Error");
                              builder.setMessage("Username already exists");
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