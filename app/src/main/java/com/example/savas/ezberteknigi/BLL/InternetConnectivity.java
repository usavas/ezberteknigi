package com.example.savas.ezberteknigi.BLL;

import com.example.savas.ezberteknigi.BLL.Interfaces.InternetConnectionCheckable;

import java.io.IOException;

public class InternetConnectivity implements InternetConnectionCheckable {

    @Override
    public boolean isConnectedToInternet() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }
}
