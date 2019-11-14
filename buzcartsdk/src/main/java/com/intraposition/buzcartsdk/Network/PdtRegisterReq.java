package com.intraposition.buzcartsdk.Network;

public class PdtRegisterReq {

    PdtRegisterReq(){

    }

    public PdtRegisterReq(String tagId, String username, String password){
        this.tagId = tagId;
        this.username = username;
        this.password = password;
    }

    private String tagId;

    private String username;

    private String password;

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
