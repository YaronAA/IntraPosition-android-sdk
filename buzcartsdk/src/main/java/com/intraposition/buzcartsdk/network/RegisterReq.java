package com.intraposition.buzcartsdk.network;

public class RegisterReq extends PdtRegisterReq {

//    private String tagId;
//
//    private String username;
//
//    private String password;

    private String appId;

//    public String getTagId() {
//        return tagId;
//    }
//
//    public void setTagId(String tagId) {
//        this.tagId = tagId;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }

    public String getAppId() {
        return appId;
    }

    public void setApiId(String appId) {
        this.appId = appId;
    }
}
