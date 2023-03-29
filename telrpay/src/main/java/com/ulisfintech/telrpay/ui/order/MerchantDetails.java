package com.ulisfintech.telrpay.ui.order;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

public class MerchantDetails implements Parcelable {

    @Expose
    private String merchant_name;

    public MerchantDetails() {
    }


    protected MerchantDetails(Parcel in) {
        merchant_name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(merchant_name);
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

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }
}
