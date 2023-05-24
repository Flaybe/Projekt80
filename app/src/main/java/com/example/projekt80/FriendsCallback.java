package com.example.projekt80;

import com.example.projekt80.json.Friends;

import java.util.List;

public interface FriendsCallback {
    /**
     * Callback till när vi har fått svar från requesten i getOnlineFriends
     * @param friends
     */
    void onSuccess(Friends friends);
    void onError(String message);
}
