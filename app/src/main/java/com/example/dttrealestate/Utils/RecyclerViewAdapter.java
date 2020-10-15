package com.example.dttrealestate.Utils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> implements Filterable{

    ArrayList<Property> properties = new ArrayList<>();
    ArrayList<Property> propertiesFiltered;
    LatLng myLocation;

    //Constructor of recyclerview adapter which we provide a full list with properties and the user location
    public void setProperties(List<Property> properties, LatLng myLocation){
        this.properties = (ArrayList<Property>) properties;
        propertiesFiltered = new ArrayList<>(properties);
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
        final Property currentItem = properties.get(position);

        holder.cityView.setText(currentItem.getCity());
        holder.priceView.setText(Integer.toString(currentItem.getPrice()));
        holder.zipView.setText(currentItem.getZip());
        holder.sizeView.setText(Integer.toString(currentItem.getSize()));
        holder.bedroomsView.setText(Integer.toString(currentItem.getBathrooms()));
        holder.bathroomsView.setText(Integer.toString(currentItem.getBedrooms()));
        Picasso.get().load("https://intern.docker-dev.d-tt.nl"+ currentItem.getImage()).into(holder.imageView);

        holder.propertyLocation = new LatLng(currentItem.getLatitude(),currentItem.getLongitude());

        if(myLocation.latitude == 0 && myLocation.longitude == 0){
            holder.distanceView.setText("uknowkn");
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

    @Override
    public Filter getFilter() {
        return propertiesFilter;
    }

    private Filter propertiesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Property> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(propertiesFiltered);
            }
            else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Property item: propertiesFiltered){
                    //if zip address of each item in the list matches the typed text from user then it adds it to the filtered list
                    if (item.getZip().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);

                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            results.count = filteredList.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            properties.clear();

            FilterResults r = new FilterResults();
            r = results;
            properties.addAll((List)results.values);
            Collections.sort(properties, Property.propertyPrice);
            notifyDataSetChanged();

        }
    };



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ConstraintLayout houseConstraint;

        private ImageView imageView, searchStateEmptyImage;
        private TextView zipView, cityView, priceView, sizeView, bedroomsView, bathroomsView, distanceView;
        private LatLng propertyLocation = new LatLng(0,0);
        double distance;
        DecimalFormat df = new DecimalFormat(".0");
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
            searchStateEmptyImage=itemView.findViewById(R.id.searchStateEmptyImage);

            //Toast.makeText(mContext, Integer.toString(a), Toast.LENGTH_SHORT).show();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Property property = properties.get(getAdapterPosition());
            Bundle bundle = new Bundle();
            bundle.putString("city", property.getCity());
            bundle.putString("price", Integer.toString(property.getPrice()));
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
