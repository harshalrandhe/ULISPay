package com.ulisfintech.telrpay.ui.order;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

public class MerchantDetails implements Parcelable {

    @Expose
    private String name;
    @Expose
    private String email;
    @Expose
    private String mobile_no;

    public MerchantDetails() {
    }

    protected MerchantDetails(Parcel in) {
        name = in.readString();
        email = in.readString();
        mobile_no = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(mobile_no);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MerchantDetails> CREATOR = new Creator<MerchantDetails>() {
        @Override
        public MerchantDetails createFromParcel(Parcel in) {
            return new MerchantDetails(in);
        }

        @Override
        public MerchantDetails[] newArray(int size) {
            return new MerchantDetails[size];
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
}
