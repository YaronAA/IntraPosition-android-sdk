package com.intraposition.buzcartsdk.Models;

import java.util.List;

public class Order {

    private List<Product> products;

    private int orderId;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
