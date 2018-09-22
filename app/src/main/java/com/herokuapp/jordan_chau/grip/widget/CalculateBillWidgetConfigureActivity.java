package com.herokuapp.jordan_chau.grip.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.herokuapp.jordan_chau.grip.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * The configuration screen for the {@link CalculateBillWidget CalculateBillWidget} AppWidget.
 */
public class CalculateBillWidgetConfigureActivity extends Activity {

    private static final String PREFS_NAME = "com.herokuapp.jordan_chau.grip.widget.CalculateBillWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private static final String SUF_TAX_KEY = "tax";
    private static final String SUF_TIP_KEY = "tip";

    @BindView(R.id.et_widget_tax) EditText mTax;
    @BindView(R.id.radio_group) RadioGroup mRadioGroup;
    @BindView(R.id.add_button) Button mAdd;
    private RadioButton mRadioButton;

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = CalculateBillWidgetConfigureActivity.this;

            // When the button is clicked, store the tax and tip
            String tax = mTax.getText().toString();

            int selectedRadioButtonId = mRadioGroup.getCheckedRadioButtonId();
            mRadioButton = findViewById(selectedRadioButtonId);
            String tip = mRadioButton.getText().toString();

            //TODO check if bug when no radio button selected
            //save tax and tip rates
            saveTaxTipPref(context, mAppWidgetId, tax, tip);

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            CalculateBillWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    public CalculateBillWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTaxTipPref(Context context, int appWidgetId, String tax, String tip) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId + SUF_TAX_KEY, tax);
        prefs.putString(PREF_PREFIX_KEY + appWidgetId + SUF_TIP_KEY, tip);
        prefs.apply();
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

        mAdd.setOnClickListener(mOnClickListener);

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
}

