package com.ulisfintech.telrpay.ui;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class PaymentPayload {

    @Expose
    @SerializedName("data")
    private Object data;

    public PaymentPayload(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
