package com.ulisfintech.telrpay.helper;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionBean implements Parcelable {

    @Expose
    @SerializedName("class")
    private String classname;
    @Expose
    @SerializedName("integration")
    private String integration;

    public TransactionBean() {
    }

    public TransactionBean(String classname, String integration) {
        this.classname = classname;
        this.integration = integration;
    }

    protected TransactionBean(Parcel in) {
        classname = in.readString();
        integration = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(classname);
        dest.writeString(integration);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TransactionBean> CREATOR = new Creator<TransactionBean>() {
        @Override
        public TransactionBean createFromParcel(Parcel in) {
            return new TransactionBean(in);
        }

        @Override
        public TransactionBean[] newArray(int size) {
            return new TransactionBean[size];
        }
    };

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getIntegration() {
        return integration;
    }

    public void setIntegration(String integration) {
        this.integration = integration;
    }
}
