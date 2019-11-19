package com.intraposition.buzcartsdk.Utils;

public interface WebServicesURLs {

    String Register_URL = "api/v2/picking/register";
    String Unregister_URL = "api/v2/picking/unregister";
    String Update_Trigger_URL = "api/v2/picking/triggers/update";
    String ACK_Triggers_URL = "api/v2/picking/triggers/ack";
    String Close_Order_URL = "api/v2/picking/order/close";
    String Refresh_Order_URL = "api/v2/picking/order/refresh";
    String Update_Order_URL = "api/v2/picking/order/update";
    String Update_Product_URL = "api/v2/picking/order/productUpdate";

    // -------------------  Pdt API ------------------------------------------------
    String PDT_Register_URL = "api/v1/directory/register";
    String PDT_Update_Product_URL = "api/v1/directory/productUpdate";
    String PDT_Tag_Status_URL = "api/v1/directory/status";
}
