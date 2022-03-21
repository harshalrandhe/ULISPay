package com.ulisfintech.myapplication.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.ulisfintech.myapplication.communication.local.AppConstants;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = AppConstants.TABLE_USER)
public class UserBean implements Parcelable {

    @PrimaryKey
    @Expose
    @NotNull
    private String userId;
    @Expose
    @ColumnInfo
    private String name;
    @Expose
    @ColumnInfo
    private String mobile;
    @Expose
    @ColumnInfo
    private boolean isMobileVerified;
    @Expose
    @ColumnInfo
    private boolean isEmailVerified;
    @Expose
    @ColumnInfo
    private String pin;

    public UserBean() {

    }

    protected UserBean(Parcel in) {
        userId = in.readString();
        name = in.readString();
        mobile = in.readString();
        isMobileVerified = in.readByte() != 0;
        isEmailVerified = in.readByte() != 0;
        pin = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(name);
        dest.writeString(mobile);
        dest.writeByte((byte) (isMobileVerified ? 1 : 0));
        dest.writeByte((byte) (isEmailVerified ? 1 : 0));
        dest.writeString(pin);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserBean> CREATOR = new Creator<UserBean>() {
        @Override
        public UserBean createFromParcel(Parcel in) {
            return new UserBean(in);
        }

        @Override
        public UserBean[] newArray(int size) {
            return new UserBean[size];
        }
    };

    @NotNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NotNull String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public boolean isMobileVerified() {
        return isMobileVerified;
    }

    public void setMobileVerified(boolean mobileVerified) {
        isMobileVerified = mobileVerified;
    }

    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
