package com.herokuapp.jordan_chau.grip.widget;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.herokuapp.jordan_chau.grip.R;
import com.herokuapp.jordan_chau.grip.fragments.CalculateTotalDialogFragment;
import com.herokuapp.jordan_chau.grip.model.Receipt;

public class SendEmailIntentService extends IntentService{
    public static final String ACTION_SEND_EMAIL = "com.herokuapp.jordan_chau.grip.send_email";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public SendEmailIntentService(String name) {
        super(name);
    }

    public static void startActionSendEmail(Context context) {
        Intent intent = new Intent(context, SendEmailIntentService.class);
        intent.setAction(ACTION_SEND_EMAIL);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null) {
            if(intent.getExtras() != null) {
               Receipt emailCopy = intent.getParcelableExtra("receipt");

                final String action = intent.getAction();
                if(ACTION_SEND_EMAIL.equals(action)) {
                    handleActionSendEmail(emailCopy);
                }
            } else {
                //catch the error
            }
        }
    }

    private void handleActionSendEmail(Receipt receipt){
        final String receiptDetails = receipt.toString();
        startActivity(Intent.createChooser(CalculateTotalDialogFragment.setUpEmailIntent(receiptDetails), getResources().getString(R.string.send_email)));
    }
}
