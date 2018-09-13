package com.herokuapp.jordan_chau.grip.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.herokuapp.jordan_chau.grip.R;
import com.herokuapp.jordan_chau.grip.ui.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsFragment extends Fragment {
    @BindView(R.id.btn_logout) Button mLogout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, rootView);

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut(getActivity());
            }
        });

        return rootView;
    }

    public void signOut(final Context c) {
        AuthUI.getInstance()
                .signOut(c)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(c, "You have been signed out.", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(c, LoginActivity.class));
                        getActivity().finish();
                    }
                });
    }
}
