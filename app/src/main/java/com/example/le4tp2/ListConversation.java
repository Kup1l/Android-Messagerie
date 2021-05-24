package com.example.le4tp2;

import java.util.ArrayList;

public class ListConversation {
    ArrayList<Conversation> conversations;

    public ArrayList<Conversation> getConversations(){
        return conversations;
    }

    @Override
    public String toString() {
        return "ListConversation{" +
                "conversations=" + conversations +
                '}';
    }
}
