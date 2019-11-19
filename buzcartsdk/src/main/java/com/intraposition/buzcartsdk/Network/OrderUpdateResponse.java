package com.intraposition.buzcartsdk.Network;

import com.intraposition.buzcartsdk.Models.SortedProducts;

public class OrderUpdateResponse extends BaseResponse {

    private SortedProducts sortedProducts;

    public SortedProducts getSortedProducts() {
        return sortedProducts;
    }

    public void setSortedProducts(SortedProducts sortedProducts) {
        this.sortedProducts = sortedProducts;
    }
}
