package com.ulisfintech.telrpay.ui.order;

import com.google.gson.annotations.Expose;

public class CustomerDetails {

    @Expose
    private String name;
    @Expose
    private String email;
    @Expose
    private String mobile;

    public CustomerDetails() {
    }

    public CustomerDetails(String name, String email, String mobile) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
