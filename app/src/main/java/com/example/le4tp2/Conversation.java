package com.example.le4tp2;

import com.google.gson.annotations.SerializedName;

public class Conversation {
    String id;
    @SerializedName("active")
    String isActive;
    String theme;

    public String getId() {
        return id;
    }

    public String getTheme() {
        return theme;
    }

    @Override
    public String toString() {
        return theme;
    }
    // {"version":1.3,"success":true,"status":200,
    // "conversations":[{"id":"4","active":"1","theme":"test1"},{"id":"8","active":"1","theme":"WEB
    //3
}
