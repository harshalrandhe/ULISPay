package com.ulisfintech.telrpay.helper;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductBean implements Parcelable {

    private String pId;
    private String name;
    private String desc;
    private double price;
    private String img;
    private String category;

    public ProductBean() {
    }

    protected ProductBean(Parcel in) {
        pId = in.readString();
        name = in.readString();
        desc = in.readString();
        price = in.readDouble();
        img = in.readString();
        category = in.readString();
    }

    public static final Creator<ProductBean> CREATOR = new Creator<ProductBean>() {
        @Override
        public ProductBean createFromParcel(Parcel in) {
            return new ProductBean(in);
        }

        @Override
        public ProductBean[] newArray(int size) {
            return new ProductBean[size];
        }
    };

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(pId);
        parcel.writeString(name);
        parcel.writeString(desc);
        parcel.writeDouble(price);
        parcel.writeString(img);
        parcel.writeString(category);
    }
}
