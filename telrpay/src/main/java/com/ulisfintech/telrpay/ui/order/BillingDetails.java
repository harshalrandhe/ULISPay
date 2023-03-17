package com.ulisfintech.telrpay.ui.order;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

public class BillingDetails  implements Parcelable {

    @Expose
    private String address_line1;
    @Expose
    private String address_line2;
    @Expose
    private String country;
    @Expose
    private String city;
    @Expose
    private String pin;
    @Expose
    private String province;

    public BillingDetails() {
    }

    protected BillingDetails(Parcel in) {
        address_line1 = in.readString();
        address_line2 = in.readString();
        country = in.readString();
        city = in.readString();
        pin = in.readString();
        province = in.readString();
    }

    public static final Creator<BillingDetails> CREATOR = new Creator<BillingDetails>() {
        @Override
        public BillingDetails createFromParcel(Parcel in) {
            return new BillingDetails(in);
        }

        @Override
        public BillingDetails[] newArray(int size) {
            return new BillingDetails[size];
        }
    };

    public String getAddress_line1() {
        return address_line1;
    }

    public void setAddress_line1(String address_line1) {
        this.address_line1 = address_line1;
    }

    public String getAddress_line2() {
        return address_line2;
    }

    public void setAddress_line2(String address_line2) {
        this.address_line2 = address_line2;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address_line1);
        dest.writeString(address_line2);
        dest.writeString(country);
        dest.writeString(city);
        dest.writeString(pin);
        dest.writeString(province);
    }
}
