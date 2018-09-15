package com.herokuapp.jordan_chau.grip.model;

import android.media.Image;

import java.util.ArrayList;

public class Receipt {
    private String mDate, mLabel;
    private ArrayList<ReceiptItem> mReceiptItems;
    private Image mReceiptPicture;
    private int mNumPplSharing;
    private double mTax, mTip, mTotal, mGrandTotal;

    public Receipt(String date, String label, ArrayList<ReceiptItem> receiptItems, Image receiptPicture, int numPplSharing, double tax, double tip, double total) {
        mDate = date;
        mLabel = label;
        mReceiptItems = receiptItems;
        mReceiptPicture = receiptPicture;
        mNumPplSharing = numPplSharing;
        mTax = tax;
        mTip = tip;
        mTotal = total;
        mGrandTotal = calculateGrandTotal(receiptItems, tax, tip);
    }

    public String getmDate() {
        return mDate;
    }

    public String getmLabel() {
        return mLabel;
    }

    public ArrayList<ReceiptItem> getmReceiptItems() {
        return mReceiptItems;
    }

    public Image getmReceiptPicture() {
        return mReceiptPicture;
    }

    public int getmNumPplSharing() {
        return mNumPplSharing;
    }

    public double getmTax() {
        return mTax;
    }

    public double getmTip() {
        return mTip;
    }

    public double getmTotal() {
        return mTotal;
    }

    private double calculateGrandTotal(ArrayList<ReceiptItem> items, double tax, double tip) {
        double grandTotal = 0.0;
        //calculate each item prices based on quantity
        for(int x = 0; x < items.size(); ++x) {
            double currentItemPrice = items.get(x).getQuantity() * items.get(x).getPrice();
            grandTotal += currentItemPrice;
        }
        //add tax and total
        /*
        grandTotal = grandTotal * tax;
        grandTotal += tip;
        */

        grandTotal = grandTotal * 0.08;
        grandTotal += 2.00;
        return grandTotal;
    }
}
