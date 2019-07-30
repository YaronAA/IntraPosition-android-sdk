package com.intraposition.buzcartsdk.models;

import com.intraposition.buzcartsdk.models.Product;

import java.util.List;

public class SortedProducts {

    private List<Product> products;

    private List<String> unknownLocationProducts;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<String> getUnknownLocationProducts() {
        return unknownLocationProducts;
    }

    public void setUnknownLocationProducts(List<String> unknownLocationProducts) {
        this.unknownLocationProducts = unknownLocationProducts;
    }
}
