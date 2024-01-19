package com.ulisfintech.telrpay.ui;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.ulisfintech.telrpay.ui.order.MerchantDetails;

import java.util.List;

public class OrderStatusBean implements Parcelable {

    @Expose
    private List<TransactionResponseBean> transactions;
    @Expose
    private String transaction_id;
    @Expose
    private String authcode;
    @Expose
    private String channel;
    @Expose
    private String store_id;
    @Expose
    private String description;
    @Expose
    private String company_name;
    @Expose
    private String card_brand;
    @Expose
    private String funding_method;
    @Expose
    private String last_update_date;
    @Expose
    private String order_id;
    @Expose
    private String account_identifier;
    @Expose
    private String total_amount;
    @Expose
    private String payment_method;
    @Expose
    private MerchantDetails merchant_details;
    @Expose
    private CustomerDetailsResponse customer_details;
    @Expose
    private String prefer_lang;
    private String status;

    public OrderStatusBean() {

    }

    protected OrderStatusBean(Parcel in) {
        transactions = in.createTypedArrayList(TransactionResponseBean.CREATOR);
        transaction_id = in.readString();
        authcode = in.readString();
        channel = in.readString();
        store_id = in.readString();
        description = in.readString();
        company_name = in.readString();
        card_brand = in.readString();
        funding_method = in.readString();
        last_update_date = in.readString();
        order_id = in.readString();
        account_identifier = in.readString();
        total_amount = in.readString();
        payment_method = in.readString();
        merchant_details = in.readParcelable(MerchantDetails.class.getClassLoader());
        customer_details = in.readParcelable(CustomerDetailsResponse.class.getClassLoader());
        prefer_lang = in.readString();
        status = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(transactions);
        dest.writeString(transaction_id);
        dest.writeString(authcode);
        dest.writeString(channel);
        dest.writeString(store_id);
        dest.writeString(description);
        dest.writeString(company_name);
        dest.writeString(card_brand);
        dest.writeString(funding_method);
        dest.writeString(last_update_date);
        dest.writeString(order_id);
        dest.writeString(account_identifier);
        dest.writeString(total_amount);
        dest.writeString(payment_method);
        dest.writeParcelable(merchant_details, flags);
        dest.writeParcelable(customer_details, flags);
        dest.writeString(prefer_lang);
        dest.writeString(status);
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

    public List<TransactionResponseBean> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionResponseBean> transactions) {
        this.transactions = transactions;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getAuthcode() {
        return authcode;
    }

    public void setAuthcode(String authcode) {
        this.authcode = authcode;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCard_brand() {
        return card_brand;
    }

    public void setCard_brand(String card_brand) {
        this.card_brand = card_brand;
    }

    public String getFunding_method() {
        return funding_method;
    }

    public void setFunding_method(String funding_method) {
        this.funding_method = funding_method;
    }

    public String getLast_update_date() {
        return last_update_date;
    }

    public void setLast_update_date(String last_update_date) {
        this.last_update_date = last_update_date;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getAccount_identifier() {
        return account_identifier;
    }

    public void setAccount_identifier(String account_identifier) {
        this.account_identifier = account_identifier;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public MerchantDetails getMerchant_details() {
        return merchant_details;
    }

    public void setMerchant_details(MerchantDetails merchant_details) {
        this.merchant_details = merchant_details;
    }

    public CustomerDetailsResponse getCustomer_details() {
        return customer_details;
    }

    public void setCustomer_details(CustomerDetailsResponse customer_details) {
        this.customer_details = customer_details;
    }

    public String getPrefer_lang() {
        return prefer_lang;
    }

    public void setPrefer_lang(String prefer_lang) {
        this.prefer_lang = prefer_lang;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
