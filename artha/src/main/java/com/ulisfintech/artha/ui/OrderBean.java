package com.ulisfintech.artha.ui;

import com.google.gson.annotations.Expose;

public class OrderBean {

    @Expose
    private double amount;
    @Expose
    private String currency;
    @Expose
    private String customer_name;
    @Expose
    private String customer_email;
    @Expose
    private String customer_mobile;
    @Expose
    private String return_url;
    @Expose
    private String resource;

    private String X_KEY;
    private String X_PASSWORD;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public String getCustomer_mobile() {
        return customer_mobile;
    }

    public void setCustomer_mobile(String customer_mobile) {
        this.customer_mobile = customer_mobile;
    }

    public String getReturn_url() {
        return return_url;
    }

    public void setReturn_url(String return_url) {
        this.return_url = return_url;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getX_KEY() {
        return X_KEY;
    }

    public void setX_KEY(String x_KEY) {
        X_KEY = x_KEY;
    }

    public String getX_PASSWORD() {
        return X_PASSWORD;
    }

    public void setX_PASSWORD(String x_PASSWORD) {
        X_PASSWORD = x_PASSWORD;
    }
}
