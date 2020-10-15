package com.example.dttrealestate.Utils;

import com.example.dttrealestate.Model.Property;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface PropertyInterface {
    @Headers("Access-Key: 98bww4ezuzfePCYFxJEWyszbUXc7dxRx")
    @GET("house")
    public Call<List<Property>> getProperties();


}
