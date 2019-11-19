package com.intraposition.buzcartsdk.Models;

public class Product {

    private String upc;

    private int  priority;

    private String status;

    private double [] orientation;

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double [] getOrientation() {
        return orientation;
    }

    public void setOrientation(double [] orientation) {
        this.orientation = orientation;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

}
