package com.ulisfintech.telrpay.ui;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;

public class TransactionResponseBean implements Parcelable {

    @Expose
    private String date_time;
    @Expose
    private String type;
    @Expose
    private String gateway_code;
    @Expose
    private String amount;
    @Expose
    private String transaction_id;
    @Expose
    private String created_by;

    public TransactionResponseBean() {
    }

    protected TransactionResponseBean(Parcel in) {
        date_time = in.readString();
        type = in.readString();
        gateway_code = in.readString();
        amount = in.readString();
        transaction_id = in.readString();
        created_by = in.readString();
    }

    public static final Creator<TransactionResponseBean> CREATOR = new Creator<TransactionResponseBean>() {
        @Override
        public TransactionResponseBean createFromParcel(Parcel in) {
            return new TransactionResponseBean(in);
        }

        @Override
        public TransactionResponseBean[] newArray(int size) {
            return new TransactionResponseBean[size];
        }
    };

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGateway_code() {
        return gateway_code;
    }

    public void setGateway_code(String gateway_code) {
        this.gateway_code = gateway_code;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(date_time);
        dest.writeString(type);
        dest.writeString(gateway_code);
        dest.writeString(amount);
        dest.writeString(transaction_id);
        dest.writeString(created_by);
    }
}
