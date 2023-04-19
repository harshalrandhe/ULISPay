package com.ulisfintech.telrpay.ui.order;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;

 public class MerchantUrls implements Parcelable {

    @Expose
    private String success;
    @Expose
    private String cancel;
    @Expose
    private String failure;

    public MerchantUrls() {
    }

     protected MerchantUrls(Parcel in) {
         success = in.readString();
         cancel = in.readString();
         failure = in.readString();
     }

     public static final Creator<MerchantUrls> CREATOR = new Creator<MerchantUrls>() {
         @Override
         public MerchantUrls createFromParcel(Parcel in) {
             return new MerchantUrls(in);
         }

         @Override
         public MerchantUrls[] newArray(int size) {
             return new MerchantUrls[size];
         }
     };

     public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getCancel() {
        return cancel;
    }

    public void setCancel(String cancel) {
        this.cancel = cancel;
    }

    public String getFailure() {
        return failure;
    }

    public void setFailure(String failure) {
        this.failure = failure;
    }

     @Override
     public int describeContents() {
         return 0;
     }

     @Override
     public void writeToParcel(@NonNull Parcel dest, int flags) {
         dest.writeString(success);
         dest.writeString(cancel);
         dest.writeString(failure);
     }
 }
