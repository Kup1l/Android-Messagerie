package com.example.le4tp2;

import com.google.gson.annotations.SerializedName;

public class Message {
    String id;
    String contenu;
    String auteur;
    String couleur;

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", contenu='" + contenu + '\'' +
                ", auteur='" + auteur + '\'' +
                ", couleur='" + couleur + '\'' +
                '}';
    }
    //"id":"156","contenu":"teeeeest","auteur":"tom","couleur":"blue"
}
