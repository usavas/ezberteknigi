package com.example.savas.ezberteknigi.BLL;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

public class InternetConnectivitySocket implements InternetConnectionCheckable {

    @Override
    public boolean isConnectedToInternet() {
        try {
            return new InternetCheck(hasInternetAccess -> {  }).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static class InternetCheck extends AsyncTask<Void,Void,Boolean> {

        private Consumer mConsumer;
        public interface Consumer { void accept(Boolean internet); }

        public InternetCheck(Consumer consumer) {
            mConsumer = consumer;
            execute();
        }

        @Override protected Boolean doInBackground(Void... voids) { try {
            Socket sock = new Socket();
            sock.connect(new InetSocketAddress("8.8.8.8", 53), 1500);
            sock.close();
            return true;
        } catch (IOException e) { return false; } }

        @Override protected void onPostExecute(Boolean internet) { mConsumer.accept(internet); }
    }
}
