package com.ulisfintech.telrpay.helper;

import android.os.Parcel;
import android.os.Parcelable;

public class BaseResponse implements Parcelable {

    private String status;
    private String message;

    public BaseResponse() {
    }

    public BaseResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    protected BaseResponse(Parcel in) {
        status = in.readString();
        message = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeString(message);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BaseResponse> CREATOR = new Creator<BaseResponse>() {
        @Override
        public BaseResponse createFromParcel(Parcel in) {
            return new BaseResponse(in);
        }

        @Override
        public BaseResponse[] newArray(int size) {
            return new BaseResponse[size];
        }
    };

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
