package com.herokuapp.jordan_chau.grip.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.herokuapp.jordan_chau.grip.R;

import butterknife.ButterKnife;

public class NewBillFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View rootView = inflater.inflate(R.layout.fragment_new_bill, container, false);
        ButterKnife.bind(this, rootView);

       return rootView;
    }
}
