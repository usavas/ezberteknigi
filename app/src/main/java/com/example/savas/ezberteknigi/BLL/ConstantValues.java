package com.example.savas.ezberteknigi.BLL;

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
}
