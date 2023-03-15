package com.ulisfintech.telrpay.ui;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class PaymentRequestBean {

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
    @SerializedName("card_number")
    private String card_number;
    @Expose
    @SerializedName("expiration_date")
    private String expiration_date;
    @Expose
    @SerializedName("cvv")
    private String cvv;
    @Expose
    @SerializedName("description")
    private String description;
    @Expose
    @SerializedName("address1")
    private String address1;
    @Expose
    @SerializedName("address2")
    private String address2;
    @Expose
    @SerializedName("city")
    private String city;
    @Expose
    @SerializedName("state")
    private String state;
    @Expose
    @SerializedName("country")
    private String country;
    @Expose
    @SerializedName("postalcode")
    private String postalcode;
    @Expose
    @SerializedName("type")
    private String type;
    @Expose
    @SerializedName("source")
    private String source;

    private HeaderBean headers;

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

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getExpiration_date() {
        return expiration_date;
    }

    public void setExpiration_date(String expiration_date) {
        this.expiration_date = expiration_date;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
