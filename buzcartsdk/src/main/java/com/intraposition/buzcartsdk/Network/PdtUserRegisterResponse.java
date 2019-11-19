package com.intraposition.buzcartsdk.Network;

public class PdtUserRegisterResponse extends BaseResponse {

    private String tagStatus;

    private String storeId;

    public String getTagStatus() {
        return tagStatus;
    }

    public void setTagStatus(String tagStatus) {
        this.tagStatus = tagStatus;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

}
