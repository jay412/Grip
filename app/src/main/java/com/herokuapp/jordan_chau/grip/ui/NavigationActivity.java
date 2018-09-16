package com.herokuapp.jordan_chau.grip.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
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
import com.herokuapp.jordan_chau.grip.fragments.HistoryFragment;
import com.herokuapp.jordan_chau.grip.fragments.NewBillFragment;
import com.herokuapp.jordan_chau.grip.fragments.SettingsFragment;
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
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        ButterKnife.bind(this);
        Timber.plant(new Timber.DebugTree());
        Timber.tag("NavigationActivity");

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            //not signed in, launch sign in activity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {

        mViewPager.setPagingEnabled(false);
        mPagerAdapter = new BottomNavBarAdapter(getSupportFragmentManager());

        HistoryFragment historyFragment = new HistoryFragment();
        NewBillFragment newBillFragment = new NewBillFragment();
        SettingsFragment settingsFragment = new SettingsFragment();

        mPagerAdapter.addFragments(historyFragment);
        mPagerAdapter.addFragments(newBillFragment);
        mPagerAdapter.addFragments(settingsFragment);

        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(1);

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

        //TODO explore this later
        // Display color under navigation bar (API 21+)
        // Don't forget these lines in your style-v21
        // <item name="android:windowTranslucentNavigation">true</item>
        // <item name="android:fitsSystemWindows">true</item>
        //bottomNavigation.setTranslucentNavigationEnabled(true);

        // Manage titles
        //bottomNavigation.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);
        //bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        //bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);

        // Set current item programmatically - set to New Bill tab
        bottomNavigation.setCurrentItem(1);

        // Customize notification (title, background, typeface)
        //bottomNavigation.setNotificationBackgroundColor(fetchColor(R.color.color_notification));

        // Add or remove notification for each item
        //bottomNavigation.setNotification("1", 2);

        // OR
        /*AHNotification notification = new AHNotification.Builder()
                .setText("1")
                .setBackgroundColor(ContextCompat.getColor(DemoActivity.this, R.color.color_notification_back))
                .setTextColor(ContextCompat.getColor(DemoActivity.this, R.color.color_notification_text))
                .build();
        bottomNavigation.setNotification(notification, 1);
        */

        // Set listeners
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                //switch fragments based on navigation tab selection
                if (!wasSelected) {
                    mViewPager.setCurrentItem(position);

                    switch(position) {
                        case 0:
                            mTitle.setText(getResources().getString(R.string.title_history));
                            break;
                        case 1:
                            mTitle.setText(getResources().getString(R.string.title_new_bill));
                            break;
                        case 2:
                            mTitle.setText(getResources().getString(R.string.title_settings));
                            break;
                    }
                }
                return true;
            }
        });
        //TODO explore this later
        /* bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override public void onPositionChange(int y) {
                // Manage the new y position
            }
        }); */
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String quantity, String name, String price) {
        if (!quantity.equals("") && !name.equals("") && !price.equals("")) {
            ReceiptItem createdItem = new ReceiptItem(Integer.valueOf(quantity), name, Double.valueOf(price));
            //Snackbar.make(mLayout, "Quantity is: " + createdItem.getQuantity() + " Name is: " + createdItem.getName() + " Price is: " + createdItem.getPrice(), Snackbar.LENGTH_SHORT).show();

            CreateNewItemCallback callback = (CreateNewItemCallback) mPagerAdapter.getItem(1);
            callback.receiveNewItemData(createdItem);
        } else {
            Snackbar.make(mLayout, "Check fields and try again.", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCalculateTotalDialogPositiveClick(DialogFragment dialog, String quantity, String name, String price) {

    }

    public interface CreateNewItemCallback {
        void receiveNewItemData(ReceiptItem createdItem);
        //public void onDialogNegativeClick(DialogFragment dialog);
    }
}
