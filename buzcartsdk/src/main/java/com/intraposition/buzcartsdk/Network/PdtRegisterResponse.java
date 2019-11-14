package com.intraposition.buzcartsdk.Network;

public class PdtRegisterResponse extends PdtUserRegisterResponse {

    private String token;

    private Integer orientation_buffer;

    private Integer orientation_sampling_interval;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getOrientation_buffer() {
        return orientation_buffer;
    }

    public void setOrientation_buffer(Integer orientation_buffer) {
        this.orientation_buffer = orientation_buffer;
    }

    public Integer getOrientation_sampling_interval() {
        return orientation_sampling_interval;
    }

    public void setOrientation_sampling_interval(Integer orientation_sampling_interval) {
        this.orientation_sampling_interval = orientation_sampling_interval;
    }

}
