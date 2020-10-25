package com.example.dttrealestate.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dttrealestate.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;


public class DetailsFragment extends Fragment implements OnMapReadyCallback {

    //Widgets
    private TextView description, bedrooms, bathrooms, size, price, distance;
    private ImageView houseImage, backImage;
    private MapView mapView;
    private DecimalFormat priceFormat = new DecimalFormat("$###,###,###,###");


    public DetailsFragment() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_details, container, false);

        description = view.findViewById(R.id.descriptionContentView);
        price = view.findViewById(R.id.priceTextView);
        bedrooms = view.findViewById(R.id.bedroomsTextView);
        bathrooms = view.findViewById(R.id.bathroomsTextView);
        size = view.findViewById(R.id.sizeTextView);
        distance = view.findViewById(R.id.distanceTextView);
        houseImage = view.findViewById(R.id.houseImageView);
        backImage = view.findViewById(R.id.backImageView);


        //Sets the textView of clicked element from recyclerViewAdapter using bundle keys
        price.setText(priceFormat.format(getArguments().getInt("price")));
        description.setText(getArguments().getString("description"));
        bedrooms.setText(getArguments().getString("bedrooms"));
        bathrooms.setText(getArguments().getString("bathrooms"));
        size.setText(getArguments().getString("size"));
        distance.setText(getArguments().getString("distance"));
        Picasso.get().load(getArguments().getString("image")).into(houseImage);






        mapView = (MapView) view.findViewById(R.id.mapView);
        if (mapView != null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }


        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().onBackPressed();

            }
        });

        return view;

    }

    //Initialization of google maps
    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        //Sets google map type
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Sets a marker on the map which latitude and longitude is retrieved
        //from clicked item on recyclerview adapter and passes them with a bundle key
        googleMap.addMarker(new MarkerOptions().position(new LatLng(getArguments().getInt("latitude"), getArguments().getInt("longitude"))));

        //Sets the camera position view
        CameraPosition address = CameraPosition.builder().target(new LatLng(getArguments().getInt("latitude"), getArguments().getInt("longitude"))).zoom(10).bearing(0).tilt(0).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(address));



    }
}