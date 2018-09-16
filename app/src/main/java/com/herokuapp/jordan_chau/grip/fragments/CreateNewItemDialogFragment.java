package com.herokuapp.jordan_chau.grip.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.herokuapp.jordan_chau.grip.R;
import com.herokuapp.jordan_chau.grip.model.Receipt;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateNewItemDialogFragment extends DialogFragment {
    @BindView(R.id.et_add_quantity) EditText mQuantity;
    @BindView(R.id.et_add_name) EditText mName;
    @BindView(R.id.et_add_price) EditText mPrice;

    // Use this instance of the interface to deliver action events
    private CreateNewItemDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (CreateNewItemDialogListener) context;
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

        View rootView = inflater.inflate(R.layout.dialog_add_item, null);
        ButterKnife.bind(this, rootView);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(rootView)
                // Add action buttons
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String quantity = mQuantity.getText().toString();
                        String name = mName.getText().toString();
                        String price = Receipt.roundToMoneyFormat(Double.valueOf(mPrice.getText().toString()));

                        mListener.onDialogPositiveClick(CreateNewItemDialogFragment.this, quantity, name, price);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CreateNewItemDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it.
     *
     * This works*/
    public interface CreateNewItemDialogListener {
        void onDialogPositiveClick(DialogFragment dialog, String quantity, String name, String price);
        //public void onDialogNegativeClick(DialogFragment dialog);
    }
}
