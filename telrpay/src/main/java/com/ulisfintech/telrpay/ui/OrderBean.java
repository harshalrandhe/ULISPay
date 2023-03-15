package com.ulisfintech.telrpay.ui;

import com.google.gson.annotations.Expose;
import com.ulisfintech.telrpay.ui.order.BillingDetails;
import com.ulisfintech.telrpay.ui.order.CustomerDetails;
import com.ulisfintech.telrpay.ui.order.OrderDetails;
import com.ulisfintech.telrpay.ui.order.ShippingDetails;

class OrderBean {

    @Expose
    private CustomerDetails customer_details;
    @Expose
    private BillingDetails billing_details;
    @Expose
    private ShippingDetails shipping_details;
    @Expose
    private OrderDetails order_details;

    private HeaderBean headerBean;

    public CustomerDetails getCustomer_details() {
        return customer_details;
    }

    public void setCustomer_details(CustomerDetails customer_details) {
        this.customer_details = customer_details;
    }

    public BillingDetails getBilling_details() {
        return billing_details;
    }

    public void setBilling_details(BillingDetails billing_details) {
        this.billing_details = billing_details;
    }

    public ShippingDetails getShipping_details() {
        return shipping_details;
    }

    public void setShipping_details(ShippingDetails shipping_details) {
        this.shipping_details = shipping_details;
    }

    public OrderDetails getOrder_details() {
        return order_details;
    }

    public void setOrder_details(OrderDetails order_details) {
        this.order_details = order_details;
    }

    public HeaderBean getHeaders() {
        return headerBean;
    }

    public void setHeaders(HeaderBean headerBean) {
        this.headerBean = headerBean;
    }
}
