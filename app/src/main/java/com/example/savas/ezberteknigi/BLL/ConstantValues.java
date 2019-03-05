package com.example.savas.ezberteknigi.BLL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public final class ConstantValues {

    private static ConstantValues instance;
    private static String MODE = "";


    private ConstantValues() {
    }

    public static ConstantValues getInstance(){
        if (instance == null){
            instance = new ConstantValues();
        }
        return instance;
    }


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
