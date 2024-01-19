package com.ulisfintech.telrpay.ui.order;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

public class CustomerDetails implements Parcelable {

    @Expose
    private String name;
    @Expose
    private String email;
    @Expose
    private String mobile;
    @Expose
    private String mobile_code;

    public CustomerDetails() {
    }

    public CustomerDetails(String name, String email, String mobile, String mobile_code) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.mobile_code = mobile_code;
    }

    protected CustomerDetails(Parcel in) {
        name = in.readString();
        email = in.readString();
        mobile = in.readString();
        mobile_code = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(mobile);
        dest.writeString(mobile_code);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CustomerDetails> CREATOR = new Creator<CustomerDetails>() {
        @Override
        public CustomerDetails createFromParcel(Parcel in) {
            return new CustomerDetails(in);
        }

        @Override
        public CustomerDetails[] newArray(int size) {
            return new CustomerDetails[size];
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobile_code() {
        return mobile_code;
    }

    public void setMobile_code(String mobile_code) {
        this.mobile_code = mobile_code;
    }
}
