package com.example.projekt80;

import com.example.projekt80.json.OnlineMembers;

public interface OnlineMembersCallback {
    void onSuccess(OnlineMembers onlineMembers);
    void onError(String message);
}
