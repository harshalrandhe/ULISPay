package com.ulisfintech.artha.ui;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class PaymentPayload {

    @Expose
    @SerializedName("data")
    private PaymentRequestBean data;

    public PaymentPayload(PaymentRequestBean data) {
        this.data = data;
    }

    public PaymentRequestBean getData() {
        return data;
    }

    public void setData(PaymentRequestBean data) {
        this.data = data;
    }
}
