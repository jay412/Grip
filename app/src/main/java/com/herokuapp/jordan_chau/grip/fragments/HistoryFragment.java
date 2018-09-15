package com.herokuapp.jordan_chau.grip.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    private ReceiptCardAdapter mAdapter;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private ArrayList<ReceiptItem> mReceiptItems;

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

        mReceiptItems = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("receipts");
        setUpDatabaseListeners();


        Timber.d("list: %s", mReceiptItems.toString());

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
                //Timber.d("dataSnapshot Key: %s", dataSnapshot.getKey());
                /*for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Object data = snapshot.getValue();
                    Timber.d("data: %s", data);
                } */
                //Timber.d("data: %s", dataSnapshot.getValue());
                int quantity = dataSnapshot.child("quantity").getValue(Integer.class);
                String name = dataSnapshot.child("name").getValue(String.class);
                double price = dataSnapshot.child("price").getValue(Double.class);

                ReceiptItem createdItem = new ReceiptItem(quantity, name, price);
                mReceiptItems.add(createdItem);
                //mAdapter.notifyItemInserted(mAdapter.getItemCount() - 1);
                //mAdapter.notifyDataSetChanged();
                //TODO: figure out why mReceiptItems is not updating accordinly
                Timber.d("name is: %s", createdItem.getName());
                Timber.d("last item added: %s", mReceiptItems.get(0).getName());
                Timber.d("list size: %s", mReceiptItems.size());

                mAdapter = new ReceiptCardAdapter(mReceiptItems, HistoryFragment.this);
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
