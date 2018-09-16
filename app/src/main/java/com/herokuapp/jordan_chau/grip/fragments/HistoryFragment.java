package com.herokuapp.jordan_chau.grip.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.herokuapp.jordan_chau.grip.R;
import com.herokuapp.jordan_chau.grip.adapters.ReceiptCardAdapter;
import com.herokuapp.jordan_chau.grip.model.Receipt;
import com.herokuapp.jordan_chau.grip.model.ReceiptItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryFragment extends Fragment implements ReceiptCardAdapter.ReceiptItemClickListener{
    @BindView(R.id.rv_receipt_cards) RecyclerView mReceiptList;
    private ReceiptCardAdapter mAdapter;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private ArrayList<Receipt> mReceipts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, rootView);

        mReceiptList.setHasFixedSize(true);

        if(savedInstanceState != null) {
            //mRecipes = savedInstanceState.getParcelableArrayList("recipes");
            //mAdapter = new RecipeCardAdapter(mRecipes, this);
            //mRecipeList.setAdapter(mAdapter);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mReceiptList.setLayoutManager(layoutManager);

        mReceipts = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference()
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("receipts");

        setUpDatabaseListeners();

        return rootView;
    }

    private void setUpDatabaseListeners() {
        /*
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                Timber.d("Value is: %s", value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Timber.d("Failed to read value");
            }
        }); */
        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String date = dataSnapshot.child("date").getValue(String.class);
                //double grandTotal = dataSnapshot.child("grandTotal").getValue(Double.class);
                String label = dataSnapshot.child("label").getValue(String.class);

                ArrayList<ReceiptItem> receiptItems = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.child("mReceiptItems").getChildren()) {
                    //Object data = snapshot.getValue();
                    String name = snapshot.child("name").getValue(String.class);
                    double price = snapshot.child("price").getValue(Double.class);
                    int quantity = snapshot.child("quantity").getValue(Integer.class);
                    receiptItems.add(new ReceiptItem(quantity, name, price));
                }

                String receiptPicture = dataSnapshot.child("receiptPicture").getValue(String.class);
                int numPplSharing = dataSnapshot.child("numPplSharing").getValue(Integer.class);
                //double personPay = dataSnapshot.child("personPay").getValue(Double.class);
                //double subTotal = dataSnapshot.child("subTotal").getValue(Double.class);
                double tax = dataSnapshot.child("tax").getValue(Double.class);
                double tip = dataSnapshot.child("tip").getValue(Double.class);


                Receipt receipt = new Receipt(label, receiptItems, receiptPicture, numPplSharing, tax, tip);
                receipt.setDate(date);
                mReceipts.add(receipt);

                mAdapter = new ReceiptCardAdapter(mReceipts, HistoryFragment.this);
                mReceiptList.setAdapter(mAdapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onReceiptItemClicked(int clickedItemIndex) {

    }
}
