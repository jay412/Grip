package com.herokuapp.jordan_chau.grip.model;

import android.media.Image;

import java.util.ArrayList;

public class Receipt {
    private String mDate, mLabel;
    private ArrayList<ReceiptItem> mReceiptItems;
    private Image mReceiptPicture;
    private int mNumPplSharing;
    private double mTax, mTip, mTotal;

    public Receipt(String date, String label, ArrayList<ReceiptItem> receiptItems, Image receiptPicture, int numPplSharing, double tax, double tip, double total) {
        mDate = date;
        mLabel = label;
        mReceiptItems = receiptItems;
        mReceiptPicture = receiptPicture;
        mNumPplSharing = numPplSharing;
        mTax = tax;
        mTip = tip;
        mTotal = total;
    }
}
