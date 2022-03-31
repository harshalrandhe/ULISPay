package com.ulisfintech.artha.helper;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

public class OrderResponse extends BaseResponse implements Parcelable {

    @Expose
    private String order_id;
    @Expose
    private String token;
    @Expose
    private int paymentType;

    private String amount;
    private String payment_link;
    private String next_step;

    protected OrderResponse(Parcel in) {
        order_id = in.readString();
        token = in.readString();
        paymentType = in.readInt();
        amount = in.readString();
        payment_link = in.readString();
        next_step = in.readString();
    }

    public static final Creator<OrderResponse> CREATOR = new Creator<OrderResponse>() {
        @Override
        public OrderResponse createFromParcel(Parcel in) {
            return new OrderResponse(in);
        }

        @Override
        public OrderResponse[] newArray(int size) {
            return new OrderResponse[size];
        }
    };

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPayment_link() {
        return payment_link;
    }

    public void setPayment_link(String payment_link) {
        this.payment_link = payment_link;
    }

    public String getNext_step() {
        return next_step;
    }

    public void setNext_step(String next_step) {
        this.next_step = next_step;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(order_id);
        parcel.writeString(token);
        parcel.writeInt(paymentType);
        parcel.writeString(amount);
        parcel.writeString(payment_link);
        parcel.writeString(next_step);
    }
}
