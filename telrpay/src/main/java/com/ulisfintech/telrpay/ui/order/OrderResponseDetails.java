package com.ulisfintech.telrpay.ui.order;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

public class OrderResponseDetails implements Parcelable {

    @Expose
    private String order_id;
    @Expose
    private String status;

    public OrderResponseDetails() {
    }

    protected OrderResponseDetails(Parcel in) {
        order_id = in.readString();
        status = in.readString();
    }

    public static final Creator<OrderResponseDetails> CREATOR = new Creator<OrderResponseDetails>() {
        @Override
        public OrderResponseDetails createFromParcel(Parcel in) {
            return new OrderResponseDetails(in);
        }

        @Override
        public OrderResponseDetails[] newArray(int size) {
            return new OrderResponseDetails[size];
        }
    };

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(order_id);
        dest.writeString(status);
    }
}
