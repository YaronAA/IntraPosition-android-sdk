package com.intraposition.buzcartsdk.BuzCart;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intraposition.buzcartsdk.Compass.Compass;
import com.intraposition.buzcartsdk.Compass.CompassEventListener;
import com.intraposition.buzcartsdk.Utils.ErrorMessagesUtils;
import com.intraposition.buzcartsdk.Utils.GlobalKeys;
import com.intraposition.buzcartsdk.Utils.ProductOperationType;
import com.intraposition.buzcartsdk.Utils.ProductStatus;
import com.intraposition.buzcartsdk.Utils.WebServicesURLs;
import com.intraposition.buzcartsdk.Models.Order;
import com.intraposition.buzcartsdk.Models.Orders;
import com.intraposition.buzcartsdk.Models.PdtProduct;
import com.intraposition.buzcartsdk.Models.Product;
import com.intraposition.buzcartsdk.Network.BaseResponse;
import com.intraposition.buzcartsdk.Network.BuzCartCallback;
import com.intraposition.buzcartsdk.Network.OrderUpdateResponse;
import com.intraposition.buzcartsdk.Network.PdtProductUpdateResponse;
import com.intraposition.buzcartsdk.Network.PdtRegisterReq;
import com.intraposition.buzcartsdk.Network.PdtRegisterResponse;
import com.intraposition.buzcartsdk.Network.PdtTagStatusResponse;
import com.intraposition.buzcartsdk.Network.PdtUserRegisterResponse;
import com.intraposition.buzcartsdk.Network.RegisterReq;
import com.intraposition.buzcartsdk.Network.RegisterResponse;
import com.intraposition.buzcartsdk.Network.UserRegisterResponse;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class BuzCartSdk {

    private static BuzCartSdk ourInstance = null;
    private String token = "";
    private String pdtToken = "";
    private BuzCartApi buzcartApi;
    private Compass compass;
    private CompassEventListener listener;

    public static BuzCartSdk getInstance()
    {
        if (ourInstance == null) {
            ourInstance = new BuzCartSdk("");
    }
        return ourInstance;
    }

    public static BuzCartSdk getInstance(String baseUrl)
    {
        if (ourInstance == null) {
            ourInstance = new BuzCartSdk(baseUrl);
        }
        return ourInstance;
    }

    private BuzCartSdk(String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl) //This is the only mandatory call on Builder object.
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().serializeSpecialFloatingPointValues().create()))
                .build();
        buzcartApi = retrofit.create(BuzCartApi.class);

    }


    public void setCompassEventListener(CompassEventListener listener){
        this.listener = listener;
    }

    public void register(final Context context , String tagId,
                         final String userName,
                         String password,
                         String appId, final BuzCartCallback<UserRegisterResponse> cb) {

        final RegisterReq registerReq = new RegisterReq(tagId,userName, password, appId);

        Call<JsonObject> call = buzcartApi.makeRequest(WebServicesURLs.Register_URL,"",registerReq);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Gson gson = new GsonBuilder().create();
                    RegisterResponse registerResponse = gson.fromJson(response.body(), RegisterResponse.class);

                    token = registerResponse.getToken();

                    if (registerResponse.getSuccess()) {
                        registerResponse.setUrl(registerResponse.getUrl());
                        if (compass == null) {
                            Float corrAngle = registerResponse.getAngle_correction();
                            compass = new Compass(context,
                                    registerResponse.getOrientationBufferLength(),
                                    registerResponse.getOrientation_sampling_interval(),
                                    corrAngle);
                            compass.setOnCompassEventListener(listener);
                            compass.start();
                        }
                    }
                    cb.onResponse(registerResponse);
                } else {
                    cb.onFailure(ErrorMessagesUtils.getErrorMessage(response));
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                cb.onFailure(t.getMessage());
            }
        });
    }

    public void register(Context context ,String tagId,
                         String userName,
                         String password,
                         final BuzCartCallback<UserRegisterResponse> cb) {
        register(context, tagId, userName, password, GlobalKeys.DEFAULT_APP_ID, cb);
    }

    public void unregister(final BuzCartCallback<BaseResponse> cb) {

        Call<JsonObject> call = buzcartApi.makeRequest(WebServicesURLs.Unregister_URL,token, new Object());

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if(response.isSuccessful()) {
                    Gson gson = new GsonBuilder().create();
                    BaseResponse baseResponse = gson.fromJson(response.body(), BaseResponse.class);
                    cb.onResponse(baseResponse);
                }else{
                    cb.onFailure(ErrorMessagesUtils.getErrorMessage(response));
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                cb.onFailure(ErrorMessagesUtils.getErrorMessage(t));
            }
        });

    }

    public void updateTriggers(JsonObject triggers, final BuzCartCallback<BaseResponse> cb) {

        Call<JsonObject> call = buzcartApi.makeRequest(WebServicesURLs.Update_Trigger_URL,token, triggers);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if(response.isSuccessful()) {
                    Gson gson = new GsonBuilder().create();
                    BaseResponse baseResponse = gson.fromJson(response.body(), BaseResponse.class);
                    cb.onResponse(baseResponse);
                }else{
                    cb.onFailure(ErrorMessagesUtils.getErrorMessage(response));
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                cb.onFailure(ErrorMessagesUtils.getErrorMessage(t));
            }
        });

    }

    public void triggerAck(String triggerId, final BuzCartCallback<BaseResponse> cb) {

        JsonObject trigger = new JsonObject();

        trigger.addProperty("triggerId",triggerId);

        Call<JsonObject> call = buzcartApi.makeRequest(WebServicesURLs.ACK_Triggers_URL,token, trigger);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Gson gson = new GsonBuilder().create();
                    BaseResponse baseResponse = gson.fromJson(response.body(), BaseResponse.class);
                    cb.onResponse(baseResponse);
                }else{
                    cb.onFailure(ErrorMessagesUtils.getErrorMessage(response));
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                cb.onFailure(ErrorMessagesUtils.getErrorMessage(t));
            }
        });


    }

    public void orderUpdate(List<Product> products,
                            int orderId,
                            final BuzCartCallback<OrderUpdateResponse> cb) {

        Order order = new Order();
        order.setOrderId(orderId);
        order.setProducts(products);
        List<Order> orderList = new ArrayList<>();
        orderList.add(order);
        Orders orders = new Orders();
        orders.setOrders(orderList);

        Call<JsonObject> call = buzcartApi.makeRequest(WebServicesURLs.Update_Order_URL,token, orders);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Gson gson = new GsonBuilder().create();
                    OrderUpdateResponse orderUpdateResponse = gson.fromJson(response.body(), OrderUpdateResponse.class);
                    cb.onResponse(orderUpdateResponse);
                }else{
                    cb.onFailure(ErrorMessagesUtils.getErrorMessage(response));
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                cb.onFailure(ErrorMessagesUtils.getErrorMessage(t));
            }
        });

    }

    public void productUpdate(String upc, String status,
                              final BuzCartCallback<OrderUpdateResponse> cb) {

        Product product = new Product();

        product.setUpc(upc);
        product.setStatus(status);
        if (status.toLowerCase().equals(ProductStatus.taken.name())) {
            product.setOrientation(compass.getOrientationBuffer());
        }

        Call<JsonObject> call = buzcartApi.makeRequest(WebServicesURLs.Update_Product_URL, token, product);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Gson gson = new GsonBuilder().create();
                    OrderUpdateResponse orderUpdateResponse = gson.fromJson(response.body(), OrderUpdateResponse.class);
                    cb.onResponse(orderUpdateResponse);
                }else{
                    cb.onFailure(ErrorMessagesUtils.getErrorMessage(response));
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                cb.onFailure(ErrorMessagesUtils.getErrorMessage(t));
            }
        });

    }

    public void orderClose(final BuzCartCallback<BaseResponse> cb) {

        Call<JsonObject> call = buzcartApi.makeRequest(WebServicesURLs.Close_Order_URL, token, new Object());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Gson gson = new GsonBuilder().create();
                    OrderUpdateResponse orderUpdateResponse = gson.fromJson(response.body(), OrderUpdateResponse.class);
                    cb.onResponse(orderUpdateResponse);
                }else{
                    cb.onFailure(ErrorMessagesUtils.getErrorMessage(response));
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                cb.onFailure(ErrorMessagesUtils.getErrorMessage(t));
            }
        });



    }

    public void orderRefresh(final BuzCartCallback<OrderUpdateResponse> cb) {

        Call<JsonObject> call = buzcartApi.makeRequest(WebServicesURLs.Refresh_Order_URL, token,new Object());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Gson gson = new GsonBuilder().create();
                    OrderUpdateResponse orderUpdateResponse = gson.fromJson(response.body(), OrderUpdateResponse.class);
                    cb.onResponse(orderUpdateResponse);
                }
                else{
                    cb.onFailure(ErrorMessagesUtils.getErrorMessage(response));
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                cb.onFailure(ErrorMessagesUtils.getErrorMessage(t));
            }
        });

    }

    // -------------------  Pdt API ------------------------------------------------

    public void registerPdt(final Context context , String tagId, String userName, String password,
                            final BuzCartCallback<PdtUserRegisterResponse> cb){

        PdtRegisterReq registerReq = new PdtRegisterReq(tagId,userName,password);

        Call<JsonObject> call = buzcartApi.makeRequest(WebServicesURLs.PDT_Register_URL,"",registerReq);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                PdtUserRegisterResponse userResponse = new PdtUserRegisterResponse();
                if (response.isSuccessful()) {

                    Gson gson = new GsonBuilder().create();
                    PdtRegisterResponse pdtRegisterResponse = gson.fromJson(response.body(), PdtRegisterResponse.class);

                    assert response.body() != null;
                    pdtToken = pdtRegisterResponse.getToken();
                    userResponse.setSuccess(pdtRegisterResponse.getSuccess());
                    userResponse.setTagStatus(pdtRegisterResponse.getTagStatus());



                    if (pdtRegisterResponse.getSuccess()) {
                        userResponse.setStoreId(pdtRegisterResponse.getStoreId());
                        if (compass == null) {
                            compass = new Compass(context,
                                    pdtRegisterResponse.getOrientation_buffer(),
                                    pdtRegisterResponse.getOrientation_sampling_interval(),
                                    null);
                            compass.setOnCompassEventListener(listener);
                            compass.start();
                        }
                    }
                    cb.onResponse(userResponse);
                } else {
                    cb.onFailure(ErrorMessagesUtils.getErrorMessage(response));
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                cb.onFailure(ErrorMessagesUtils.getErrorMessage(t));
            }
        });

    }


    public void pdtUpdateProduct(String productId, String operationType, String policy, String freeText,
                                 final BuzCartCallback<PdtProductUpdateResponse> cb){

        PdtProduct pdtProduct = new PdtProduct();
        pdtProduct.setProductId(productId);
        pdtProduct.setOperationType(operationType);
        if (policy!=null){
            pdtProduct.setPolicy(policy);
        }
        pdtProduct.setFreeText(freeText);
        if (operationType.toLowerCase().equals(ProductOperationType.add.name())){
            pdtProduct.setOrientation(compass.getOrientationBuffer());
        }

        Call<JsonObject> call = buzcartApi.makeRequest(WebServicesURLs.PDT_Update_Product_URL, pdtToken, pdtProduct);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if(response.isSuccessful()) {
                    Gson gson = new GsonBuilder().create();
                    PdtProductUpdateResponse pdtProductUpdateResponse = gson.fromJson(response.body(), PdtProductUpdateResponse.class);
                    cb.onResponse(pdtProductUpdateResponse);
                }else{
                    cb.onFailure(ErrorMessagesUtils.getErrorMessage(response));
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                cb.onFailure(ErrorMessagesUtils.getErrorMessage(t));
            }
        });


    }

    public void pdtGetTagStatus(final BuzCartCallback<PdtTagStatusResponse> cb){

        Call<JsonObject> call = buzcartApi.makeRequest(WebServicesURLs.PDT_Tag_Status_URL, pdtToken,new Object());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if(response.isSuccessful()) {
                    Gson gson = new GsonBuilder().create();
                    PdtTagStatusResponse pdtTagStatusResponse = gson.fromJson(response.body(), PdtTagStatusResponse.class);
                    cb.onResponse(pdtTagStatusResponse);
                }else{
                    cb.onFailure(ErrorMessagesUtils.getErrorMessage(response));
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                cb.onFailure(ErrorMessagesUtils.getErrorMessage(t));
            }
        });

    }



}
