package com.example.dttrealestate.Utils;

import com.example.dttrealestate.Model.Property;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ParseJson {
    private static final String BASE_URL = "https://intern.docker-dev.d-tt.nl/api/";
    private PropertyInterface propertyInterface;
    private static ParseJson INSTANCE;

    public ParseJson() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        propertyInterface = retrofit.create(PropertyInterface.class);
    }

    public static ParseJson getINSTANCE() {
        if (null == INSTANCE){
            INSTANCE = new ParseJson();
        }
        return INSTANCE;
    }

    public Call<List<Property>> getProperties(){
        return propertyInterface.getProperties();
    }
}
