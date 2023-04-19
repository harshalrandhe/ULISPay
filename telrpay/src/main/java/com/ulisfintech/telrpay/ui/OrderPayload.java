package com.ulisfintech.telrpay.ui;

import com.google.gson.annotations.Expose;

import org.json.JSONObject;

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

//    private JSONObject data;
//
//    public OrderPayload() {
//    }
//
//    public OrderPayload(JSONObject data) {
//        this.data = data;
//    }
//
//    public JSONObject getData() {
//        return data;
//    }
//
//    public void setData(JSONObject data) {
//        this.data = data;
//    }
}
