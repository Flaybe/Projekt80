package com.example.projekt80;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projekt80.adapters.ChatAdapter;
import com.example.projekt80.databinding.FragmentEventBinding;
import com.example.projekt80.json.Event;
import com.example.projekt80.json.Messages;
import com.example.projekt80.json.User;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class EventFragment extends Fragment {

    private FragmentEventBinding binding;
    private Event event;
    private User user;
    private Messages messages;
    private final Gson gson = new Gson();
    private RequestQueue queue;


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
        EventFragmentArgs args = EventFragmentArgs.fromBundle(getArguments());
        event = args.getEvent();
        user = args.getUser();
        queue = Volley.newRequestQueue(getContext());

        // ska göra en request och hämta alla medelanden i eventet och sedan skcika med det till adaptern

        binding = FragmentEventBinding.inflate(inflater, container, false);
        binding.eventNameLabel.setText(event.getName());
        binding.chat.setLayoutManager(new LinearLayoutManager(getContext()));
        this.getMessages(binding.chat);


        binding.sendButton.setOnClickListener(view -> {
            this.sendMessage(binding.messageText.getText().toString());
        });

        return binding.getRoot();
    }


    private void sendMessage(String message){
        String url = LoginFragment.AZURE + "/event/send/" + event.getName();

        JSONObject json = new JSONObject();
        try {
            json.put("text", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, json,
                response -> {
                    try {
                        String value = response.get("response").toString();
                        if(value.equals("User not in event")){
                            showChoiceDialog("You are not in this event. Do you want to join?", message);
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    Log.d("Response", response.toString());
                    this.getMessages(binding.chat);
                    binding.messageText.setText("");
                    // drop keyboard
                    // Get the InputMethodManager
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

                    // Hide the keyboard
                    imm.hideSoftInputFromWindow(binding.messageText.getWindowToken(), 0);


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



    private void getMessages(RecyclerView recyclerView){

        String url = LoginFragment.AZURE + "/event/messages/" + event.getName();

        JSONObject json = new JSONObject();
        try {
            json.put("name", event.getName());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, json,
                response -> {
                    messages = gson.fromJson(response.toString(), Messages.class);
                    Log.d("messages", messages.getMessages().toString());
                    recyclerView.setAdapter(new ChatAdapter(messages.getMessages(), user));
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

    private void showChoiceDialog(String prompt, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Join Event")
                .setMessage(prompt)
                .setPositiveButton("Join", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String url = LoginFragment.AZURE + "/event/join/" + event.getName();

                        StringRequest request = new StringRequest(Request.Method.POST, url,
                                response -> {
                                    Toast.makeText(getContext(), "Event joined", Toast.LENGTH_SHORT).show();
                                    sendMessage(message);
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
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


}