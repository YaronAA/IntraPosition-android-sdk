package com.intraposition.buzcartsdk.Utils;

import retrofit2.Response;


public class ErrorMessagesUtils {

    public static String getErrorMessage(Throwable t){
        return t.getLocalizedMessage();
    }

    public static String getErrorMessage(Response response){
        return response.message();
    }
}
