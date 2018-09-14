package com.herokuapp.jordan_chau.grip.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ReceiptItem implements Parcelable{
    private int mQuantity;
    private String mName;
    private double mPrice;

    public ReceiptItem(int quantity, String name, double price) {
        mQuantity = quantity;
        mName = name;
        mPrice = price;
    }

    protected ReceiptItem(Parcel in) {
        mQuantity = in.readInt();
        mName = in.readString();
        mPrice = in.readDouble();
    }

    public static final Creator<ReceiptItem> CREATOR = new Creator<ReceiptItem>() {
        @Override
        public ReceiptItem createFromParcel(Parcel in) {
            return new ReceiptItem(in);
        }

        @Override
        public ReceiptItem[] newArray(int size) {
            return new ReceiptItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mQuantity);
        dest.writeString(mName);
        dest.writeDouble(mPrice);
    }

    public int getQuantity(){
        return mQuantity;
    }

    public String getName(){
        return mName;
    }

    public double getPrice(){
        return mPrice;
    }
}
