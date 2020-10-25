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

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dttrealestate.Model.Property;
import com.example.dttrealestate.R;
import com.example.dttrealestate.Utils.Permissions;
import com.example.dttrealestate.ViewModel.LocationViewModel;
import com.example.dttrealestate.ViewModel.PropertyViewModel;
import com.example.dttrealestate.Adapter.RecyclerViewAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import java.util.List;


public class HomeFragment extends Fragment {

    //Widgets
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private EditText search;
    private ImageView noResultsImage, searchIcon;
    private TextView noResultsText1, noResultsText2;

    //ViewModel
    private PropertyViewModel propertyViewModel;
    private LocationViewModel locationViewModel;

    private FusedLocationProviderClient fusedLocationProviderClient;

    //Assign empty values for userLocation before he grants location permission
    private LatLng myLocation = new LatLng(0, 0);

    //variables
    private double latitude, longitude;
    private boolean emptyList = true;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        adapter = new RecyclerViewAdapter();


        /**
         * PropertyViewModel and LocationViewModel classes can now be accessed
         * from HomeFragment class and communicate between.
         * So now we tell the PropertyViewModel to start loading data from
         * Api Service and store them into MutableLiveData
         */
        propertyViewModel = new ViewModelProvider(this).get(PropertyViewModel.class);
        propertyViewModel.loadProperties();

        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);


    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        search = view.findViewById(R.id.searchText);
        recyclerView = view.findViewById(R.id.recyclerViewLayout);
        noResultsImage = view.findViewById(R.id.noResultsImage);
        noResultsText1 = view.findViewById(R.id.noResultsText1);
        noResultsText2 = view.findViewById(R.id.noResultsText2);
        searchIcon = view.findViewById(R.id.searchIcon);


        adapter = new RecyclerViewAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        getPermissions();


        searchIcon.setTag(R.drawable.ic_search);
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(searchIcon.getTag().equals(R.drawable.ic_close)){
                    search.setText("");
                }
            }
        });


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(!emptyList){
                    propertyViewModel.getFilter().filter(s);
                }

                if(search.getText().length()==0){
                    searchIcon.setImageResource(R.drawable.ic_search);
                    searchIcon.setTag(R.drawable.ic_search);

                }else{
                    searchIcon.setImageResource(R.drawable.ic_close);
                    searchIcon.setTag(R.drawable.ic_close);
                }

            }
        });


        /**
         * With this method when location is added in LocationViewModel
         * then it calls for getProperties methods to retrieve them into recyclerView
         */

        locationViewModel.userLocation.observe(getViewLifecycleOwner(), new Observer<LatLng>() {
            @Override
            public void onChanged(LatLng latLng) {

                getProperties();
            }
        });



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

                    locationViewModel.setLocation(myLocation);

                }

            }
        });



    }

    /**
     * This method observes the properties list from PropertyViewModel and puts them into recyclerView.
     * Every time a search is performed then new list get observed and re attached into recycler view.
     * If the list is empty then brings No results message to the screen.
     * Also
     */

    private void getProperties(){

        propertyViewModel.propertiesMutableLiveData.observe(getViewLifecycleOwner(), new Observer<List<Property>>() {
            @Override
            public void onChanged(List<Property> properties) {
                if(!properties.isEmpty()){
                    emptyList = false;

                }
                if(propertyViewModel.propertiesMutableLiveData.getValue().isEmpty()){
                    noResultsImage.setVisibility(View.VISIBLE);
                    noResultsText1.setVisibility(View.VISIBLE);
                    noResultsText2.setVisibility(View.VISIBLE);

                }else{
                    noResultsImage.setVisibility(View.INVISIBLE);
                    noResultsText1.setVisibility(View.INVISIBLE);
                    noResultsText2.setVisibility(View.INVISIBLE);
                }

                adapter.setProperties(properties, myLocation);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();


            }

        });
    }

    /**
     * This method calls
     */

    private void getPermissions(){
        boolean isPermitted = false;

        Permissions permissions = new Permissions();
        if(permissions.checkPermissionsArray(getActivity(),Permissions.PERMISSIONS)){
            getLocation();

        }
        else{
            permissions.verifyPermissions(getActivity(), Permissions.PERMISSIONS);

            getProperties();

        }


    }


}