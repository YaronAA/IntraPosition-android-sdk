package com.intraposition.buzcartsdk;

import android.content.Context;
import android.util.Log;

import com.intraposition.buzcartsdk.models.Order;
import com.intraposition.buzcartsdk.models.Orders;
import com.intraposition.buzcartsdk.models.PdtProduct;
import com.intraposition.buzcartsdk.models.Product;
import com.intraposition.buzcartsdk.models.Trigger;
import com.intraposition.buzcartsdk.models.Triggers;
import com.intraposition.buzcartsdk.network.BaseCallbackConverter;
import com.intraposition.buzcartsdk.network.BaseResponse;
import com.intraposition.buzcartsdk.network.BuzCartCallback;
import com.intraposition.buzcartsdk.network.OrderUpdateCallbackConverter;
import com.intraposition.buzcartsdk.network.OrderUpdateResponse;
import com.intraposition.buzcartsdk.network.PdtProductUpdateResponse;
import com.intraposition.buzcartsdk.network.PdtRegisterReq;
import com.intraposition.buzcartsdk.network.PdtRegisterResponse;
import com.intraposition.buzcartsdk.network.PdtTagStatusResponse;
import com.intraposition.buzcartsdk.network.PdtUserRegisterResponse;
import com.intraposition.buzcartsdk.network.RegisterReq;
import com.intraposition.buzcartsdk.network.RegisterResponse;
import com.intraposition.buzcartsdk.network.UserRegisterResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class BuzCartClient {

    private static final String TAG = "BuzCartClient";

    private static final String NOTOKEN = "you must register before using this function";

    private static final String BASE_URL = "";

    private String token = "";

    private String pdtToken = "";

    private final BuzcartApi buzcartApi;

    private Compass compass;

    private Context context;

    public BuzCartClient(Context context) {
        this.context = context;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL) //This is the only mandatory call on Builder object.
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        buzcartApi = retrofit.create(BuzcartApi.class);
    }

    public BuzCartClient(Context context,String baseUrl) {
        this.context = context;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl) //This is the only mandatory call on Builder object.
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        buzcartApi = retrofit.create(BuzcartApi.class);
    }

    public void register(String tagId,
                         final String userName,
                         String password,
                         String apiId, final BuzCartCallback<UserRegisterResponse> cb) {

        RegisterReq registerReq = new RegisterReq();

        registerReq.setTagId(tagId);

        registerReq.setUsername(userName);

        registerReq.setPassword(password);

        registerReq.setApiId(apiId);

        Call<RegisterResponse> call = buzcartApi.register(registerReq);

        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call,
                                   Response<RegisterResponse> response) {
                UserRegisterResponse userResponse = new UserRegisterResponse();
                if (response.isSuccessful()) {
                    token = response.body().getToken();
                    userResponse.setSuccess(response.body().getSuccess());
                    userResponse.setMessage(response.body().getMessage());
                    if (response.body().getSuccess()) {
                        userResponse.setUrl(response.body().getUrl());
                        if (compass == null) {
                            compass = new Compass(context,
                                    response.body().getOrientationBufferLength(),
                                    response.body().getOrientation_sampling_interval());
                            compass.start();
                        }
                    }
                    cb.onResponse(userResponse);
                } else {
                    if (response.body() == null) {
                        cb.onFailure(response.message());
                    } else {
                        cb.onFailure(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                cb.onFailure(t.getMessage());
            }
        });
    }

    public void register(String tagId,
                         String userName,
                         String password,
                         final BuzCartCallback<UserRegisterResponse> cb) {
        register(tagId, userName, password, "buzcart-demo", cb);
    }

    public void unregister(final BuzCartCallback<BaseResponse> cb) {

        if (token == null) {
            cb.onFailure(NOTOKEN);
        }

        Call<BaseResponse> call = buzcartApi.unregister(token);

        BaseCallbackConverter baseCallbackConverter = new BaseCallbackConverter(cb);

        call.enqueue(baseCallbackConverter);

    }

    public void updateTriggers(Triggers triggers, final BuzCartCallback<BaseResponse> cb) {

        if (token == null) {
            cb.onFailure(NOTOKEN);
        }

        Call<BaseResponse> call = buzcartApi.updateTrigger(token, triggers);

        BaseCallbackConverter baseCallbackConverter = new BaseCallbackConverter(cb);

        call.enqueue(baseCallbackConverter);


    }

    public void triggerAck(String triggerId, final BuzCartCallback<BaseResponse> cb) {

        if (token == null) {
            cb.onFailure(NOTOKEN);
        }

        Trigger trigger = new Trigger();

        trigger.setTriggerId(triggerId);

        Call<BaseResponse> call = buzcartApi.ackTrigger(token, trigger);

        BaseCallbackConverter baseCallbackConverter = new BaseCallbackConverter(cb);

        call.enqueue(baseCallbackConverter);

    }

    public void orderUpdate(List<Product> products,
                            int orderId,
                            final BuzCartCallback<OrderUpdateResponse> cb) {

        if (token == null) {
            cb.onFailure(NOTOKEN);
        }

        Order order = new Order();
        order.setOrderId(orderId);
        order.setProducts(products);
        List<Order> orderlist = new ArrayList<>();
        orderlist.add(order);
        Orders orders = new Orders();
        orders.setOrders(orderlist);

        Call<OrderUpdateResponse> call = buzcartApi.orderUpdate(token, orders);

        OrderUpdateCallbackConverter orderUpdateCallbackConverter =
                new OrderUpdateCallbackConverter(cb);

        call.enqueue(orderUpdateCallbackConverter);

    }

    public void productUpdate(String upc, String status,
                              final BuzCartCallback<OrderUpdateResponse> cb) {

        if (token == null) {
            cb.onFailure(NOTOKEN);
        }

        Product product = new Product();

        product.setUpc(upc);
        product.setStatus(status);
        if (status.toLowerCase().equals("taken")) {
            product.setOrientation(compass.getOrientationBuffer());
        }

        Call<OrderUpdateResponse> call = buzcartApi.productUpdate(token, product);

        OrderUpdateCallbackConverter orderUpdateCallbackConverter =
                new OrderUpdateCallbackConverter(cb);

        call.enqueue(orderUpdateCallbackConverter);

    }

    public void orderClose(final BuzCartCallback<BaseResponse> cb) {

        if (token == null) {
            cb.onFailure(NOTOKEN);
        }

        Call<BaseResponse> call = buzcartApi.orderClose(token);

        BaseCallbackConverter baseCallbackConverter = new BaseCallbackConverter(cb);

        call.enqueue(baseCallbackConverter);

    }

    public void orderRefresh(final BuzCartCallback<OrderUpdateResponse> cb) {

        if (token == null) {
            cb.onFailure(NOTOKEN);
        }

        Call<OrderUpdateResponse> call = buzcartApi.orderRefresh(token);

        OrderUpdateCallbackConverter orderUpdateCallbackConverter =
                new OrderUpdateCallbackConverter(cb);

        call.enqueue(orderUpdateCallbackConverter);

    }

    // -------------------  Pdt API ------------------------------------------------

    public void pdtRegister(String tagId, String userName, String password ,
                            final BuzCartCallback<PdtUserRegisterResponse> cb){

        PdtRegisterReq registerReq = new PdtRegisterReq();

        registerReq.setTagId(tagId);

        registerReq.setUsername(userName);

        registerReq.setPassword(password);

        Call<PdtRegisterResponse> call = buzcartApi.pdtRegister(registerReq);

        call.enqueue(new Callback<PdtRegisterResponse>() {
            @Override
            public void onResponse(Call<PdtRegisterResponse> call, Response<PdtRegisterResponse> response) {
                PdtUserRegisterResponse userResponse = new PdtUserRegisterResponse();
                if (response.isSuccessful()) {
                    pdtToken = response.body().getToken();
                    userResponse.setSuccess(response.body().getSuccess());
                    userResponse.setTagStatus(response.body().getTagStatus());
                    if (response.body().getSuccess()) {
                        userResponse.setStoreId(response.body().getStoreId());
                        if (compass == null) {
                            compass = new Compass(context,
                                    response.body().getOrientation_buffer(),
                                    response.body().getOrientation_sampling_interval());
                            compass.start();
                        }
                    }
                    cb.onResponse(userResponse);
                } else {
                    if (response.body() == null) {
                        cb.onFailure(response.message());
                    } else {
                        cb.onFailure(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<PdtRegisterResponse> call, Throwable t) {
                cb.onFailure(t.getMessage());
            }
        });

    }


    public void pdtProductUpdate(String productId, String operationType,String policy, String freeText,
                                 final BuzCartCallback<PdtProductUpdateResponse> cb){
        if (pdtToken == null) {
            Log.e(TAG,NOTOKEN);
            return;
        }

        PdtProduct pdtProduct = new PdtProduct();
        pdtProduct.setProductId(productId);
        pdtProduct.setOperationType(operationType);
        if (policy!=null){
            pdtProduct.setPolicy(policy);
        }
        pdtProduct.setFreeText(freeText);
        if (operationType.toLowerCase() == "add"){
            pdtProduct.setOrientation(compass.getOrientationBuffer());
        }
        Call<PdtProductUpdateResponse> call = buzcartApi.pdtProductUpdate(pdtToken, pdtProduct);

        call.enqueue(new Callback<PdtProductUpdateResponse>() {
            @Override
            public void onResponse(Call<PdtProductUpdateResponse> call, Response<PdtProductUpdateResponse> response) {
                if (response.isSuccessful()){
                    cb.onResponse(response.body());
                }
                else{
                    if (response.body() == null) {
                        cb.onFailure(response.message());
                    }
                    else{
                        cb.onFailure(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<PdtProductUpdateResponse> call, Throwable t) {
                cb.onFailure(t.getMessage());
            }
        });
    }

    public void pdtGetTagStatus(final BuzCartCallback<PdtTagStatusResponse> cb){

        if (pdtToken == null) {
            Log.e(TAG,NOTOKEN);
            return;
        }
        Call<PdtTagStatusResponse> call = buzcartApi.getTagStatus(pdtToken);

        call.enqueue(new Callback<PdtTagStatusResponse>() {
            @Override
            public void onResponse(Call<PdtTagStatusResponse> call, Response<PdtTagStatusResponse> response) {
                if (response.isSuccessful()){
                    cb.onResponse(response.body());
                }
                else{
                    if (response.body() == null) {
                        cb.onFailure(response.message());
                    }
                    else{
                        cb.onFailure(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<PdtTagStatusResponse> call, Throwable t) {
                cb.onFailure(t.getMessage());
            }
        });
    }
}
