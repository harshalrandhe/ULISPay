package com.ulisfintech.telrpay.helper;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

public class OrderResponse extends BaseResponse implements Parcelable {

    @Expose
    private String order_id;
    @Expose
    private String token;
    @Expose
    private String amount;
    @Expose
    private String payment_link;

    private int paymentType;
    private String next_step;
    private ProductBean productBean;

    public OrderResponse() {

    }

    protected OrderResponse(Parcel in) {
        order_id = in.readString();
        token = in.readString();
        amount = in.readString();
        payment_link = in.readString();
        paymentType = in.readInt();
        next_step = in.readString();
        productBean = in.readParcelable(ProductBean.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(order_id);
        dest.writeString(token);
        dest.writeString(amount);
        dest.writeString(payment_link);
        dest.writeInt(paymentType);
        dest.writeString(next_step);
        dest.writeParcelable(productBean, flags);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public String getNext_step() {
        return next_step;
    }

    public void setNext_step(String next_step) {
        this.next_step = next_step;
    }

    public ProductBean getProductBean() {
        return productBean;
    }

    public void setProductBean(ProductBean productBean) {
        this.productBean = productBean;
    }
}
