package com.ulisfintech.artha.ui;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.ulisfintech.artha.helper.BaseResponse;

public class OrderStatusBean extends BaseResponse implements Parcelable {

    @Expose
    private String batch_no;
    @Expose
    private String order_id;
    @Expose
    private String transaction_id;
    @Expose
    private String amount;
    @Expose
    private String order_status;
    @Expose
    private String customer_name;
    @Expose
    private String customer_email;
    @Expose
    private String customer_mobile;
    @Expose
    private String order_date;
    @Expose
    private String order_time;
    @Expose
    private String ip;
    @Expose
    private String country;
    @Expose
    private String payment_method;

    BaseResponse baseResponse;

    public OrderStatusBean() {
    }

    protected OrderStatusBean(Parcel in) {
        super(in);
        batch_no = in.readString();
        order_id = in.readString();
        transaction_id = in.readString();
        amount = in.readString();
        order_status = in.readString();
        customer_name = in.readString();
        customer_email = in.readString();
        customer_mobile = in.readString();
        order_date = in.readString();
        order_time = in.readString();
        ip = in.readString();
        country = in.readString();
        payment_method = in.readString();
        if (baseResponse != null)
            baseResponse = in.readParcelable(BaseResponse.class.getClassLoader());
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

    public String getBatch_no() {
        return batch_no;
    }

    public void setBatch_no(String batch_no) {
        this.batch_no = batch_no;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public String getCustomer_mobile() {
        return customer_mobile;
    }

    public void setCustomer_mobile(String customer_mobile) {
        this.customer_mobile = customer_mobile;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(batch_no);
        parcel.writeString(order_id);
        parcel.writeString(transaction_id);
        parcel.writeString(amount);
        parcel.writeString(order_status);
        parcel.writeString(customer_name);
        parcel.writeString(customer_email);
        parcel.writeString(customer_mobile);
        parcel.writeString(order_date);
        parcel.writeString(order_time);
        parcel.writeString(ip);
        parcel.writeString(country);
        parcel.writeString(payment_method);
        if (baseResponse != null)
            parcel.writeParcelable(baseResponse, i);
    }
}
