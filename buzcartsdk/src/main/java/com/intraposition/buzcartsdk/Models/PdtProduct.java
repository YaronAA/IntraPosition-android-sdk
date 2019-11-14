package com.intraposition.buzcartsdk.Models;

public class PdtProduct {

    private String productId;

    private String operationType;

    private String freeText;

    private String policy;

    private double [] orientation;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getFreeText() {
        return freeText;
    }

    public void setFreeText(String freeText) {
        this.freeText = freeText;
    }

    public double[] getOrientation() {
        return orientation;
    }

    public void setOrientation(double[] orientation) {
        this.orientation = orientation;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }
}
