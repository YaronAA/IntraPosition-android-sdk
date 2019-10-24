package com.intraposition.buzcartsdk.network;

public class RegisterResponse extends UserRegisterResponse{

    private String token;

    private Integer orientation_buffer;

    private Integer orientation_sampling_interval;

    private Float angle_correction;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getOrientationBufferLength() {
        return orientation_buffer;
    }

    public void setOrientationBufferLength(Integer orientationBufferLength) {
        this.orientation_buffer = orientationBufferLength;
    }

    public Integer getOrientation_sampling_interval() {
        return orientation_sampling_interval;
    }

    public void setOrientation_sampling_interval(Integer orientation_sampling_interval) {
        this.orientation_sampling_interval = orientation_sampling_interval;
    }

    public Float getAngle_correction() {
        return angle_correction;
    }

    public void setAngle_correction(Float angle_correction) {
        this.angle_correction = angle_correction;
    }
}
