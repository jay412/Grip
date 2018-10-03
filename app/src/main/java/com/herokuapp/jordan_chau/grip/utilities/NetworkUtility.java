package com.herokuapp.jordan_chau.grip.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.herokuapp.jordan_chau.grip.R;

public class NetworkUtility {

    public static Boolean checkInternetConnection(Context context){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static void showErrorMessage(View v) {
        Snackbar snackbar = Snackbar.make(v, R.string.connection_error, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
