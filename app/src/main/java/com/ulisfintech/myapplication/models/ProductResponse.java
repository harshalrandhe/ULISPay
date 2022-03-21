package com.ulisfintech.myapplication.models;

import com.ulisfintech.myapplication.ui.products.ProductBean;

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
