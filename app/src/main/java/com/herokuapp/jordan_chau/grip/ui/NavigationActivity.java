package com.herokuapp.jordan_chau.grip.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.herokuapp.jordan_chau.grip.R;
import com.herokuapp.jordan_chau.grip.adapters.BottomNavBarAdapter;
import com.herokuapp.jordan_chau.grip.adapters.NoSwipePager;
import com.herokuapp.jordan_chau.grip.fragments.CalculateTotalDialogFragment;
import com.herokuapp.jordan_chau.grip.fragments.CreateNewItemDialogFragment;
import com.herokuapp.jordan_chau.grip.model.ReceiptItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class NavigationActivity extends AppCompatActivity implements CreateNewItemDialogFragment.CreateNewItemDialogListener, CalculateTotalDialogFragment.CalculateTotalDialogListener{
    @BindView(R.id.bottom_navigation) AHBottomNavigation bottomNavigation;
    @BindView(R.id.view_pager) NoSwipePager mViewPager;
    @BindView(R.id.tv_title) TextView mTitle;
    @BindView(R.id.coordinator) CoordinatorLayout mLayout;

    private BottomNavBarAdapter mPagerAdapter;
    private int mHistoryCount = 0;
    private String mCurrentTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        ButterKnife.bind(this);
        Timber.plant(new Timber.DebugTree());
        Timber.tag("NavigationActivity");

        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            //not signed in, launch sign in activity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {

                mViewPager.setPagingEnabled(false);
                mPagerAdapter = new BottomNavBarAdapter(getSupportFragmentManager());

                mViewPager.setAdapter(mPagerAdapter);

                mViewPager.setCurrentItem(1);
                mCurrentTitle = getResources().getString(R.string.title_new_bill);
                setUpBottomNavigationBar();
        }
    }

    private int fetchColor(@ColorRes int color) {
        return ContextCompat.getColor(this, color);
    }

    private void setUpBottomNavigationBar() {
        AHBottomNavigationItem mHistory = new AHBottomNavigationItem(R.string.title_history, R.drawable.ic_outline_history_24px, R.color.colorPrimary);
        AHBottomNavigationItem mNewBill = new AHBottomNavigationItem(R.string.title_new_bill, R.drawable.ic_outline_receipt_24px, R.color.colorPrimaryDark);
        AHBottomNavigationItem mSettings = new AHBottomNavigationItem(R.string.title_settings, R.drawable.ic_outline_settings_24px, R.color.colorAccent);

        bottomNavigation.addItem(mHistory);
        bottomNavigation.addItem(mNewBill);
        bottomNavigation.addItem(mSettings);

        //set background color
        bottomNavigation.setDefaultBackgroundColor(fetchColor(R.color.colorPrimaryDark));

        // Change colors
        bottomNavigation.setAccentColor(fetchColor(R.color.colorAccent));
        bottomNavigation.setInactiveColor(fetchColor(R.color.white));

        // Force to tint the drawable (useful for font with icon for example)
        bottomNavigation.setForceTint(true);

        //don't hide on scroll
        bottomNavigation.setBehaviorTranslationEnabled(false);

        // Set current item programmatically - set to New Bill tab
        bottomNavigation.setCurrentItem(1);

        // Set listeners
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                //switch fragments based on navigation tab selection
                if (!wasSelected) {
                    mViewPager.setCurrentItem(position);

                    switch(position) {
                        case 0:
                            mCurrentTitle = getResources().getString(R.string.title_history);
                            mTitle.setText(mCurrentTitle);
                            bottomNavigation.setNotification("", 0);
                            mHistoryCount = 0;
                            break;
                        case 1:
                            mCurrentTitle = getResources().getString(R.string.title_new_bill);
                            mTitle.setText(mCurrentTitle);
                            break;
                        case 2:
                            mCurrentTitle = getResources().getString(R.string.title_settings);
                            mTitle.setText(mCurrentTitle);
                            break;
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void onDialogPositiveClick(String quantity, String name, String price) {
        if (!quantity.equals("") && !name.equals("") && !price.equals("")) {
            ReceiptItem createdItem = new ReceiptItem(Integer.valueOf(quantity), name, Double.valueOf(price));

            CreateNewItemCallback callback = (CreateNewItemCallback) mPagerAdapter.getRegisteredFragment(1);
            callback.receiveNewItemData(createdItem);
        } else {
            Snackbar.make(mLayout, "Check fields and try again.", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void dialogMissingFields() {
        Snackbar.make(mLayout, "Please fill in the missing fields", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onCalculateTotalDialogPositiveClick() {
        // Add or remove notification for each item
        mHistoryCount++;
        bottomNavigation.setNotification(Integer.toString(mHistoryCount), 0);
    }

    public interface CreateNewItemCallback {
        void receiveNewItemData(ReceiptItem createdItem);
        //public void onDialogNegativeClick(DialogFragment dialog);
    }
}
