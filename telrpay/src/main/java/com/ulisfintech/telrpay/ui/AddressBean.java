package com.ulisfintech.telrpay.ui;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

public class AddressBean implements Parcelable {

    @Expose
    private String address_line_1;
    @Expose
    private String address_line_2;
    @Expose
    private String city;
    @Expose
    private String province;
    @Expose
    private String country;
    @Expose
    private String pincode;

    public AddressBean() {
    }

    protected AddressBean(Parcel in) {
        address_line_1 = in.readString();
        address_line_2 = in.readString();
        city = in.readString();
        province = in.readString();
        country = in.readString();
        pincode = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address_line_1);
        dest.writeString(address_line_2);
        dest.writeString(city);
        dest.writeString(province);
        dest.writeString(country);
        dest.writeString(pincode);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AddressBean> CREATOR = new Creator<AddressBean>() {
        @Override
        public AddressBean createFromParcel(Parcel in) {
            return new AddressBean(in);
        }

        @Override
        public AddressBean[] newArray(int size) {
            return new AddressBean[size];
        }
    };

    public String getAddress_line_1() {
        return address_line_1;
    }

    public void setAddress_line_1(String address_line_1) {
        this.address_line_1 = address_line_1;
    }

    public String getAddress_line_2() {
        return address_line_2;
    }

    public void setAddress_line_2(String address_line_2) {
        this.address_line_2 = address_line_2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
