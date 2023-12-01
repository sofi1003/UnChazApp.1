package com.example.unchazapp.acces;

public interface KeyCallback {
    void onKeyFound(Object key);
    void onError(String errorMessage);
}