package com.herokuapp.jordan_chau.grip.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.herokuapp.jordan_chau.grip.adapters.BottomNavBarAdapter;
import com.herokuapp.jordan_chau.grip.adapters.NoSwipePager;
import com.herokuapp.jordan_chau.grip.R;
import com.herokuapp.jordan_chau.grip.fragments.CreateNewItemDialogFragment;
import com.herokuapp.jordan_chau.grip.fragments.HistoryFragment;
import com.herokuapp.jordan_chau.grip.fragments.NewBillFragment;
import com.herokuapp.jordan_chau.grip.fragments.SettingsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class NavigationActivity extends AppCompatActivity implements CreateNewItemDialogFragment.CreateNewItemDialogListener{
    @BindView(R.id.bottom_navigation) AHBottomNavigation bottomNavigation;
    @BindView(R.id.view_pager) NoSwipePager mViewPager;
    @BindView(R.id.tv_title) TextView mTitle;
    private BottomNavBarAdapter mPagerAdapter;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUsername;

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
            mUsername = mFirebaseUser.getDisplayName();
            //mTextMessage.setText("Hello " + mUsername);
        }

        //TODO load default new bill page after log in
        mViewPager.setPagingEnabled(false);
        mPagerAdapter = new BottomNavBarAdapter(getSupportFragmentManager());

        HistoryFragment historyFragment = new HistoryFragment();
        NewBillFragment newBillFragment = new NewBillFragment();
        SettingsFragment settingsFragment = new SettingsFragment();
        //fragment.setArguments(bundle);
        mPagerAdapter.addFragments(historyFragment);
        mPagerAdapter.addFragments(newBillFragment);
        mPagerAdapter.addFragments(settingsFragment);

        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(1);

        setUpBottomNavigationBar();
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

        // Disable the translation inside the CoordinatorLayout
        //bottomNavigation.setBehaviorTranslationEnabled(false);

        // Enable the translation of the FloatingActionButton
        //bottomNavigation.manageFloatingActionButtonBehavior(floatingActionButton);

        // Change colors
        bottomNavigation.setAccentColor(fetchColor(R.color.colorAccent));
        bottomNavigation.setInactiveColor(fetchColor(R.color.white));

        // Force to tint the drawable (useful for font with icon for example)
        bottomNavigation.setForceTint(true);

        // Display color under navigation bar (API 21+)
        // Don't forget these lines in your style-v21
        // <item name="android:windowTranslucentNavigation">true</item>
        // <item name="android:fitsSystemWindows">true</item>
        //bottomNavigation.setTranslucentNavigationEnabled(true);

        // Manage titles
        //bottomNavigation.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);
        //bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        //bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);

        // Use colored navigation with circle reveal effect
        //bottomNavigation.setColored(true);

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
        // Enable / disable item & set disable color
        //bottomNavigation.enableItemAtPosition(2);
        //bottomNavigation.disableItemAtPosition(2);
        //bottomNavigation.setItemDisableColor(Color.parseColor("#3A000000"));

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
        /* bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override public void onPositionChange(int y) {
                // Manage the new y position
            }
        }); */
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String quantity, String name, String price) {
        //TODO make recyclerview adapter for new bill page, add and set up adapter & RV, create receipt item here and add to RV
        //TODO send data from dialog click to fragment and refresh fragment
        Toast.makeText(this, "Added Item!", Toast.LENGTH_LONG).show();
    }
}
