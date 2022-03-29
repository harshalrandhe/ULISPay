package com.ulisfintech.artha.ui;

import android.os.Parcel;
import android.os.Parcelable;

public class SyncMessage implements Parcelable {

    public String message;
    public Object data;
    public boolean status;

    public SyncMessage() {
    }

    protected SyncMessage(Parcel in) {
        message = in.readString();
        status = in.readByte() != 0;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(message);
        parcel.writeByte((byte) (status ? 1 : 0));
    }
}
