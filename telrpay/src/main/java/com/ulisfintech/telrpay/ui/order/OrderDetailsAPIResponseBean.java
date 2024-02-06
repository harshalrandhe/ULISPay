package com.ulisfintech.telrpay.ui.order;

import com.google.gson.annotations.Expose;

public class OrderDetailsAPIResponseBean {

    @Expose
    private String order_id;
    @Expose
    private String status;
    @Expose
    private String env;

    public OrderDetailsAPIResponseBean() {
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }
}
