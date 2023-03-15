package com.ulisfintech.telrpay.helper;

import android.os.Parcel;
import android.os.Parcelable;

import com.ulisfintech.telrpay.ui.OrderStatusBean;
import com.ulisfintech.telrpay.ui.TransactionResponseBean;

public class SyncMessage implements Parcelable {

    public String message;
    public Object data;
    public boolean status;
    public String orderId;
    public String transactionId;
    public TransactionResponseBean transactionResponseBean;
    public OrderResponse orderResponse;
    public OrderStatusBean orderStatusBean;

    public SyncMessage() {
    }

    protected SyncMessage(Parcel in) {
        message = in.readString();
        status = in.readByte() != 0;
        orderId = in.readString();
        transactionId = in.readString();
        transactionResponseBean = in.readParcelable(TransactionResponseBean.class.getClassLoader());
        orderResponse = in.readParcelable(OrderResponse.class.getClassLoader());
        orderStatusBean = in.readParcelable(OrderStatusBean.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
        dest.writeByte((byte) (status ? 1 : 0));
        dest.writeString(orderId);
        dest.writeString(transactionId);
        dest.writeParcelable(transactionResponseBean, flags);
        dest.writeParcelable(orderResponse, flags);
        dest.writeParcelable(orderStatusBean, flags);
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
