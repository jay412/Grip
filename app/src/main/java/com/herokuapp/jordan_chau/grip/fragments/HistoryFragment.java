package com.herokuapp.jordan_chau.grip.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.herokuapp.jordan_chau.grip.R;
import com.herokuapp.jordan_chau.grip.adapters.ReceiptCardAdapter;
import com.herokuapp.jordan_chau.grip.model.Receipt;
import com.herokuapp.jordan_chau.grip.model.ReceiptItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class HistoryFragment extends Fragment implements ReceiptCardAdapter.ReceiptItemClickListener{
    @BindView(R.id.rv_receipt_cards) RecyclerView mReceiptList;
    @BindView(R.id.bottom_sheet) FrameLayout mBottomSheet;
    private ReceiptCardAdapter mAdapter;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private ArrayList<Receipt> mReceipts;
    private BottomSheetDialog mBottomSheetDialog;

    //TODO make final strings for database child names

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
        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                getAllReceipts(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                getAllReceipts(dataSnapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                deleteReceipt(dataSnapshot);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getAllReceipts(DataSnapshot dataSnapshot) {
        //retrieve and parse data from database
        String date = dataSnapshot.child("date").getValue(String.class);
        //double grandTotal = dataSnapshot.child("grandTotal").getValue(Double.class);
        String label = dataSnapshot.child("label").getValue(String.class);

        ArrayList<ReceiptItem> receiptItems = new ArrayList<>();
        for(DataSnapshot snapshot : dataSnapshot.child("mReceiptItems").getChildren()) {
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

        //create receipt object from database data
        Receipt receipt = new Receipt(label, receiptItems, receiptPicture, numPplSharing, tax, tip);
        receipt.setDate(date);
        mReceipts.add(receipt);
        //refresh recyclerview
        mAdapter = new ReceiptCardAdapter(mReceipts, HistoryFragment.this);
        mReceiptList.setAdapter(mAdapter);
    }

    //detects if receipt is deleted from database and refreshes recyclerview
    private void deleteReceipt(DataSnapshot dataSnapshot) {
        //for(DataSnapshot selectedSnapshot : dataSnapshot.getChildren()) {
            //TODO need to implement an unique ID field and change this
            String receiptPicture = dataSnapshot.child("receiptPicture").getValue(String.class);
            for (int x = 0; x < mReceipts.size(); ++x) {
                if(mReceipts.get(x).getReceiptPicture().equals(receiptPicture)) {
                    mReceipts.remove(x);
                }
            }

            mAdapter.notifyDataSetChanged();
            mAdapter = new ReceiptCardAdapter(mReceipts, HistoryFragment.this);
            mReceiptList.setAdapter(mAdapter);
        //}
    }

    //trigger receipt deletion from bottom sheet dialog
    private void triggerReceiptDeletion(String receiptPicture) {
        Query receiptQuery = mRef.orderByChild("receiptPicture").equalTo(receiptPicture);
        receiptQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot receiptSnapshot : dataSnapshot.getChildren()) {
                    receiptSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Timber.d("onCancelled%s", databaseError.toException());
            }
        });
    }

    @Override
    public void onReceiptItemClicked(int clickedItemIndex) {
        showBottomSheetDialog(clickedItemIndex);
    }

    private void showBottomSheetDialog(int clickedItemIndex){
        final Receipt receipt = mReceipts.get(clickedItemIndex);

        final View bottomSheetLayout = getLayoutInflater().inflate(R.layout.dialog_bottom_sheet, null);

        TextView title = bottomSheetLayout.findViewById(R.id.tv_title);
        TextView details = bottomSheetLayout.findViewById(R.id.tv_detail);

        title.setText(receipt.getLabel());
        final String receiptDetails = receipt.toString();
        details.setText(receiptDetails);

        (bottomSheetLayout.findViewById(R.id.button_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.dismiss();
            }
        });
        (bottomSheetLayout.findViewById(R.id.button_delete)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO fix this later
                triggerReceiptDeletion(receipt.getReceiptPicture());
                mBottomSheetDialog.dismiss();
            }
        });
        (bottomSheetLayout.findViewById(R.id.button_email)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(Intent.createChooser(CalculateTotalDialogFragment.setUpEmailIntent(receiptDetails), "Send Email"));
                }
        });

        mBottomSheetDialog = new BottomSheetDialog(getActivity());
        mBottomSheetDialog.setContentView(bottomSheetLayout);
        mBottomSheetDialog.show();
    }
}
