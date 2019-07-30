package com.intraposition.buzcartsdk;

import android.util.Log;
import android.webkit.JavascriptInterface;

import com.google.gson.Gson;
import com.intraposition.buzcartsdk.models.BannerNotification;

import static android.content.ContentValues.TAG;


public class BuzCartJavaScriptConnector {

    BuzCartEventListener listener;

    public BuzCartJavaScriptConnector(BuzCartEventListener listener){
        this.listener = listener;
    }

    @JavascriptInterface
    public void notify(String strJson) {

        Log.e(TAG, "received: " + strJson);

        Gson gson = new Gson();

        BannerNotification bannerNotification = gson.fromJson(strJson,BannerNotification.class);

        if (listener != null) {
            listener.onBannerEvent(bannerNotification.getId(), bannerNotification.getType());
        }
    }

}
