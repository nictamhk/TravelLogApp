package com.example.travellog;

import android.os.Parcel;
import android.os.Parcelable;

public class POI extends Object implements Parcelable {
    private String name, address, city, country, longitude, latitude, category;
    private int distance, visited_times, here_now;

    // Cases where visited_times cannot be retrieved/defined
    public POI (String name, String address, String city, String country,
                String longitude, String latitude, String category, int distance) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.country = country;
        this.longitude = longitude;
        this.latitude = latitude;
        this.category = category;
        this.distance = distance;
        this.visited_times = 0;
        this.here_now = 0;
    }

    public POI (String name, String address, String city, String country,
                String longitude, String latitude, String category, int distance,
                int visited_times, int here_now)
    {
        this.name = name;
        this.address = address;
        this.city = city;
        this.country = country;
        this.longitude = longitude;
        this.latitude = latitude;
        this.category = category;
        this.distance = distance;
        this.visited_times = visited_times;
        this.here_now = here_now;
    }

    public POI ()
    {
        this.name = "";
        this.address = "";
        this.city = "";
        this.country = "";
        this.longitude = "0.00";
        this.latitude = "0.00";
        this.category = "";
        this.distance = 0;
        this.visited_times = 0;
        this.here_now = 0;
    }

    public POI (Parcel source) {
        name = source.readString();
        address = source.readString();
        city = source.readString();
        country = source.readString();
        longitude = source.readString();
        latitude = source.readString();
        category = source.readString();
        distance = source.readInt();
        visited_times = source.readInt();
        here_now = source.readInt();
    }

    public String getName (){
        return this.name;
    }

    public int getDistance (){
        return this.distance;
    }

    public int getVisitedTimes (){
        return this.visited_times;
    }

    public boolean getVisitedBefore (){
        if (this.getVisitedTimes() > 0)
            return true;
        else
            return false;
    }

    public int getHereNow(){
        return this.here_now;
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(city);
        dest.writeString(country);
        dest.writeString(longitude);
        dest.writeString(latitude);
        dest.writeString(category);
        dest.writeInt(distance);
        dest.writeInt(visited_times);
        dest.writeInt(here_now);
    }

    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public POI createFromParcel(Parcel in) {
            return new POI(in);
        }

        public POI[] newArray(int size) {
            return new POI[size];
        }
    };
}
