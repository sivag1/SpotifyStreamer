package com.sivag1.spotifystreamer.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sivag1 on 6/16/15.
 */
public class Artist implements Parcelable {

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String imageURI;
    private String name;

    public String getSpotityId() {
        return spotityId;
    }

    public void setSpotityId(String spotityId) {
        this.spotityId = spotityId;
    }

    private String spotityId;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(spotityId);
        dest.writeString(imageURI);
    }

    public Artist() {

    }

    private Artist(Parcel in) {
        this.name = in.readString();
        this.spotityId = in.readString();
        this.imageURI = in.readString();
    }

    public static final Parcelable.Creator<Artist> CREATOR = new Parcelable.Creator<Artist>() {

        @Override
        public Artist createFromParcel(Parcel source) {
            return new Artist(source);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };
}
