package com.ulisfintech.telrpay.ui.order;

import com.google.gson.annotations.Expose;

public class OrderDetails {

    @Expose
    private String order_id;
    @Expose
    private Double amount;
    @Expose
    private String currency;
    @Expose
    private String description;
    @Expose
    private String return_url;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getReturn_url() {
        return return_url;
    }

    public void setReturn_url(String return_url) {
        this.return_url = return_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
