package com.herokuapp.jordan_chau.grip.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.herokuapp.jordan_chau.grip.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewBillFragment extends Fragment{
    @BindView(R.id.fab_add_new_item) FloatingActionButton mAddItem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View rootView = inflater.inflate(R.layout.fragment_new_bill, container, false);
        ButterKnife.bind(this, rootView);

        setUpButtons();

       return rootView;
    }

    public void showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new CreateNewItemDialogFragment();
        dialog.show(getActivity().getSupportFragmentManager(), "CreateNewItemDialogFragment");
    }

    private void setUpButtons(){
        mAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNoticeDialog();
            }
        });
    }
}
