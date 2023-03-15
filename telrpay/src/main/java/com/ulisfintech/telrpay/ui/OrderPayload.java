package com.ulisfintech.telrpay.ui;

import com.google.gson.annotations.Expose;

class OrderPayload {

    @Expose
    private OrderBean data;

    public OrderPayload() {
    }

    public OrderPayload(OrderBean data) {
        this.data = data;
    }

    public OrderBean getData() {
        return data;
    }

    public void setData(OrderBean data) {
        this.data = data;
    }
}
