package com.example.le4tp2;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {
    public String version;
    public boolean success;
    public int status;
    public String hash;

    @Override
    public String toString() {
        return "Response{" +
                "version='" + version + '\'' +
                ", success='" + success + '\'' +
                ", status='" + status + '\'' +
                ", hash='" + hash + '\'' +
                '}';
    }
}
