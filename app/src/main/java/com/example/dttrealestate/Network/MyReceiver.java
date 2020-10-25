package com.example.dttrealestate.Network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.dttrealestate.Network.Network;
import com.example.dttrealestate.Utils.Constants;

public class MyReceiver extends BroadcastReceiver {
    public boolean status;

    @Override
    public void onReceive(Context context, Intent intent) {
        status = Network.getConnectivityStatusString(context);
        if(!status){
            Toast.makeText(context, Constants.NO_INTERNET, Toast.LENGTH_LONG).show();
        }

    }
}
