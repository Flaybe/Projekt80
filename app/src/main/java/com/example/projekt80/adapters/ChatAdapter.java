package com.example.projekt80.adapters;


import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekt80.R;
import com.example.projekt80.json.Message;
import com.example.projekt80.json.User;

import java.util.ArrayList;
import java.util.Objects;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder>{

    private final ArrayList<Message> messages;

    private User user;

    public ChatAdapter(ArrayList<Message> messages, User user) {
        this.messages = messages;
        this.user = user;
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
        TextView message = holder.itemView.findViewById(R.id.message);
        TextView author = holder.itemView.findViewById(R.id.author);
        Log.d("Message", messages.get(position).getText());
        message.setText(messages.get(position).getText());

        if(Objects.equals(messages.get(position).getAuthor(), user.getName())){
            message.setGravity(Gravity.RIGHT);
            holder.itemView.findViewById(R.id.chatLayout).setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }else{
            message.setGravity(Gravity.LEFT);
            holder.itemView.findViewById(R.id.chatLayout).setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            author.setText(messages.get(position).getAuthor());
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
