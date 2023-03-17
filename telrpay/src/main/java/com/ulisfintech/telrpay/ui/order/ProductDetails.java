package com.ulisfintech.telrpay.ui.order;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

public class ProductDetails implements Parcelable {

    @Expose
    private String vendorName;
    @Expose
    private String vendorMobile;
    @Expose
    private String productName;
    @Expose
    private double productPrice;
    @Expose
    private String currency;
    @Expose
    private String image;

    public ProductDetails() {
    }

    protected ProductDetails(Parcel in) {
        vendorName = in.readString();
        vendorMobile = in.readString();
        productName = in.readString();
        productPrice = in.readDouble();
        currency = in.readString();
        image = in.readString();
    }

    public static final Creator<ProductDetails> CREATOR = new Creator<ProductDetails>() {
        @Override
        public ProductDetails createFromParcel(Parcel in) {
            return new ProductDetails(in);
        }

        @Override
        public ProductDetails[] newArray(int size) {
            return new ProductDetails[size];
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(vendorName);
        dest.writeString(vendorMobile);
        dest.writeString(productName);
        dest.writeDouble(productPrice);
        dest.writeString(currency);
        dest.writeString(image);
    }
}
