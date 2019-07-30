package com.intraposition.buzcartsdk.network;

public class PdtProductUpdateResponse extends BaseResponse{

    private String desc;

    private String tagStatus;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTagStatus() {
        return tagStatus;
    }

    public void setTagStatus(String tagStatus) {
        this.tagStatus = tagStatus;
    }

}
