package com.example.dttrealestate.Model;

import java.util.Comparator;

public class Property {

    public Property(int id, String image, int price, int bedrooms, int bathrooms, int size, String description, String zip, String city, int longitude, int latitude, String createdDate) {
        this.id = id;
        this.image = image;
        this.price = price;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.size = size;
        this.description = description;
        this.zip = zip;
        this.city = city;
        this.longitude = longitude;
        this.latitude = latitude;
        this.createdDate = createdDate;
    }


    private int id;
    private String image;
    private int price;
    private int bedrooms;
    private int bathrooms;
    private int size;
    private String description;
    private String zip;
    private String city;
    private int longitude;
    private int latitude;
    private String createdDate;

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public int getPrice() {
        return price;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public int getBathrooms() {
        return bathrooms;
    }

    public int getSize() {
        return size;
    }

    public String getDescription() {
        return description;
    }

    public String getZip() {
        return zip;
    }

    public String getCity() {
        return city;
    }

    public int getLongitude() {
        return longitude;
    }

    public int getLatitude() {
        return latitude;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public static Comparator<Property> propertyPrice = new Comparator<Property>() {
        @Override
        public int compare(Property e1, Property e2) {
            return e1.price - e2.price;
        }
    };




}
