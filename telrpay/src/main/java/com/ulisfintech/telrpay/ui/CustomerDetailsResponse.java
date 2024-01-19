package com.ulisfintech.telrpay.ui;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

public class CustomerDetailsResponse implements Parcelable {


    @Expose
    private String name;
    @Expose
    private String email;
    @Expose
    private String mobile_no;
    @Expose
    private String mobile_code;
    @Expose
    private AddressBean billing_address;
    @Expose
    private AddressBean shipping_address;
    @Expose
    private String redirect_url;

    public CustomerDetailsResponse() {
    }

    protected CustomerDetailsResponse(Parcel in) {
        name = in.readString();
        email = in.readString();
        mobile_no = in.readString();
        mobile_code = in.readString();
        billing_address = in.readParcelable(AddressBean.class.getClassLoader());
        shipping_address = in.readParcelable(AddressBean.class.getClassLoader());
        redirect_url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(mobile_no);
        dest.writeString(mobile_code);
        dest.writeParcelable(billing_address, flags);
        dest.writeParcelable(shipping_address, flags);
        dest.writeString(redirect_url);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CustomerDetailsResponse> CREATOR = new Creator<CustomerDetailsResponse>() {
        @Override
        public CustomerDetailsResponse createFromParcel(Parcel in) {
            return new CustomerDetailsResponse(in);
        }

        @Override
        public CustomerDetailsResponse[] newArray(int size) {
            return new CustomerDetailsResponse[size];
        }
    };

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

    public String getMobile_code() {
        return mobile_code;
    }

    public void setMobile_code(String mobile_code) {
        this.mobile_code = mobile_code;
    }

    public AddressBean getBilling_address() {
        return billing_address;
    }

    public void setBilling_address(AddressBean billing_address) {
        this.billing_address = billing_address;
    }

    public AddressBean getShipping_address() {
        return shipping_address;
    }

    public void setShipping_address(AddressBean shipping_address) {
        this.shipping_address = shipping_address;
    }

    public String getRedirect_url() {
        return redirect_url;
    }

    public void setRedirect_url(String redirect_url) {
        this.redirect_url = redirect_url;
    }
}
