package com.ulisfintech.telrpay.helper;

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
    @Expose
    private String currency;
    @Expose
    private String customerName;
    @Expose
    private String customerEmail;
    @Expose
    private String customerMobile;
    @Expose
    private String returnUrl;
    @Expose
    private int paymentType;

    private String merchantKey;
    private String merchantSecret;
    private ProductBean productBean;

    public PaymentData() {

    }

    protected PaymentData(Parcel in) {
        vendorName = in.readString();
        vendorMobile = in.readString();
        product = in.readString();
        price = in.readDouble();
        currency = in.readString();
        customerName = in.readString();
        customerEmail = in.readString();
        customerMobile = in.readString();
        merchantKey = in.readString();
        merchantSecret = in.readString();
        returnUrl = in.readString();
        paymentType = in.readInt();
//        if (productBean != null) {
            productBean = in.readParcelable(ProductBean.class.getClassLoader());
//        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(vendorName);
        dest.writeString(vendorMobile);
        dest.writeString(product);
        dest.writeDouble(price);
        dest.writeString(currency);
        dest.writeString(customerName);
        dest.writeString(customerEmail);
        dest.writeString(customerMobile);
        dest.writeString(merchantKey);
        dest.writeString(merchantSecret);
        dest.writeString(returnUrl);
        dest.writeInt(paymentType);
        if (productBean != null) {
            dest.writeParcelable(productBean, flags);
        }
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
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

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public ProductBean getProductBean() {
        return productBean;
    }

    public void setProductBean(ProductBean productBean) {
        this.productBean = productBean;
    }
}
