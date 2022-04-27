package com.ulisfintech.artha.helper;

import android.os.Parcel;
import android.os.Parcelable;

import com.ulisfintech.artha.ui.OrderStatusBean;
import com.ulisfintech.artha.ui.TransactionResponseBean;

public class SyncMessage implements Parcelable {

    public String message;
    public Object data;
    public boolean status;
    public String orderId;
    public String transactionId;

    public SyncMessage() {
    }

    protected SyncMessage(Parcel in) {
        message = in.readString();
        status = in.readByte() != 0;
        orderId = in.readString();
        transactionId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
        dest.writeByte((byte) (status ? 1 : 0));
        dest.writeString(orderId);
        dest.writeString(transactionId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SyncMessage> CREATOR = new Creator<SyncMessage>() {
        @Override
        public SyncMessage createFromParcel(Parcel in) {
            return new SyncMessage(in);
        }

        @Override
        public SyncMessage[] newArray(int size) {
            return new SyncMessage[size];
        }
    };
}
