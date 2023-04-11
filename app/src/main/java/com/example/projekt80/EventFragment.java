package com.example.projekt80;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projekt80.databinding.FragmentEventBinding;
import com.example.projekt80.json.Event;


public class EventFragment extends Fragment {

    private FragmentEventBinding binding;
    private Event event;


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

        // ska göra en request och hämta alla medelmmar i eventet och sedan skcika med det till adaptern


        binding = FragmentEventBinding.inflate(inflater, container, false);
        binding.eventNameLabel.setText(event.getName());

        return binding.getRoot();
    }
}