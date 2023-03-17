package com.ulisfintech.telrpay.helper;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.ulisfintech.telrpay.ui.order.BillingDetails;
import com.ulisfintech.telrpay.ui.order.CustomerDetails;
import com.ulisfintech.telrpay.ui.order.ProductDetails;
import com.ulisfintech.telrpay.ui.order.ShippingDetails;

public class PaymentData implements Parcelable {

    @Expose
    private ProductDetails productDetails;
    @Expose
    private CustomerDetails customer_details;
    @Expose
    private BillingDetails billing_details;
    @Expose
    private ShippingDetails shipping_details;

    @Expose
    private int paymentType;

    private String merchantKey;
    private String merchantSecret;
    private ProductBean productBean;
    private String returnUrl;

    public PaymentData() {

    }

    protected PaymentData(Parcel in) {
        productDetails = in.readParcelable(ProductDetails.class.getClassLoader());
        customer_details = in.readParcelable(CustomerDetails.class.getClassLoader());
        billing_details = in.readParcelable(BillingDetails.class.getClassLoader());
        shipping_details = in.readParcelable(ShippingDetails.class.getClassLoader());
        paymentType = in.readInt();
        merchantKey = in.readString();
        merchantSecret = in.readString();
        productBean = in.readParcelable(ProductBean.class.getClassLoader());
        returnUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(productDetails, flags);
        dest.writeParcelable(customer_details, flags);
        dest.writeParcelable(billing_details, flags);
        dest.writeParcelable(shipping_details, flags);
        dest.writeInt(paymentType);
        dest.writeString(merchantKey);
        dest.writeString(merchantSecret);
        dest.writeParcelable(productBean, flags);
        dest.writeString(returnUrl);
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

    public ProductDetails getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(ProductDetails productDetails) {
        this.productDetails = productDetails;
    }

    public CustomerDetails getCustomer_details() {
        return customer_details;
    }

    public void setCustomer_details(CustomerDetails customer_details) {
        this.customer_details = customer_details;
    }

    public BillingDetails getBilling_details() {
        return billing_details;
    }

    public void setBilling_details(BillingDetails billing_details) {
        this.billing_details = billing_details;
    }

    public ShippingDetails getShipping_details() {
        return shipping_details;
    }

    public void setShipping_details(ShippingDetails shipping_details) {
        this.shipping_details = shipping_details;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
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

    public ProductBean getProductBean() {
        return productBean;
    }

    public void setProductBean(ProductBean productBean) {
        this.productBean = productBean;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }
}
