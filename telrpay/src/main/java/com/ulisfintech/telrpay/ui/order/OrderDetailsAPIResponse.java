package com.ulisfintech.telrpay.ui.order;

import com.google.gson.annotations.Expose;

public class OrderDetailsAPIResponse {

    @Expose
    private OrderDetailsAPIResponseBean order_details;

    public OrderDetailsAPIResponse() {
    }

    public OrderDetailsAPIResponseBean getOrder_details() {
        return order_details;
    }

    public void setOrder_details(OrderDetailsAPIResponseBean order_details) {
        this.order_details = order_details;
    }
}
