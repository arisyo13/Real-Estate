package com.example.dttrealestate.Utils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dttrealestate.Model.Property;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PropertyViewModel extends ViewModel {
    public MutableLiveData<List<Property>> propertiesMutableLiveData = new MutableLiveData<>();
    MutableLiveData<String> properties = new MutableLiveData<>();

    public void getProperties() {
        ParseJson.getINSTANCE().getProperties().enqueue(new Callback<List<Property>>() {
            @Override
            public void onResponse(Call<List<Property>> call, Response<List<Property>> response) {
                propertiesMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Property>> call, Throwable t) {
                properties.setValue("error");
            }
        });
    }
}
