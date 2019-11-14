package com.intraposition.buzcartsdk.Network;

public class RegisterReq extends PdtRegisterReq {

    private String appId;

    public RegisterReq(String tagId, String username, String password , String appId){
        super(tagId,username,password);
        this.appId = appId;
    }

    public String getAppId() {
        return appId;
    }
    public void setApiId(String appId) {
        this.appId = appId;
    }
}
