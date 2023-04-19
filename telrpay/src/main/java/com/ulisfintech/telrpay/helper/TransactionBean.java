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

    public TransactionBean() {
    }

    public TransactionBean(String classname) {
        this.classname = classname;
    }

    protected TransactionBean(Parcel in) {
        classname = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(classname);
    }
}
