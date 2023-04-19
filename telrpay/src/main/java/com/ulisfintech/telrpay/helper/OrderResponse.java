package com.ulisfintech.telrpay.helper;



import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.ulisfintech.telrpay.ui.order.OrderResponseBean;

public class OrderResponse extends BaseResponse implements Parcelable {

    @Expose
    private OrderResponseBean data;

    private int paymentType;
    private String next_step;
    private ProductBean productBean;

    public OrderResponse() {

    }

    protected OrderResponse(Parcel in) {
        data = in.readParcelable(OrderResponseBean.class.getClassLoader());
        paymentType = in.readInt();
        next_step = in.readString();
        productBean = in.readParcelable(ProductBean.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(data, flags);
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

    public OrderResponseBean getData() {
        return data;
    }

    public void setData(OrderResponseBean data) {
        this.data = data;
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
