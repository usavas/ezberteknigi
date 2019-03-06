package com.example.savas.ezberteknigi.BLL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;

public final class ConstantValues   {

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



}
