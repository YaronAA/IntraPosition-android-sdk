package com.intraposition.buzcartsdk;

import android.content.Context;

import com.intraposition.buzcartsdk.models.Product;
import com.intraposition.buzcartsdk.models.Triggers;
import com.intraposition.buzcartsdk.network.BaseResponse;
import com.intraposition.buzcartsdk.network.BuzCartCallback;
import com.intraposition.buzcartsdk.network.OrderUpdateResponse;
import com.intraposition.buzcartsdk.network.PdtProductUpdateResponse;
import com.intraposition.buzcartsdk.network.PdtTagStatusResponse;
import com.intraposition.buzcartsdk.network.PdtUserRegisterResponse;
import com.intraposition.buzcartsdk.network.UserRegisterResponse;

import java.util.List;


public class BuzCartSdk {

    private BuzCartClient buzCartClient;

    private static final BuzCartSdk ourInstance = new BuzCartSdk();

    public static BuzCartSdk getInstance() {
        return ourInstance;
    }

    private BuzCartSdk() {

    }

//    public void init(Context context) {
//
//        if (buzCartClient == null){
//            buzCartClient = new BuzCartClient(context);
//        }
//    }

    public void init(Context context, String baseUrl) {

        if (buzCartClient == null){
            buzCartClient = new BuzCartClient(context,baseUrl);
        }
    }

    public void register(String tagId,
                         final String userName,
                         String password,
                         String apiId, final BuzCartCallback<UserRegisterResponse> cb) {
        buzCartClient.register(tagId, userName, password, apiId, cb);
    }

    public void register(String tagId,
                         final String userName,
                         String password,
                         final BuzCartCallback<UserRegisterResponse> cb) {
        buzCartClient.register(tagId, userName, password, cb);
    }

    public void unregister(final BuzCartCallback<BaseResponse> cb) {
        buzCartClient.unregister(cb);
    }

    public void updateTriggers(Triggers triggers, final BuzCartCallback<BaseResponse> cb) {
        buzCartClient.updateTriggers(triggers, cb);
    }

    public void triggerAck(String triggerId, final BuzCartCallback<BaseResponse> cb) {
        buzCartClient.triggerAck(triggerId, cb);
    }

    public void productUpdate(String upc, String status,
                              final BuzCartCallback<OrderUpdateResponse> cb) {

        buzCartClient.productUpdate(upc, status, cb);
    }

    public void orderUpdate(List<Product> products, int orderId,
                            final BuzCartCallback<OrderUpdateResponse> cb) {

        buzCartClient.orderUpdate(products, orderId, cb);
    }

    public void orderClose(final BuzCartCallback<BaseResponse> cb){
        buzCartClient.orderClose(cb);
    }

    public void orderRefresh(final BuzCartCallback<OrderUpdateResponse> cb){
        buzCartClient.orderRefresh(cb);
    }

    // --------------------- PDT API ----------------------------------------------------------

    public void registerPdt(String tagId, String userName, String password ,
                            final BuzCartCallback<PdtUserRegisterResponse> cb){
        buzCartClient.pdtRegister(tagId, userName, password, cb);
    }

    public void pdtUpdateProduct(String productId, String operationType, String freeText,
                                 final BuzCartCallback<PdtProductUpdateResponse> cb){
        buzCartClient.pdtProductUpdate(productId, operationType,null, freeText, cb);
    }

    public void pdtUpdateProduct(String productId, String operationType, String policy, String freeText,
                                 final BuzCartCallback<PdtProductUpdateResponse> cb){
        buzCartClient.pdtProductUpdate(productId, operationType, policy, freeText, cb);
    }

    public void pdtGetTagStatus(final BuzCartCallback<PdtTagStatusResponse> cb){
        buzCartClient.pdtGetTagStatus(cb);
    }
}
