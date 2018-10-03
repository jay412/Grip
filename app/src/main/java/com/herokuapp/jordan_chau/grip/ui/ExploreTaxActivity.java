package com.herokuapp.jordan_chau.grip.ui;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.herokuapp.jordan_chau.grip.BuildConfig;
import com.herokuapp.jordan_chau.grip.R;
import com.herokuapp.jordan_chau.grip.utilities.NetworkUtility;
import com.taxjar.Taxjar;
import com.taxjar.model.rates.RateResponse;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExploreTaxActivity extends AppCompatActivity {
    @BindView(R.id.tv_tax_rate) TextView mTaxRate;
    @BindView(R.id.tv_copy_tax_rate_instruction) TextView mInstructions;
    @BindView(R.id.et_tax_rate_input) EditText mInput;
    @BindView(R.id.explore_tax_coordinator) CoordinatorLayout mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_tax);
        ButterKnife.bind(this);
    }

    public void getTaxRate(View view) {
        String zipCode = mInput.getText().toString();

        new GetOperation().execute(BuildConfig.API_KEY, zipCode);
    }

    private class GetOperation extends AsyncTask<String, Void, String> {
        final ProgressDialog progressDialog = new ProgressDialog(ExploreTaxActivity.this);

        @Override
        protected void onPreExecute() {
            //checks for internet connection before proceeding
            if(!NetworkUtility.checkInternetConnection(ExploreTaxActivity.this)) {
                this.cancel(true);
                NetworkUtility.showErrorMessage(mLayout);
            }
            else {
                super.onPreExecute();

                progressDialog.setTitle(getString(R.string.please_wait));
                progressDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            String api_key = params[0];
            String zip_code = params[1];
            Taxjar client = new Taxjar(api_key);

            try {
                RateResponse res = client.ratesForLocation(zip_code);

                return Float.toString(res.rate.getCombinedRate());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String rate) {
            progressDialog.dismiss();

            if(rate != null) {
                mTaxRate.setText(rate);
                mTaxRate.setVisibility(View.VISIBLE);
                mInstructions.setVisibility(View.VISIBLE);
            } else {
                NetworkUtility.showErrorMessage(mLayout);
            }
        }
    }

    public void goBack(View v){
        finish();
    }
}
