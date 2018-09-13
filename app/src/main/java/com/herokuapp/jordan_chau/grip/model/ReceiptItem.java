package com.herokuapp.jordan_chau.grip.model;

public class ReceiptItem {
    private int mQuantity;
    private String mName, mPrice;

    public ReceiptItem(int quantity, String name, String price) {
        mQuantity = quantity;
        mName = name;
        mPrice = price;
    }
}
