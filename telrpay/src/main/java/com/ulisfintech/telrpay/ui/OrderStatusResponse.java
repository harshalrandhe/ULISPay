package com.ulisfintech.telrpay.ui;

import com.google.gson.annotations.Expose;

class OrderStatusResponse {

    @Expose
    private OrderStatusBean data;
    @Expose
    private String status;
    @Expose
    private String message;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
