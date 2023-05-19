package com.example.projekt80;

import com.example.projekt80.json.OnlineFriends;

public interface OnlineFriendsCallback {

    void onSuccess(OnlineFriends onlineFriends);
    void onError(String message);
}
