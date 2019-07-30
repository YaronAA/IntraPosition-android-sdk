package com.intraposition.buzcartsdk.network;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderUpdateCallbackConverter implements Callback<OrderUpdateResponse> {

    private BuzCartCallback<OrderUpdateResponse> cb;

    public OrderUpdateCallbackConverter(BuzCartCallback<OrderUpdateResponse> cb){
        this.cb = cb;
    }

    @Override
    public void onResponse(Call<OrderUpdateResponse> call, Response<OrderUpdateResponse> response) {
        if (response.isSuccessful()) {
            cb.onResponse(response.body());
        } else {
            if (response.body() == null) {
                cb.onFailure(response.message());
            } else {
                cb.onFailure(response.body().getMessage());
            }
        }
    }

    @Override
    public void onFailure(Call<OrderUpdateResponse> call, Throwable t) {
        if (t.getMessage() != null) {
            cb.onFailure(t.getMessage());
        }
    }
}
