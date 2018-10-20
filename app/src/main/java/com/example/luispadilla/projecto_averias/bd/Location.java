package com.example.luispadilla.projecto_averias.bd;

import android.os.Parcel;
import android.os.Parcelable;

public class Location implements Parcelable {
    public  double lat;
    public  double lon;

    public Location(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    protected Location(Parcel in) {
        lat = in.readDouble();
        lon = in.readDouble();
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(lat);
        parcel.writeDouble(lon);
    }
}
