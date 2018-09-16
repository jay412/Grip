package com.herokuapp.jordan_chau.grip.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.herokuapp.jordan_chau.grip.R;
import com.herokuapp.jordan_chau.grip.model.Receipt;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class CalculateTotalDialogFragment extends DialogFragment {
    @BindView(R.id.tv_item_subtotal) TextView mSubtotal;
    @BindView(R.id.tv_item_tax) TextView mTax;
    @BindView(R.id.tv_item_tip) TextView mTip;
    @BindView(R.id.tv_item_grand_total) TextView mGrandTotal;
    @BindView(R.id.tv_item_person_pay) TextView mPersonPay;

    // Use this instance of the interface to deliver action events
    private CalculateTotalDialogListener mListener;

    private DatabaseReference mReceiptRef;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (CalculateTotalDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.dialogTheme);
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View rootView = inflater.inflate(R.layout.dialog_calculate_total, null);
        ButterKnife.bind(this, rootView);

        Bundle bundle = getArguments();
        if(bundle == null) {
            Timber.d("bundle is null");
            return null;
        } else {
            final Receipt receipt = bundle.getParcelable("receipt");

            mSubtotal.setText(new StringBuilder().append("$").append(Receipt.roundToMoneyFormat(receipt.getSubTotal())).toString());
            mTax.setText(new StringBuilder().append("$").append(Receipt.roundToMoneyFormat(receipt.getTax())).toString());
            mTip.setText(new StringBuilder().append("$").append(Receipt.roundToMoneyFormat(receipt.getTip())).toString());
            mGrandTotal.setText(new StringBuilder().append("$").append(Receipt.roundToMoneyFormat(receipt.getGrandTotal())).toString());
            mPersonPay.setText(new StringBuilder().append("$").append(Receipt.roundToMoneyFormat(receipt.getPersonPay())).toString());


            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(rootView)
                    // Add action buttons
                    .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            //String quantity = mQuantity.getText().toString();
                            //String name = mName.getText().toString();
                            //String price = mPrice.getText().toString();

                            //mListener.onDialogPositiveClick(CalculateTotalDialogFragment.this, quantity, name, price);

                            mReceiptRef = FirebaseDatabase.getInstance()
                                    .getReference()
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("receipts");
                            mReceiptRef.push().setValue(receipt);

                            //TODO update history tab to have 1 notification
                            Snackbar.make(getActivity().findViewById(R.id.coordinator), "Saved!", Snackbar.LENGTH_SHORT).show();
                        }
                    })
                    .setNeutralButton(R.string.email, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            CalculateTotalDialogFragment.this.getDialog().cancel();
                        }
                    });
            return builder.create();
        }
    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it.
     *
     * This works*/
    public interface CalculateTotalDialogListener {
        void onCalculateTotalDialogPositiveClick(DialogFragment dialog, String quantity, String name, String price);
        //public void onDialogNegativeClick(DialogFragment dialog);
    }
}
