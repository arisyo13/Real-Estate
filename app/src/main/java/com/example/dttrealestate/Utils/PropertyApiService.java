package com.example.dttrealestate.Utils;

import com.example.dttrealestate.Model.Property;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface PropertyApiService {
    @Headers("Access-Key: " + Constants.API_KEY)
    @GET(Constants.HOUSE_PROPERTY)

    public Call<List<Property>> getProperties();

}
