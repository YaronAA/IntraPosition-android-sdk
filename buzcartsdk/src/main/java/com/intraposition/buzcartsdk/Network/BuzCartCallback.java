package com.intraposition.buzcartsdk.Network;

public interface BuzCartCallback<T> {

    void onResponse(T response);

    void onFailure(String response);

}
