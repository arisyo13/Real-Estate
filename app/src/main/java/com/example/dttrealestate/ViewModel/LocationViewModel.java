package com.example.dttrealestate.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;

public class LocationViewModel extends ViewModel {

    public MutableLiveData<LatLng> userLocation = new MutableLiveData<>();


    public void setLocation(LatLng latLng){
        userLocation.setValue(latLng);

    }


}
