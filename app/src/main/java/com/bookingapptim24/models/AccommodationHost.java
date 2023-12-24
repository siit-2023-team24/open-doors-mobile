package com.bookingapptim24.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AccommodationHost implements Parcelable, Serializable {

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("image")
    @Expose
    private Long image;

    @SerializedName("name")
    @Expose
    private String name;


    public AccommodationHost() {}

    public AccommodationHost(Long id, Long image, String name) {
        this.id = id;
        this.image = image;
        this.name = name;
    }

    protected AccommodationHost(Parcel in) {
        id = in.readLong();
        image = in.readLong();
        name = in.readString();
    }

    public Long getId() {
        return id;
    }

    public Long getImage() {
        return image;
    }

    public void setImage(Long image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AccommodationHostDTO{" +
                "id=" + id +
                ", image=" + image +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(image);
        dest.writeString(name);
    }

    public static final Creator<AccommodationHost> CREATOR = new Creator<AccommodationHost>() {
        @Override
        public AccommodationHost createFromParcel(Parcel in) {
            return new AccommodationHost(in);
        }

        @Override
        public AccommodationHost[] newArray(int size) {
            return new AccommodationHost[size];
        }
    };

}
