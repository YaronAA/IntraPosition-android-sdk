package com.intraposition.buzcartsdk.network;

import com.intraposition.buzcartsdk.models.SortedProducts;

public class OrderUpdateResponse extends BaseResponse {

    private SortedProducts sortedProducts;

    public SortedProducts getSortedProducts() {
        return sortedProducts;
    }

    public void setSortedProducts(SortedProducts sortedProducts) {
        this.sortedProducts = sortedProducts;
    }
}
