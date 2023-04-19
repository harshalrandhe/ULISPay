package com.ulisfintech.telrpay.helper;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.SurfaceControl;

import com.google.gson.annotations.Expose;
import com.ulisfintech.telrpay.ui.order.BillingDetails;
import com.ulisfintech.telrpay.ui.order.CustomerDetails;
import com.ulisfintech.telrpay.ui.order.MerchantUrls;
import com.ulisfintech.telrpay.ui.order.OrderDetails;
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
    private OrderDetails order_details;
    @Expose
    private MerchantUrls merchant_urls;
    @Expose
    private TransactionBean transaction;
    private String merchantKey;
    private String merchantSecret;
    private ProductBean productBean;

    public PaymentData() {

    }

    protected PaymentData(Parcel in) {
        productDetails = in.readParcelable(ProductDetails.class.getClassLoader());
        customer_details = in.readParcelable(CustomerDetails.class.getClassLoader());
        billing_details = in.readParcelable(BillingDetails.class.getClassLoader());
        shipping_details = in.readParcelable(ShippingDetails.class.getClassLoader());
        order_details = in.readParcelable(OrderDetails.class.getClassLoader());
        merchant_urls = in.readParcelable(MerchantUrls.class.getClassLoader());
        transaction = in.readParcelable(TransactionBean.class.getClassLoader());
        merchantKey = in.readString();
        merchantSecret = in.readString();
        productBean = in.readParcelable(ProductBean.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(productDetails, flags);
        dest.writeParcelable(customer_details, flags);
        dest.writeParcelable(billing_details, flags);
        dest.writeParcelable(shipping_details, flags);
        dest.writeParcelable(order_details, flags);
        dest.writeParcelable(merchant_urls, flags);
        dest.writeParcelable(transaction, flags);
        dest.writeString(merchantKey);
        dest.writeString(merchantSecret);
        dest.writeParcelable(productBean, flags);
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

    public OrderDetails getOrder_details() {
        return order_details;
    }

    public void setOrder_details(OrderDetails order_details) {
        this.order_details = order_details;
    }

    public MerchantUrls getMerchant_urls() {
        return merchant_urls;
    }

    public void setMerchant_urls(MerchantUrls merchant_urls) {
        this.merchant_urls = merchant_urls;
    }

    public TransactionBean getTransaction() {
        return transaction;
    }

    public void setTransaction(TransactionBean transaction) {
        this.transaction = transaction;
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

}
