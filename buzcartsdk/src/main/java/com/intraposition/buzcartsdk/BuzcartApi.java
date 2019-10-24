package com.intraposition.buzcartsdk;

import com.google.gson.JsonObject;
import com.intraposition.buzcartsdk.models.Orders;
import com.intraposition.buzcartsdk.models.PdtProduct;
import com.intraposition.buzcartsdk.models.Product;
import com.intraposition.buzcartsdk.models.Trigger;
import com.intraposition.buzcartsdk.network.BaseResponse;
import com.intraposition.buzcartsdk.network.OrderUpdateResponse;
import com.intraposition.buzcartsdk.network.PdtProductUpdateResponse;
import com.intraposition.buzcartsdk.network.PdtRegisterReq;
import com.intraposition.buzcartsdk.network.PdtRegisterResponse;
import com.intraposition.buzcartsdk.network.PdtTagStatusResponse;
import com.intraposition.buzcartsdk.network.RegisterReq;
import com.intraposition.buzcartsdk.network.RegisterResponse;



import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface BuzcartApi {

    // --------------------------------- PIcking API ----------------------------------------------

    @Headers("content-type: application/json")
    @POST("api/v2/picking/register")
    Call<RegisterResponse> register(@Body RegisterReq registerReq);

    @Headers({"content-type: application/json"})
    @POST("api/v2/picking/unregister")
    Call<BaseResponse> unregister(@Header("authorization") String token);

    @Headers({"content-type: application/json"})
    @POST("api/v2/picking/triggers/update")
    Call<BaseResponse> updateTrigger(@Header("authorization") String token,
                                     @Body JsonObject triggers);

    @Headers({"content-type: application/json"})
    @POST("api/v2/picking/triggers/ack")
    Call<BaseResponse> ackTrigger(@Header("authorization") String token, @Body JsonObject trigger);

    @Headers({"content-type: application/json"})
    @POST("api/v2/picking/order/close")
    Call<BaseResponse>  orderClose(@Header("authorization") String token);

    @Headers({"content-type: application/json"})
    @POST("api/v2/picking/order/refresh")
    Call<OrderUpdateResponse> orderRefresh(@Header("authorization") String token);

    @Headers({"content-type: application/json"})
    @POST("api/v2/picking/order/update")
    Call<OrderUpdateResponse> orderUpdate(@Header("authorization") String token,
                                          @Body Orders orders);

    @Headers({"content-type: application/json"})
    @POST("api/v2/picking/order/productUpdate")
    Call<OrderUpdateResponse> productUpdate(@Header("authorization") String token,
                                            @Body Product product);

    // --------------------------------- Pdt API ----------------------------------------------

    @Headers("content-type: application/json")
    @POST("api/v1/directory/register")
    Call<PdtRegisterResponse> pdtRegister(@Body PdtRegisterReq registerReq);

    @Headers({"content-type: application/json"})
    @POST("api/v1/directory/productUpdate")
    Call<PdtProductUpdateResponse> pdtProductUpdate(@Header("authorization") String token,
                                                    @Body PdtProduct product);

    @Headers({"content-type: application/json"})
    @GET("api/v1/directory/status")
    Call<PdtTagStatusResponse> getTagStatus(@Header("authorization") String token);

}
