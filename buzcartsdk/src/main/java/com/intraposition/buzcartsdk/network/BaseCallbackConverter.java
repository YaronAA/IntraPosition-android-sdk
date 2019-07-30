package com.intraposition.buzcartsdk.network;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseCallbackConverter implements Callback<BaseResponse> {

    BuzCartCallback<BaseResponse> cb;

    public BaseCallbackConverter(BuzCartCallback<BaseResponse> cb){
        this.cb = cb;
    }

    @Override
    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
        if (response.isSuccessful()) {
            cb.onResponse(response.body());
        }
        else{
            if (response.body() == null) {
                cb.onFailure(response.message());
            } else {
                cb.onFailure(response.body().getMessage());
            }
        }
    }

    @Override
    public void onFailure(Call<BaseResponse> call, Throwable t) {
        cb.onFailure(t.getMessage());
    }
}
