package com.example.dttrealestate.Adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;


import com.example.dttrealestate.Model.Property;
import com.example.dttrealestate.R;
import com.example.dttrealestate.Utils.Constants;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{

    ArrayList<Property> properties = new ArrayList<>();
    LatLng myLocation;

    //Constructor of recyclerview adapter which we provide a full list with properties and the user location
    public void setProperties(List<Property> properties, LatLng myLocation){
        this.properties = (ArrayList<Property>) properties;
        this.myLocation = myLocation;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.property_layout, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final Property item = properties.get(position);
        DecimalFormat price = new DecimalFormat("$###,###,###,###");

        holder.cityView.setText(item.getCity());
        holder.priceView.setText(price.format((item.getPrice())));
        holder.zipView.setText(item.getZip());
        holder.sizeView.setText(Integer.toString(item.getSize()));
        holder.bedroomsView.setText(Integer.toString(item.getBathrooms()));
        holder.bathroomsView.setText(Integer.toString(item.getBedrooms()));
        Picasso.get().load(Constants.IMAGE_URL + item.getImage()).into(holder.imageView);

        holder.propertyLocation = new LatLng(item.getLatitude(),item.getLongitude());

        if(myLocation.latitude == 0 && myLocation.longitude == 0){
            holder.distanceView.setText("unknown");
        }
        else{
            holder.distance = SphericalUtil.computeDistanceBetween(myLocation, holder.propertyLocation);

            holder.d = String.valueOf(holder.df.format(holder.distance/1000));

            holder.distanceView.setText(holder.d);
        }

    }

    @Override
    public int getItemCount() {
        return properties.size();
    }





    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ConstraintLayout houseConstraint;

        private ImageView imageView, searchStateEmptyImage;
        private TextView zipView, cityView, priceView, sizeView, bedroomsView, bathroomsView, distanceView;
        private LatLng propertyLocation = new LatLng(0,0);
        double distance;
        DecimalFormat df = new DecimalFormat(".0 km");
        String d;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            zipView=itemView.findViewById(R.id.zipView);
            cityView=itemView.findViewById(R.id.cityView);
            imageView=itemView.findViewById(R.id.imageView);
            priceView=itemView.findViewById(R.id.priceView);
            sizeView=itemView.findViewById(R.id.sizeView);
            bedroomsView=itemView.findViewById(R.id.bedroomsView);
            bathroomsView=itemView.findViewById(R.id.bathroomsView);
            distanceView=itemView.findViewById(R.id.distanceView);
            searchStateEmptyImage=itemView.findViewById(R.id.noResultsImage);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Property property = properties.get(getAdapterPosition());
            Bundle bundle = new Bundle();
            bundle.putString("city", property.getCity());
            bundle.putInt("price", property.getPrice());
            bundle.putString("description", property.getDescription());
            bundle.putString("image", "https://intern.docker-dev.d-tt.nl" + property.getImage());
            bundle.putString("bedrooms", Integer.toString(property.getBedrooms()));
            bundle.putString("bathrooms", Integer.toString(property.getBathrooms()));
            bundle.putString("size", Integer.toString(property.getSize()));
            bundle.putInt("latitude", property.getLatitude());
            bundle.putInt("longitude", property.getLongitude());
            bundle.putString("distance", d);


            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment);
            navController.navigate(R.id.action_homeFragment_to_detailsFragment, bundle);


        }
    }
}
