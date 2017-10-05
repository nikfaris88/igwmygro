package com.ingeniworks.mykomunitimardi.utils;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.NetworkOnMainThreadException;

import java.net.SocketException;
import java.net.SocketTimeoutException;

public class NetworkCheck {

    private Context context;

    public NetworkCheck(Context context){
        this.context = context;
    }

    public boolean isNetworkConnected() throws NetworkOnMainThreadException, SocketTimeoutException,
            SocketException, NetworkErrorException {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            return (netInfo != null && netInfo.isConnected());
        }
    }

}