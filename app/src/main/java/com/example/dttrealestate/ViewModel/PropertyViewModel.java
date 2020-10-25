package com.example.dttrealestate.ViewModel;

import android.widget.Filter;
import android.widget.Filterable;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dttrealestate.Model.Property;
import com.example.dttrealestate.Utils.ParseJson;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PropertyViewModel extends ViewModel implements Filterable {
    public MutableLiveData<List<Property>> propertiesMutableLiveData = new MutableLiveData<>();
    private List<Property> propertiesFull = new ArrayList<>();


    public void loadProperties() {
        ParseJson.getINSTANCE().getProperties().enqueue(new Callback<List<Property>>() {
            @Override
            public void onResponse(Call<List<Property>> call, Response<List<Property>> response) {
                propertiesMutableLiveData.setValue(response.body());
                Collections.sort(response.body(), Property.propertyPrice);
                propertiesFull.addAll(response.body());


            }

            @Override
            public void onFailure(Call<List<Property>> call, Throwable t) {

            }
        });


    }

    @Override
    public Filter getFilter() {

        return filter;
    }

    public Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Property> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(propertiesFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Property item : propertiesFull) {
                    if (item.getZip().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            propertiesMutableLiveData.setValue((List)results.values);
            Collections.sort((List)results.values, Property.propertyPrice);

        }
    };


}
