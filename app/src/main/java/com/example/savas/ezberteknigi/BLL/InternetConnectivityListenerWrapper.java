package com.example.savas.ezberteknigi.BLL;

import android.content.Context;

import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;

public class InternetConnectivityListenerWrapper implements InternetConnectivityChekable, InternetConnectivityListener {

    private boolean _isConnected = false;

    @Override
    public boolean checkNetworkConnection(Context context) {
        InternetAvailabilityChecker.init(context);
        return  _isConnected;
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        _isConnected = isConnected;
    }
}
