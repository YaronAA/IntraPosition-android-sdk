package com.intraposition.buzcartsdk.network;

public interface BuzCartCallback<T> {

    void onResponse(T response);

    void onFailure(String response);

}
