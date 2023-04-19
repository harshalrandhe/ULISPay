package com.ulisfintech.telrpay.ui.order;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.ulisfintech.telrpay.helper.BaseResponse;

public class OrderResponseBean extends BaseResponse implements Parcelable {

    @Expose
    private String token;
    @Expose
    private String order_id;
    @Expose
    private String amount;
    @Expose
    private String payment_link;
    @Expose
    private String iframe_link;

    public OrderResponseBean() {

    }

    protected OrderResponseBean(Parcel in) {
        token = in.readString();
        order_id = in.readString();
        amount = in.readString();
        payment_link = in.readString();
        iframe_link = in.readString();
    }

    public static final Creator<OrderResponseBean> CREATOR = new Creator<OrderResponseBean>() {
        @Override
        public OrderResponseBean createFromParcel(Parcel in) {
            return new OrderResponseBean(in);
        }

        @Override
        public OrderResponseBean[] newArray(int size) {
            return new OrderResponseBean[size];
        }
    };

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

    public String getIframe_link() {
        return iframe_link;
    }

    public void setIframe_link(String iframe_link) {
        this.iframe_link = iframe_link;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(token);
        dest.writeString(order_id);
        dest.writeString(amount);
        dest.writeString(payment_link);
        dest.writeString(iframe_link);
    }
}
