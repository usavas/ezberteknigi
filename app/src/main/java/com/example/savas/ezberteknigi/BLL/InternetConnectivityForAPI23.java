package com.example.savas.ezberteknigi.BLL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;

public class InternetConnectivityForAPI23 implements InternetConnectivityChekable {

    public boolean checkNetworkConnection(Context context){
        boolean isConnected = false;

        //TODO: requires API 23 or higher
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }

        return isConnected;
    }
}
