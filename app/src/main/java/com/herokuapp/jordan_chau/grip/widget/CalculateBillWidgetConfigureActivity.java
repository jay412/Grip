package com.herokuapp.jordan_chau.grip.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.herokuapp.jordan_chau.grip.R;
import com.herokuapp.jordan_chau.grip.adapters.BillitemAdapter;
import com.herokuapp.jordan_chau.grip.model.Receipt;
import com.herokuapp.jordan_chau.grip.model.ReceiptItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * The configuration screen for the {@link CalculateBillWidget CalculateBillWidget} AppWidget.
 */
public class CalculateBillWidgetConfigureActivity extends Activity implements BillitemAdapter.BillItemClickListener{
    @BindView(R.id.et_widget_tax) EditText mTax;
    @BindView(R.id.radio_group) RadioGroup mRadioGroup;
    @BindView(R.id.add_button) Button mAdd;
    @BindView(R.id.rv_widget_item_list) RecyclerView mItemList;
    @BindView(R.id.fab_widget_add_new_item) FloatingActionButton mAddItem;
    @BindView(R.id.et_widget_add_quantity) EditText mQuantity;
    @BindView(R.id.et_widget_add_price) EditText mPrice;
    @BindView(R.id.et_widget_sharing) EditText mSharing;
    private RadioButton mRadioButton;
    private BillitemAdapter mAdapter;

    private static final String PREFS_NAME = "com.herokuapp.jordan_chau.grip.widget.CalculateBillWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private static final String SUF_TAX_KEY = "tax";
    private static final String SUF_TIP_KEY = "tip";

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = CalculateBillWidgetConfigureActivity.this;

            // When the button is clicked, store the tax and tip
            String tax = mTax.getText().toString();

            int selectedRadioButtonId = mRadioGroup.getCheckedRadioButtonId();
            mRadioButton = findViewById(selectedRadioButtonId);
            String tip = mRadioButton.getText().toString();

            if (mTax.getText().toString().isEmpty()) {
                Toast.makeText(CalculateBillWidgetConfigureActivity.this, R.string.tax_rate_empty, Toast.LENGTH_LONG).show();
            } else if (tip.isEmpty()) {
                Toast.makeText(CalculateBillWidgetConfigureActivity.this, R.string.tip_rate_empty, Toast.LENGTH_LONG).show();
            } else if (mAdapter.getItemCount() == 0) {
                Toast.makeText(CalculateBillWidgetConfigureActivity.this, getString(R.string.create_item_error), Toast.LENGTH_LONG).show();
            } else if (mSharing.getText().toString().isEmpty()) {
                Toast.makeText(CalculateBillWidgetConfigureActivity.this, getString(R.string.people_sharing_empty), Toast.LENGTH_LONG).show();
            } else if (mSharing.getText().toString().equals("0")) {
                Toast.makeText(CalculateBillWidgetConfigureActivity.this, getString(R.string.people_sharing_valid_input), Toast.LENGTH_LONG).show();
            } else {

                Receipt newReceipt = new Receipt("", mAdapter.getItems(), "", Integer.valueOf(mSharing.getText().toString()), Double.valueOf(tax), Double.valueOf(tip));

                //TODO check if bug when no radio button selected, no items, no tax rate, no people sharing
                //save tax and tip rates
                savePrefs(context, mAppWidgetId, newReceipt);

                // It is the responsibility of the configuration activity to update the app widget
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                CalculateBillWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

                // Make sure we pass back the original appWidgetId
                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                setResult(RESULT_OK, resultValue);
                finish();
            }
        }
    };

    public CalculateBillWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void savePrefs(Context context, int appWidgetId, Receipt receipt) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId + SUF_TAX_KEY, Receipt.roundToMoneyFormat(receipt.getTax()));
        prefs.putString(PREF_PREFIX_KEY + appWidgetId + SUF_TIP_KEY, Receipt.roundToMoneyFormat(receipt.getTip()));
        prefs.putString("subtotal", Receipt.roundToMoneyFormat(receipt.getSubTotal()));
        prefs.putString("grandtotal", Receipt.roundToMoneyFormat(receipt.getGrandTotal()));
        prefs.putString("peoplePay", Receipt.roundToMoneyFormat(receipt.getPersonPay()));
        prefs.apply();
    }

    static String loadSubTotalPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String subtotal = prefs.getString("subtotal", null);
        if (subtotal != null) {
            return subtotal;
        } else {
            return "0";
        }
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTaxPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String tax = prefs.getString(PREF_PREFIX_KEY + appWidgetId + SUF_TAX_KEY, null);
        if (tax != null) {
            return tax;
        } else {
            return "0.08875";
        }
    }

    static String loadTipPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String tip = prefs.getString(PREF_PREFIX_KEY + appWidgetId + SUF_TIP_KEY, null);
        if (tip != null) {
            return tip;
        } else {
            return "0.15";
        }
    }

    static String loadGrandTotalPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String grandtotal = prefs.getString("grandtotal", null);
        if (grandtotal != null) {
            return grandtotal;
        } else {
            return "0";
        }
    }

    static String loadPersonPayPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String personPay = prefs.getString("peoplePay", null);
        if (personPay != null) {
            return personPay;
        } else {
            return "0";
        }
    }

    static void deleteTaxTipPref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId + SUF_TAX_KEY);
        prefs.remove(PREF_PREFIX_KEY + appWidgetId + SUF_TIP_KEY);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.calculate_bill_widget_configure);
        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);
        ButterKnife.bind(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mItemList.setLayoutManager(layoutManager);
        mItemList.setHasFixedSize(true);

        mAdapter = new BillitemAdapter(new ArrayList<ReceiptItem>(), this);
        mItemList.setAdapter(mAdapter);

        mAdd.setOnClickListener(mOnClickListener);
        setUpFabButton();

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        mTax.setText(loadTaxPref(CalculateBillWidgetConfigureActivity.this, mAppWidgetId));
    }

    private void setUpFabButton(){
        mAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mQuantity.getText().toString().isEmpty() || mQuantity.getText().toString().equals("") || mPrice.getText().toString().isEmpty() || mPrice.getText().toString().equals("")) {
                    Toast.makeText(CalculateBillWidgetConfigureActivity.this, getResources().getString(R.string.missing_fields), Toast.LENGTH_SHORT).show();
                } else {
                    ReceiptItem newItem = new ReceiptItem(Integer.valueOf(mQuantity.getText().toString()), "", Integer.valueOf(mPrice.getText().toString()));
                    mAdapter.addItem(newItem);
                    mAdapter.notifyItemInserted(mAdapter.getItemCount() - 1);
                }
            }
        });
    }

    @Override
    public void onBillItemClicked(int clickedItemIndex) {
        mAdapter.removeItem(clickedItemIndex);
        mAdapter.notifyItemRemoved(clickedItemIndex);
    }
}

