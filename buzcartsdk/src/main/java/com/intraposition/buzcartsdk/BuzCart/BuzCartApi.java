package com.intraposition.buzcartsdk.BuzCart;

import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

interface BuzCartApi {

    String Authorization = "authorization";
    String Retrofit_Header = "content-type: application/json";

    @Headers(Retrofit_Header)
    @POST()
    Call<JsonObject> makeRequest(@Url String url, @Header(Authorization) String token, @Body Object object);
}
