package com.ulisfintech.telrpay.ui;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class OrderIdBean {

    @Expose
    @SerializedName("order_id")
    private String order_id;
    @Expose
    @SerializedName("token")
    private String token;

    public OrderIdBean() {
    }

    public OrderIdBean(String order_id) {
        this.order_id = order_id;
    }

    public OrderIdBean(String order_id, String token) {
        this.order_id = order_id;
        this.token = token;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
