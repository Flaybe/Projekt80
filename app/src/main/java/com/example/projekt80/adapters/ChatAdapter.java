package com.example.projekt80.adapters;

import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekt80.R;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder>{

    private final ArrayList<Message> messages;

    public ChatAdapter(ArrayList<Message> messages) {
        this.messages = messages;
    }


    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_layout, parent, false);
        return new ChatViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        TextView textView = holder.itemView.findViewById(R.id.message);
        textView.setText(messages.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
