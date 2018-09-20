package com.herokuapp.jordan_chau.grip.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.herokuapp.jordan_chau.grip.R;
import com.herokuapp.jordan_chau.grip.ui.LoginActivity;
import com.jaredrummler.android.device.DeviceName;

public class SettingsFragment extends PreferenceFragmentCompat {
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Load the preferences from an XML resource
        setPreferencesFromResource(R.xml.preferences, rootKey);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        //set up tax and tip default summaries
        setDeviceIdSummary(getString(R.string.tax_preference_key), getString(R.string.default_tax_rate));
        setDeviceIdSummary(getString(R.string.tip_list_preference_key), getString(R.string.default_tip));

        setDevicePreferenceSummary();
        //set up preference click listeners
        setUpPreferenceClickListeners();
    }

    public void setDeviceIdSummary(final String deviceIdKey, String deviceIdDefault) {
        String deviceIdValue = sharedPreferences.getString(deviceIdKey, deviceIdDefault);

        final Preference preference = findPreference(deviceIdKey);
        preference.setSummary(deviceIdValue);

        preference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                String newValue = o.toString();
                sharedPreferences.edit().putString(deviceIdKey, newValue).apply();
                preference.setSummary(newValue);
                return true;
            }
        });
    }

    public void setDevicePreferenceSummary(){
        Preference devicePreference = findPreference(getString(R.string.device_stylish_preference_key));

        String deviceName = DeviceName.getDeviceName();
        devicePreference.setSummary(deviceName);
    }

    private void setUpPreferenceClickListeners() {
        Preference logoutPreference = findPreference("logout_preference");
        logoutPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                signOut();
                return true;
            }
        });
    }

    public void signOut() {
        AuthUI.getInstance()
                .signOut(getActivity())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                    }
                });
    }
}
