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

import com.herokuapp.jordan_chau.grip.R;
import com.herokuapp.jordan_chau.grip.adapters.ReceiptCardAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryFragment extends Fragment implements ReceiptCardAdapter.ReceiptItemClickListener{
    @BindView(R.id.rv_receipt_cards) RecyclerView mReceiptList;
    private ReceiptCardAdapter mAdapter;

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

        mAdapter = new ReceiptCardAdapter(this);
        mReceiptList.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onRecipeItemClicked(int clickedItemIndex) {

    }
}
