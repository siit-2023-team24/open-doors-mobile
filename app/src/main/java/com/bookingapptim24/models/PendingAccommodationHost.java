package com.bookingapptim24.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PendingAccommodationHost implements Parcelable, Serializable {

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("accommodationId")
    @Expose
    private Long accommodationId;

    @SerializedName("image")
    @Expose
    private Long image;

    @SerializedName("name")
    @Expose
    private String name;

    public PendingAccommodationHost() {}

    public PendingAccommodationHost(Long id, Long accommodationId, Long image, String name) {
        this.id = id;
        this.accommodationId = accommodationId;
        this.image = image;
        this.name = name;
    }

    public PendingAccommodationHost(Parcel in) {
        id = in.readLong();
        accommodationId = in.readLong();
        image = in.readLong();
        name = in.readString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(Long accommodationId) {
        this.accommodationId = accommodationId;
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
        return "PendingAccommodationHost{" +
                "id=" + id +
                ", accommodationId=" + accommodationId +
                ", image=" + image +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public int describeContents() {return 0; }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(accommodationId);
        dest.writeLong(image);
        dest.writeString(name);
    }

    public static final Creator<PendingAccommodationHost> CREATOR = new Creator<PendingAccommodationHost>() {
        @Override
        public PendingAccommodationHost createFromParcel(Parcel source) {
            return new PendingAccommodationHost(source);
        }

        @Override
        public PendingAccommodationHost[] newArray(int size) {
            return new PendingAccommodationHost[size];
        }
    };

}
