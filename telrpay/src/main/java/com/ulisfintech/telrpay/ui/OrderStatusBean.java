package com.ulisfintech.telrpay.ui;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.ulisfintech.telrpay.ui.order.MerchantDetails;
import com.ulisfintech.telrpay.ui.order.OrderResponseDetails;

public class OrderStatusBean implements Parcelable {

    @Expose
    private MerchantDetails merchant_details;
    @Expose
    private OrderResponseDetails order_details;
    @Expose
    private String prefer_lang;

    public OrderStatusBean() {

    }


    protected OrderStatusBean(Parcel in) {
        merchant_details = in.readParcelable(MerchantDetails.class.getClassLoader());
        order_details = in.readParcelable(OrderResponseDetails.class.getClassLoader());
        prefer_lang = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(merchant_details, flags);
        dest.writeParcelable(order_details, flags);
        dest.writeString(prefer_lang);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderStatusBean> CREATOR = new Creator<OrderStatusBean>() {
        @Override
        public OrderStatusBean createFromParcel(Parcel in) {
            return new OrderStatusBean(in);
        }

        @Override
        public OrderStatusBean[] newArray(int size) {
            return new OrderStatusBean[size];
        }
    };

    public MerchantDetails getMerchant_details() {
        return merchant_details;
    }

    public void setMerchant_details(MerchantDetails merchant_details) {
        this.merchant_details = merchant_details;
    }

    public OrderResponseDetails getOrder_details() {
        return order_details;
    }

    public void setOrder_details(OrderResponseDetails order_details) {
        this.order_details = order_details;
    }

    public String getPrefer_lang() {
        return prefer_lang;
    }

    public void setPrefer_lang(String prefer_lang) {
        this.prefer_lang = prefer_lang;
    }

}
