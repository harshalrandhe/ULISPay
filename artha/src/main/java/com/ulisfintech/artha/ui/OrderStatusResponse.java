package com.ulisfintech.artha.ui;

import com.google.gson.annotations.Expose;

class OrderStatusResponse {

    @Expose
    private OrderStatusBean data;
    @Expose
    private String status;

    public OrderStatusBean getData() {
        return data;
    }

    public void setData(OrderStatusBean data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
