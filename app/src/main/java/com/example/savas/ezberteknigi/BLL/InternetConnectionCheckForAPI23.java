package com.example.savas.ezberteknigi.BLL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class InternetConnectionCheckForAPI23 implements InternetConnectionCheckable{

    private Context context;

    public InternetConnectionCheckForAPI23(Context _context){
        context = _context;
    }

    @Override
    public boolean isConnectedToInternet() {
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
