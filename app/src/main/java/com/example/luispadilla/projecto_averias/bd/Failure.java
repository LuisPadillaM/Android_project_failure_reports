package com.example.luispadilla.projecto_averias.bd;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Failure implements Parcelable, Cloneable {

    public String id;
    @SerializedName("nombre")
    public String name;
    @SerializedName("tipo")
    public String type;
    @SerializedName("usuario")
    public User user;
    @SerializedName("fecha")
    public String date;
    @SerializedName("descripcion")
    public String description;
    @SerializedName("imagen")
    public String image;
    @SerializedName("ubicacion")
    public Location location;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Failure(String id, String name, String type, User user, String date, String description, String image, Location location) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.user = user;
        this.date = date;
        this.description = description;
        this.image = image;
        this.location = location;
    }

    @Override
    public Object clone() {
        Parcel parcel = Parcel.obtain();
        this.writeToParcel(parcel, 0);
        byte[] bytes = parcel.marshall();

        Parcel parcel2 = Parcel.obtain();
        parcel2.unmarshall(bytes, 0, bytes.length);
        parcel2.setDataPosition(0);
        return Failure.CREATOR.createFromParcel(parcel2);
    }

    public Object regularClone(){
        Failure result = null;
        try {
            result = (Failure) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return result;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.type);
        dest.writeString(this.date);
        dest.writeString(this.description);
        dest.writeString(this.image);
        dest.writeParcelable(this.location, flags);
    }

    protected Failure(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.type = in.readString();
        this.date = in.readString();
        this.description = in.readString();
        this.image = in.readString();
        this.location = in.readParcelable(Location.class.getClassLoader());
    }

    public static final Creator<Failure> CREATOR = new Creator<Failure>() {
        @Override
        public Failure createFromParcel(Parcel source) {
            return new Failure(source);
        }

        @Override
        public Failure[] newArray(int size) {
            return new Failure[size];
        }
    };

    public static class Builder {
        private String id;
        private String name;
        private String type;
        private User user;
        private String date;
        private String description;
        private String image;
        private Location location;
        public Builder(){
            // TODO make validation should always have user and location
        }
        public Failure build(){
            return new Failure(this);
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder date(String date) {
            this.date = date;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder image(String image) {
            this.image = image;
            return this;
        }

        public Builder location(Location location) {
            this.location = location;
            return this;
        }
    }
    private Failure(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.type = builder.type;
        this.user = builder.user;
        this.date = builder.date;
        this.description = builder.description;
        this.image = builder.image;
        this.location = builder.location;
    }
}
