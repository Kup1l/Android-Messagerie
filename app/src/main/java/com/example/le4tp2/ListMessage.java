package com.example.le4tp2;

import java.util.ArrayList;

public class ListMessage {
    ArrayList<Message> messages;

    @Override
    public String toString() {
        return "ListMessage{" +
                "conversations=" + messages +
                '}';
    }
}
