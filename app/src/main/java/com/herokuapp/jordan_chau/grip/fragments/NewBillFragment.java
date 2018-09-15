package com.herokuapp.jordan_chau.grip.fragments;

import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.herokuapp.jordan_chau.grip.R;
import com.herokuapp.jordan_chau.grip.adapters.BillitemAdapter;
import com.herokuapp.jordan_chau.grip.model.Receipt;
import com.herokuapp.jordan_chau.grip.model.ReceiptItem;
import com.herokuapp.jordan_chau.grip.ui.NavigationActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class NewBillFragment extends Fragment implements NavigationActivity.CreateNewItemCallback, BillitemAdapter.BillItemClickListener{
    @BindView(R.id.fab_add_new_item) FloatingActionButton mAddItem;
    @BindView(R.id.rv_item_list) RecyclerView mItemList;
    @BindView(R.id.fab_take_picture) FloatingActionButton mTakePicture;
    @BindView(R.id.iv_receipt_image) ImageView mReceiptImage;
    @BindView(R.id.btn_calculate_total) Button mCalculateTotal;
    private BillitemAdapter mAdapter;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int RESULT_OK = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View rootView = inflater.inflate(R.layout.fragment_new_bill, container, false);
        ButterKnife.bind(this, rootView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mItemList.setLayoutManager(layoutManager);
        mItemList.setHasFixedSize(true);

            //TODO remove this?
            mAdapter = new BillitemAdapter(new ArrayList<ReceiptItem>(), this);
            mItemList.setAdapter(mAdapter);

        setUpButtons();
       return rootView;
    }

    public void showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new CreateNewItemDialogFragment();
        dialog.show(getActivity().getSupportFragmentManager(), "CreateNewItemDialogFragment");
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //TODO save image here?
            mReceiptImage.setImageBitmap(imageBitmap);
        }
    }

    private void setUpButtons(){
        //add item fab listener
        mAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNoticeDialog();
            }
        });

        //camera fab listener
        PackageManager pm = getActivity().getPackageManager();
        final boolean deviceHasCamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
        if(!deviceHasCamera) {
            mTakePicture.setEnabled(false);
        } else {
            mTakePicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dispatchTakePictureIntent();
                }
            });
        }

        //calculate total button listener
        mCalculateTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO implement
            }
        });
    }

    @Override
    public void receiveNewItemData(ReceiptItem createdItem) {
        if(mAdapter != null) {
            mAdapter.addItem(createdItem);
            mAdapter.notifyItemInserted(mAdapter.getItemCount() - 1);
        } else {
            mAdapter = new BillitemAdapter(new ArrayList<ReceiptItem>(), this);
            //why is adapter still null?
            mItemList.setAdapter(mAdapter);
            mAdapter.addItem(createdItem);
            mAdapter.notifyItemInserted(mAdapter.getItemCount() - 1);
        }
    }

    @Override
    public void onBillItemClicked(int clickedItemIndex) {
        //TODO delete item when clicked
    }
}
