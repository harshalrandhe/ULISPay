package com.ulisfintech.artha.helper;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

public class PaymentData implements Parcelable {

    @Expose
    private String vendorName;
    @Expose
    private String vendorMobile;
    @Expose
    private String product;
    @Expose
    private double price;

    private String merchantKey;
    private String merchantSecret;

    public PaymentData() {
    }

    public PaymentData(String vendorName, String vendorMobile, String product, double price) {
        this.vendorName = vendorName;
        this.vendorMobile = vendorMobile;
        this.product = product;
        this.price = price;
    }

    protected PaymentData(Parcel in) {
        vendorName = in.readString();
        vendorMobile = in.readString();
        product = in.readString();
        price = in.readDouble();
        merchantKey = in.readString();
        merchantSecret = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(vendorName);
        dest.writeString(vendorMobile);
        dest.writeString(product);
        dest.writeDouble(price);
        dest.writeString(merchantKey);
        dest.writeString(merchantSecret);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PaymentData> CREATOR = new Creator<PaymentData>() {
        @Override
        public PaymentData createFromParcel(Parcel in) {
            return new PaymentData(in);
        }

        @Override
        public PaymentData[] newArray(int size) {
            return new PaymentData[size];
        }
    };

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorMobile() {
        return vendorMobile;
    }

    public void setVendorMobile(String vendorMobile) {
        this.vendorMobile = vendorMobile;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getMerchantKey() {
        return merchantKey;
    }

    public void setMerchantKey(String merchantKey) {
        this.merchantKey = merchantKey;
    }

    public String getMerchantSecret() {
        return merchantSecret;
    }

    public void setMerchantSecret(String merchantSecret) {
        this.merchantSecret = merchantSecret;
    }
}
