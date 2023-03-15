package com.ulisfintech.telrpay.ui;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;

public class TransactionResponseBean implements Parcelable {

    @Expose
    private String order_id;

    @Expose
    private String transaction_id;

    @Expose
    private String status;

    @Expose
    private String currency;

    @Expose
    private double amount;

    @Expose
    private String payment_method;

    @Expose
    private String return_url;

    @Expose
    private String result_code;

    @Expose
    private String result_message;

    @Expose
    private String cardNumber;

    @Expose
    private long txnDate;

    public TransactionResponseBean() {
    }

    protected TransactionResponseBean(Parcel in) {
        order_id = in.readString();
        transaction_id = in.readString();
        status = in.readString();
        currency = in.readString();
        amount = in.readDouble();
        payment_method = in.readString();
        return_url = in.readString();
        result_code = in.readString();
        result_message = in.readString();
        cardNumber = in.readString();
        txnDate = in.readLong();
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

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    @NonNull
    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(@NonNull String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getReturn_url() {
        return return_url;
    }

    public void setReturn_url(String return_url) {
        this.return_url = return_url;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getResult_message() {
        return result_message;
    }

    public void setResult_message(String result_message) {
        this.result_message = result_message;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public long getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(long txnDate) {
        this.txnDate = txnDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(order_id);
        parcel.writeString(transaction_id);
        parcel.writeString(status);
        parcel.writeString(currency);
        parcel.writeDouble(amount);
        parcel.writeString(payment_method);
        parcel.writeString(return_url);
        parcel.writeString(result_code);
        parcel.writeString(result_message);
        parcel.writeString(cardNumber);
        parcel.writeLong(txnDate);
    }
}
