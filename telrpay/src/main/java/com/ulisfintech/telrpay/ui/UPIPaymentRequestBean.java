package com.ulisfintech.telrpay.ui;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class UPIPaymentRequestBean {

    @Expose
    @SerializedName("token")
    private String token;
    @Expose
    @SerializedName("order_id")
    private String order_id;
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("upi")
    private String upi;
    @Expose
    @SerializedName("pin")
    private String pin;
    @Expose
    @SerializedName("description")
    private String description;
    @Expose
    @SerializedName("type")
    private String type;

    private HeaderBean headers;

    public UPIPaymentRequestBean() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpi() {
        return upi;
    }

    public void setUpi(String upi) {
        this.upi = upi;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HeaderBean getHeaders() {
        return headers;
    }

    public void setHeaders(HeaderBean headers) {
        this.headers = headers;
    }
}
