package com.example.dttrealestate.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

public class Permissions {

    private static final int VERIFY_PERMISSION_REQUEST = 1;
    public static final String[] PERMISSIONS ={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    public static final String[] ACCESS_FINE_LOCATION = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    public static final String[] ACCESS_COARSE_LOCATION = {
            Manifest.permission.ACCESS_COARSE_LOCATION
    };


    public void verifyPermissions(Activity activity, String[] permissions){
        ActivityCompat.requestPermissions(activity, permissions, VERIFY_PERMISSION_REQUEST);

    }

    public boolean checkPermissionsArray(Activity activity, String[] permissions){
        for (int i=0; i<permissions.length; i++){
            String check = permissions[i];
            if(!checkPermissions(activity, check)){
                return false;
            }
        }
        return true;
    }

    public boolean checkPermissions(Activity activity, String permission){
        int permissionRequest = ActivityCompat.checkSelfPermission(activity, permission);
        if(permissionRequest != PackageManager.PERMISSION_GRANTED){
            return false;
        }
        else{
            return true;
        }
    }
}
