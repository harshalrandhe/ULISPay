package com.ulisfintech.telrpay.ui.order;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

public class OrderResponseDetails implements Parcelable {

    @Expose
    private String order_id;
    @Expose
    private String name;
    @Expose
    private String email;
    @Expose
    private String mobile_no;
    @Expose
    private String amount;
    @Expose
    private String currency;
    @Expose
    private String status;
    @Expose
    private String return_url;
    @Expose
    private String success_url;
    @Expose
    private String failed_url;
    @Expose
    private String cancel_url;
    @Expose
    private String env;

    public OrderResponseDetails() {
    }

    protected OrderResponseDetails(Parcel in) {
        order_id = in.readString();
        name = in.readString();
        email = in.readString();
        mobile_no = in.readString();
        amount = in.readString();
        currency = in.readString();
        status = in.readString();
        return_url = in.readString();
        success_url = in.readString();
        failed_url = in.readString();
        cancel_url = in.readString();
        env = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(order_id);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(mobile_no);
        dest.writeString(amount);
        dest.writeString(currency);
        dest.writeString(status);
        dest.writeString(return_url);
        dest.writeString(success_url);
        dest.writeString(failed_url);
        dest.writeString(cancel_url);
        dest.writeString(env);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReturn_url() {
        return return_url;
    }

    public void setReturn_url(String return_url) {
        this.return_url = return_url;
    }

    public String getSuccess_url() {
        return success_url;
    }

    public void setSuccess_url(String success_url) {
        this.success_url = success_url;
    }

    public String getFailed_url() {
        return failed_url;
    }

    public void setFailed_url(String failed_url) {
        this.failed_url = failed_url;
    }

    public String getCancel_url() {
        return cancel_url;
    }

    public void setCancel_url(String cancel_url) {
        this.cancel_url = cancel_url;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }
}
