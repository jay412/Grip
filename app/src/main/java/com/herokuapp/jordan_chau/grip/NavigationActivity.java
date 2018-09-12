package com.herokuapp.jordan_chau.grip;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NavigationActivity extends AppCompatActivity {
    @BindView(R.id.bottom_navigation) AHBottomNavigation bottomNavigation;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUsername;

    private TextView mTextMessage;

   /* private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    }; */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        ButterKnife.bind(this);

        //mTextMessage = findViewById(R.id.message);
        //BottomNavigationView navigation = findViewById(R.id.navigation);
        //navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

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
        bottomNavigation.setDefaultBackgroundColor(fetchColor(R.color.colorAccent));

        // Disable the translation inside the CoordinatorLayout
        //bottomNavigation.setBehaviorTranslationEnabled(false);

        // Enable the translation of the FloatingActionButton
        //bottomNavigation.manageFloatingActionButtonBehavior(floatingActionButton);

        // Change colors
        bottomNavigation.setAccentColor(fetchColor(R.color.colorPrimary));
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
                // Do something cool here...
                return true;
            }
        });
        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override public void onPositionChange(int y) {
                // Manage the new y position
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sign_out, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void signOut(final Context c) {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(c, "You have been signed out.", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(c, LoginActivity.class));
                        finish();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_sign_out:
                signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
