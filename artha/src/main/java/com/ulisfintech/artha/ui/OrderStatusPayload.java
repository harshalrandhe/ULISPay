package com.ulisfintech.artha.ui;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class OrderStatusPayload extends GatewayRequest {

    @Expose
    @SerializedName("data")
    private OrderIdBean data;

    public OrderStatusPayload() {
    }

    public OrderStatusPayload(OrderIdBean data) {
        this.data = data;
    }

    public OrderIdBean getData() {
        return data;
    }

    public void setData(OrderIdBean data) {
        this.data = data;
    }
}
