package com.herokuapp.jordan_chau.grip.model;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Receipt implements Parcelable{
    private String mDate, mLabel;
    private ArrayList<ReceiptItem> mReceiptItems;
    private String mReceiptPicture;
    private int mNumPplSharing;
    private double mTax, mTip, mSubTotal, mGrandTotal, mPersonPay;

    public Receipt(String label, ArrayList<ReceiptItem> receiptItems, String receiptPicture, int numPplSharing, double tax, double tip) {
        mDate = getCurrentDate();
        mLabel = label;
        mReceiptItems = receiptItems;
        mReceiptPicture = receiptPicture;
        mNumPplSharing = numPplSharing;
        mTip = tip;
        mSubTotal = calculateSubTotal();
        mTax = calculateTax();
        mGrandTotal = calculateGrandTotal();
        mPersonPay = calculateEachPersonPay();
    }

    protected Receipt(Parcel in) {
        mDate = in.readString();
        mLabel = in.readString();
        mReceiptItems = in.createTypedArrayList(ReceiptItem.CREATOR);
        mReceiptPicture = in.readString();
        mNumPplSharing = in.readInt();
        mTax = in.readDouble();
        mTip = in.readDouble();
        mSubTotal = in.readDouble();
        mGrandTotal = in.readDouble();
        mPersonPay = in.readDouble();
    }

    public static final Creator<Receipt> CREATOR = new Creator<Receipt>() {
        @Override
        public Receipt createFromParcel(Parcel in) {
            return new Receipt(in);
        }

        @Override
        public Receipt[] newArray(int size) {
            return new Receipt[size];
        }
    };

    public String getDate() {
        return mDate;
    }

    public String getLabel() {
        return mLabel;
    }

    public ArrayList<ReceiptItem> getmReceiptItems() {
        return mReceiptItems;
    }

    public String getReceiptPicture() {
        return mReceiptPicture;
    }

    public int getNumPplSharing() {
        return mNumPplSharing;
    }

    public double getTax() {
        return mTax;
    }

    public double getTip() {
        return mTip;
    }

    //TODO: calculate tips later
    public double getSubTotal() {
        return mSubTotal;
    }

    public double getGrandTotal() { return mGrandTotal; }

    public double getPersonPay() { return mPersonPay; }

    public void setDate(String date) {
        mDate = date;
    }

    public void setLabel(String label) { mLabel = label; }

    private String getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        return format.format(calendar.getTime());
    }

    private double calculateSubTotal(){
        double subTotal = 0.0;
        for(int x = 0; x < mReceiptItems.size(); ++x) {
            double currentItemPrice = mReceiptItems.get(x).getQuantity() * mReceiptItems.get(x).getPrice();
            subTotal += currentItemPrice;
        }

        return subTotal;
    }

    private double calculateTax(){
        return mSubTotal * 0.08;
    }

    private double calculateGrandTotal() {
        //calculate each item prices based on quantity
        //add tax and total
        /*
        grandTotal = grandTotal * tax;
        grandTotal += tip;
        */
        return mSubTotal + mTax + 2.00;
    }

    private double calculateEachPersonPay(){
        return mGrandTotal / mNumPplSharing;
    }

    public static String roundToMoneyFormat(double m) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(m);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mDate);
        dest.writeString(mLabel);
        dest.writeTypedList(mReceiptItems);
        dest.writeString(mReceiptPicture);
        dest.writeInt(mNumPplSharing);
        dest.writeDouble(mTax);
        dest.writeDouble(mTip);
        dest.writeDouble(mSubTotal);
        dest.writeDouble(mGrandTotal);
        dest.writeDouble(mPersonPay);
    }

    public String toString(){
        String details = "Item List:" + "\n";

        for(ReceiptItem currentItem : mReceiptItems) {
            String quantity = Integer.toString(currentItem.getQuantity());
            String name = currentItem.getName();
            String price = Double.toString(currentItem.getPrice());

            details += "Quantity: " + quantity + "\n";
            details += "Name: " + name + "\n";
            details += "Price: $" + price + "\n\n";
        }

        details += "\n" + "Subtotal: $" + mSubTotal + "\n";
        details += "Tax: $" + mTax + "\n";
        details += "Tip: $" + mTip + "\n";
        details += "Grand Total: $" + mGrandTotal + "\n";
        details += "Number of People Sharing: " + mNumPplSharing + "\n";
        details += "Each person should pay: $" + mPersonPay + "\n";

        return details;
    }
}
