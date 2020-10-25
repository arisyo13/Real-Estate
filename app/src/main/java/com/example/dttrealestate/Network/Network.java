package com.example.dttrealestate.Network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class Network {

    public static boolean getConnectivityStatusString(Context context) {

        boolean status;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            status = true;
        }else{
            status = false;
        }
        return status;

    }

}
