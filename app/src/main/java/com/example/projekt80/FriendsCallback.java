package com.example.projekt80;

import com.example.projekt80.json.Friends;

import java.util.List;

public interface FriendsCallback {
    void onSuccess(Friends friends);
    void onError(String message);
}
