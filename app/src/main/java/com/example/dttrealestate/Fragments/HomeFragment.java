package com.example.dttrealestate.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.dttrealestate.Model.Property;
import com.example.dttrealestate.R;
import com.example.dttrealestate.Utils.PropertyViewModel;
import com.example.dttrealestate.Utils.RecyclerViewAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import java.util.Collections;
import java.util.List;


public class HomeFragment extends Fragment {

    //Widgets
    private RecyclerView recyclerView;
    private SearchView searchView;
    private ImageView searchStateEmptyImage;
    private TextView noResultsTextView, anotherSeachTextView;

    //ViewModel
    PropertyViewModel propertyViewModel;

    FusedLocationProviderClient fusedLocationProviderClient;
    private LatLng myLocation = new LatLng(0, 0); //Assign empty values for userLocation before he grants location permissionn

    //variables
    private double latitude, longitude;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());


        //sets the ViewModel into homeFragment
        propertyViewModel = new ViewModelProvider(this).get(PropertyViewModel.class);
        propertyViewModel.getProperties();  //Retrieves all properties from ViewModel


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        searchView = view.findViewById(R.id.searchView);
        recyclerView = view.findViewById(R.id.recyclerViewLayout);
        searchStateEmptyImage = view.findViewById(R.id.searchStateEmptyImage);
        noResultsTextView = view.findViewById(R.id.noResultsTextView);
        anotherSeachTextView = view.findViewById(R.id.anotherSearchTextView);

        final RecyclerViewAdapter adapter = new RecyclerViewAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);


        //Loading data from viewmodel to a list of properties
        propertyViewModel.propertiesMutableLiveData.observe(getViewLifecycleOwner(), new Observer<List<Property>>() {
            @Override
            public void onChanged(List<Property> properties) {

                adapter.setProperties(properties, myLocation); //Passes loaded list from view model and user location to recyclerview adapter
                recyclerView.setAdapter(adapter);  //Passes the recyclerview adapter to recyclerview widget
                Collections.sort(properties, Property.propertyPrice);  //sorts the list of properties by their price from smaller to bigger

            }

        });


        //Removes the search button from keyboard
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        //When user types into searchview
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                //When submitting the search it checks if has no results and brings a no results message
                if(adapter.getItemCount()== 0){
                    searchStateEmptyImage.setVisibility(View.VISIBLE);
                    noResultsTextView.setVisibility(View.VISIBLE);
                    anotherSeachTextView.setVisibility(View.VISIBLE);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                //When typing into search bar the recyclerview adapter gets called with the typed text without pressing enter and filters the list
                adapter.getFilter().filter(s);


                //When typing the search it checks if has results and hides the message
                if(adapter.getItemCount()== 0) {
                    searchStateEmptyImage.setVisibility(View.INVISIBLE);
                    noResultsTextView.setVisibility(View.INVISIBLE);
                    anotherSeachTextView.setVisibility(View.INVISIBLE);
                }

                return false;
            }


        });

        getLocation();



        return view;
    }


    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {

                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    myLocation = new LatLng(latitude, longitude);

                }

            }
        });
    }


}