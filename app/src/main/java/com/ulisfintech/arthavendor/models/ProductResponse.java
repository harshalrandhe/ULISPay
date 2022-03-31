package com.ulisfintech.arthavendor.models;

import com.ulisfintech.arthavendor.ui.products.ProductBean;

import java.util.List;

public class ProductResponse {

    private List<ProductBean> products;

    public List<ProductBean> getProducts() {
        return products;
    }

    public void setProducts(List<ProductBean> products) {
        this.products = products;
    }
}
