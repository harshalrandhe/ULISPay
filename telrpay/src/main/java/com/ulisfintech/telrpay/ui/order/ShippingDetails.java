package com.ulisfintech.telrpay.ui.order;

import com.google.gson.annotations.Expose;

public class ShippingDetails {

    @Expose
    private String address_line1;
    @Expose
    private String address_line2;
    @Expose
    private String country;
    @Expose
    private String city;
    @Expose
    private String pin;
    @Expose
    private String province;

    public String getAddress_line1() {
        return address_line1;
    }

    public void setAddress_line1(String address_line1) {
        this.address_line1 = address_line1;
    }

    public String getAddress_line2() {
        return address_line2;
    }

    public void setAddress_line2(String address_line2) {
        this.address_line2 = address_line2;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
