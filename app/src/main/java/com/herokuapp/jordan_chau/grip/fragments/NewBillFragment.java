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
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.herokuapp.jordan_chau.grip.R;
import com.herokuapp.jordan_chau.grip.adapters.BillitemAdapter;
import com.herokuapp.jordan_chau.grip.model.Receipt;
import com.herokuapp.jordan_chau.grip.model.ReceiptItem;
import com.herokuapp.jordan_chau.grip.ui.NavigationActivity;

import java.io.ByteArrayOutputStream;
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
    @BindView(R.id.et_sharing_input) EditText mSharing;
    private BillitemAdapter mAdapter;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int RESULT_OK = -1;

    private Bitmap mCurrentPicture;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View rootView = inflater.inflate(R.layout.fragment_new_bill, container, false);
        ButterKnife.bind(this, rootView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mItemList.setLayoutManager(layoutManager);
        mItemList.setHasFixedSize(true);

        setUpButtons();
       return rootView;
    }

    public void showCreateItemDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new CreateNewItemDialogFragment();
        dialog.show(getActivity().getSupportFragmentManager(), "CreateNewItemDialogFragment");
    }

    public void showCalculateTotalDialog() {
        if(areInputsValid()) {
            String bitmapUrl;
            if(mCurrentPicture != null) {
                bitmapUrl = convertBitmapToStringUrl(mCurrentPicture);
            } else {
                bitmapUrl = "";
            }

            Receipt newReceipt = new Receipt("test label", mAdapter.getItems(), bitmapUrl, Integer.valueOf(mSharing.getText().toString()), 0.08, 2.00);

            Bundle bundle = new Bundle();
            bundle.putParcelable("receipt", newReceipt);

            // Create an instance of the dialog fragment and show it
            DialogFragment dialog = new CalculateTotalDialogFragment();
            dialog.setArguments(bundle);
            dialog.show(getActivity().getSupportFragmentManager(), "CalculateTotalDialogFragment");
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private String convertBitmapToStringUrl(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            mCurrentPicture = (Bitmap) extras.get("data");
            mReceiptImage.setImageBitmap(mCurrentPicture);
        }
    }

    private void setUpButtons(){
        //add item fab listener
        mAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateItemDialog();
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
                showCalculateTotalDialog();
            }
        });
    }

    private boolean areInputsValid(){
        if(mAdapter == null) {
            Snackbar.make(getActivity().findViewById(R.id.coordinator), "Create some items before calculating total", Snackbar.LENGTH_LONG).show();
            return false;
        } else if (mSharing.getText().toString().equals("0")) {
            Snackbar.make(getActivity().findViewById(R.id.coordinator), "The number of people sharing must be one or more", Snackbar.LENGTH_LONG).show();
            return false;
        } else if (mSharing.getText().toString().isEmpty()) {
            Snackbar.make(getActivity().findViewById(R.id.coordinator), "Please provide a value for number of people sharing", Snackbar.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void receiveNewItemData(ReceiptItem createdItem) {
        if(mAdapter != null) {
            mAdapter.addItem(createdItem);
            mAdapter.notifyItemInserted(mAdapter.getItemCount() - 1);
        } else {
            ArrayList<ReceiptItem> newItemList = new ArrayList<>();
            newItemList.add(createdItem);
            mAdapter = new BillitemAdapter(newItemList, this);
            mItemList.setAdapter(mAdapter);
        }
    }

    @Override
    public void onBillItemClicked(int clickedItemIndex) {
        //TODO delete item when clicked
    }
}
